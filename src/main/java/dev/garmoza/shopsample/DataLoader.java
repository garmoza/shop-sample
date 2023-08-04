package dev.garmoza.shopsample;

import dev.garmoza.shopsample.entity.*;
import dev.garmoza.shopsample.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final OrganizationService organizationService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CharacteristicService characteristicService;
    private final ProductCharacteristicService productCharacteristicService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("username", "test@mail.com", "pass");
        user = userService.saveNewUser(user);

        Organization org = new Organization("org name", "org desc", "logo");
        org = organizationService.saveNewOrganization(user, org);

        Category category = new Category("category name");
        category = categoryService.saveNewCategory(category);

        Product product = new Product("product name", "product desc", new BigDecimal("4.95"), 100);
        product = productService.saveNewProduct(org, category, product);

        Characteristic char1 = new CharacteristicText("text char name");
        Characteristic char2 = new CharacteristicSelect("select char name", List.of("select1", "select2"));
        characteristicService.saveNewCharacteristic(category, char1);
        characteristicService.saveNewCharacteristic(category, char2);

        ProductCharacteristic productCharacteristicText = new ProductCharacteristicText("Some text value");
        ProductCharacteristic productCharacteristicSelect = new ProductCharacteristicSelect(Set.of("select2"));
        productCharacteristicService.saveNewProductCharacteristic(product, char1, productCharacteristicText);
        productCharacteristicService.saveNewProductCharacteristic(product, char2, productCharacteristicSelect);
    }
}
