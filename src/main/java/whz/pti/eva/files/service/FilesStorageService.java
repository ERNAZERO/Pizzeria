package whz.pti.eva.files.service;
import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

    String save(MultipartFile file);

    Resource load(String filename);

    void delete(String image);

}
