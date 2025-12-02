package whz.pti.eva.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.entity.PizzaPrice;
import whz.pti.eva.pizza.domain.enums.PizzaSize;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;
import whz.pti.eva.pizza.repository.PizzaRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class PizzaInitializer implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(PizzaInitializer.class);
    private final PizzaRepository pizzaRepository;
    private static final String SOURCE_DIRECTORY = "source-images";
    private static final String TARGET_DIRECTORY = "files";

    public PizzaInitializer(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        copyImagesToTargetFolder();
        if (pizzaRepository.count() == 0) {
            pizzaRepository.saveAll(createPizzas());
            log.info("✅Pizzas have been initialized");
        }
    }


    private List<Pizza> createPizzas() {
        return List.of(
                createPizza("Margherita", PizzaStatus.ACTIVE, "/pizza-images/margherita.png",
                        "The Margherita pizza is a timeless classic, featuring a blend of fresh tomatoes, creamy mozzarella, and aromatic basil leaves.",
                        List.of(5.99, 8.99, 12.99)),
                createPizza("Pepperoni", PizzaStatus.ACTIVE, "/pizza-images/pepperoni.png",
                        "Our Pepperoni pizza is the ultimate crowd-pleaser, made with a generous layer of sliced pepperoni atop a bed of gooey mozzarella cheese.",
                        List.of(6.99, 9.99, 13.99)),
                createPizza("Hawaiian", PizzaStatus.ARCHIVE, "/pizza-images/hawaiian.png",
                        "The Hawaiian pizza offers a unique combination of flavors, with juicy pineapple chunks and tender ham creating a perfect blend of sweet and savory.",
                        List.of(6.49, 9.49, 13.49)),
                createPizza("BBQ Chicken", PizzaStatus.ACTIVE, "/pizza-images/bbq_chicken.png",
                        "Our BBQ Chicken pizza is a savory delight for those who love bold flavors.",
                        List.of(7.49, 10.49, 14.49)),
                createPizza("Veggie", PizzaStatus.ACTIVE, "/pizza-images/veggie.png",
                        "The Veggie pizza is perfect for those looking for a fresh and healthy option, featuring a vibrant mix of colorful bell peppers, black olives, mushrooms, and red onions.",
                        List.of(6.49, 9.49, 13.49)),
                createPizza("Four Cheese", PizzaStatus.ARCHIVE, "/pizza-images/four_cheese.png",
                        "Our Four Cheese pizza is a dream for cheese lovers, made with a blend of mozzarella, parmesan, gorgonzola, and cheddar.",
                        List.of(7.99, 11.49, 15.49)),
                createPizza("Meat Lover's", PizzaStatus.ACTIVE, "/pizza-images/meat_lovers.png",
                        "The Meat Lover's pizza is packed with flavor, featuring a hearty mix of pepperoni, sausage, ham, and bacon.",
                        List.of(8.49, 12.49, 16.49)),
                createPizza("Spicy Italian", PizzaStatus.ACTIVE, "/pizza-images/spicy_italian.png",
                        "The Spicy Italian pizza is for those who love a little heat in every bite.",
                        List.of(7.49, 10.99, 14.99)),
                createPizza("Buffalo Chicken", PizzaStatus.ACTIVE, "/pizza-images/buffalo_chicken.png",
                        "Our Buffalo Chicken pizza is made for those who enjoy the spicy and tangy flavors of buffalo wings.",
                        List.of(7.99, 11.99, 15.99)),
                createPizza("Mushroom Delight", PizzaStatus.ACTIVE, "/pizza-images/mushroom_delight.png",
                        "The Mushroom Delight pizza is a simple yet flavorful choice for mushroom lovers.",
                        List.of(6.99, 9.99, 13.99)),
                createPizza("Greek", PizzaStatus.ACTIVE, "/pizza-images/greek.png",
                        "The Greek pizza features feta cheese, olives, tomatoes, and red onions, offering a Mediterranean twist.",
                        List.of(7.49, 10.99, 14.99)),
                createPizza("Tandoori Chicken", PizzaStatus.ACTIVE, "/pizza-images/tandoori_chicken.png",
                        "The Tandoori Chicken pizza is infused with Indian spices, topped with marinated chicken and fresh cilantro.",
                        List.of(8.49, 11.99, 15.99)),
                createPizza("Pesto Veggie", PizzaStatus.ACTIVE, "/pizza-images/pesto_veggie.png",
                        "Our Pesto Veggie pizza includes a vibrant pesto base, topped with spinach, tomatoes, and mozzarella.",
                        List.of(7.99, 11.49, 15.49)),
                createPizza("Truffle Mushroom", PizzaStatus.ACTIVE, "/pizza-images/truffle_mushroom.png",
                        "The Truffle Mushroom pizza is topped with sautéed mushrooms and a drizzle of truffle oil for an earthy flavor.",
                        List.of(9.49, 13.49, 17.49)),
                createPizza("Mexican Fiesta", PizzaStatus.ACTIVE, "/pizza-images/mexican_fiesta.png",
                        "The Mexican Fiesta pizza features spicy ground beef, jalapenos, and a blend of cheddar and mozzarella.",
                        List.of(8.49, 12.49, 16.49)),
                createPizza("Capricciosa", PizzaStatus.ACTIVE, "/pizza-images/capricciosa.png",
                        "The Capricciosa pizza includes ham, mushrooms, artichokes, and black olives for a balanced flavor.",
                        List.of(7.99, 11.99, 15.99)),
                createPizza("Prosciutto Arugula", PizzaStatus.ACTIVE, "/pizza-images/prosciutto_arugula.png",
                        "The Prosciutto Arugula pizza is topped with thinly sliced prosciutto and fresh arugula.",
                        List.of(9.49, 13.49, 17.49)),
                createPizza("Bacon Cheeseburger", PizzaStatus.ACTIVE, "/pizza-images/bacon_cheeseburger.png",
                        "The Bacon Cheeseburger pizza is loaded with ground beef, crispy bacon, and cheddar cheese.",
                        List.of(8.99, 12.99, 16.99)),
                createPizza("Garlic Shrimp", PizzaStatus.ACTIVE, "/pizza-images/garlic_shrimp.png",
                        "The Garlic Shrimp pizza features succulent shrimp, garlic, and a blend of mozzarella and parmesan.",
                        List.of(10.49, 14.99, 18.99)),
                createPizza("Chicken Alfredo", PizzaStatus.ACTIVE, "/pizza-images/chicken_alfredo.png",
                        "The Chicken Alfredo pizza has a creamy Alfredo sauce base, topped with grilled chicken and mozzarella.",
                        List.of(8.49, 12.49, 16.49))
        );
    }

    private Pizza createPizza(String name, PizzaStatus status, String imagePath, String description, List<Double> prices) {
        return new Pizza(
                name,
                status,
                List.of(
                        new PizzaPrice(PizzaSize.SMALL, prices.get(0)),
                        new PizzaPrice(PizzaSize.MEDIUM, prices.get(1)),
                        new PizzaPrice(PizzaSize.LARGE, prices.get(2))
                ),
                LocalDateTime.now(),
                imagePath,
                description
        );
    }

    private void copyImagesToTargetFolder() {
        try {
            File sourceDir = new File(PizzaInitializer.SOURCE_DIRECTORY);
            File targetDir = new File(PizzaInitializer.TARGET_DIRECTORY);

            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            File[] sourceFiles = sourceDir.listFiles();
            if (sourceFiles != null) {
                for (File sourceFile : sourceFiles) {
                    if (sourceFile.isFile()) {
                        Path targetPath = new File(targetDir, sourceFile.getName()).toPath();
                        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error copying images to target folder", e);
        }
    }

}
