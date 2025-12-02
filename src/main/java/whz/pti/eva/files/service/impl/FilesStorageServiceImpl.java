package whz.pti.eva.files.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whz.pti.eva.files.service.FilesStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Override
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = generateFileName(file);
        String uploadDir = "files";

        try {
            saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + fileName, e);
        }

        return "/pizza-images/" + fileName;
    }

    @Override
    public Resource load(String filename) {
        try {
            String mainPath = "./" + getPathToDirectory(filename);
            Path file = Paths.get(mainPath).resolve(getPath(filename));
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Failed to load image: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load image: " + filename, e);
        }
    }

    @Override
    public void delete(String image) {
        String finalImage = getPathToDirectory(image);
        try (Stream<Path> paths = Files.walk(Paths.get(finalImage))) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete image at path: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image at path: " + image, e);
        }
    }


    private String generateFileName(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        return UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    private static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String getPathToDirectory(String image) {
        int lastSlashIndex = image.lastIndexOf('/');
        return (lastSlashIndex != -1) ? image.substring(0, lastSlashIndex) : image;
    }

    private String getPath(String filename) {
        int lastSlashIndex = filename.lastIndexOf('/');
        return (lastSlashIndex != -1) ? filename.substring(lastSlashIndex + 1) : filename;
    }
}
