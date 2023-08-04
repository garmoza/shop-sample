package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.Product;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static dev.garmoza.shopsample.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Organization org;
    private Category category;

    @BeforeEach
    void setUp() {
        product = new Product("product name", "desc", new BigDecimal("7.45"), 100);
        org = orgRepository.save(new Organization("org name", "desc", "logo"));
        category = categoryRepository.save(new Category("category name"));
        product.setOrganization(org);
        product.setCategory(category);
    }

    @Test
    void save() {
        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isPositive();
    }

    @Test
    void save_generatesId() {
        Product product2 = new Product("product name 2", "desc", new BigDecimal("7.45"), 100);
        product2.setOrganization(org);
        product2.setCategory(category);

        productRepository.save(product);
        productRepository.save(product2);

        assertEquals(product.getId() + 1, product2.getId());
    }

    @Test
    void save_generatesCreationTime() {
        productRepository.save(product);

        assertNotNull(product.getCreationTime());
    }

    @Test
    void findAll() {
        Product product2 = new Product("product name 2", "desc", new BigDecimal("7.45"), 100);
        product2.setOrganization(org);
        product2.setCategory(category);

        productRepository.save(product);
        productRepository.save(product2);

        List<Product> productList = productRepository.findAll();

        assertThat(productList).hasSize(2);
    }

    @Test
    void findById() {
        productRepository.save(product);

        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        assertThat(optionalProduct).isPresent();
    }

    @Test
    void findById_productNotExist() {
        productRepository.save(product);

        Optional<Product> optionalProduct = productRepository.findById(product.getId() + 1);

        assertThat(optionalProduct).isNotPresent();
    }

    @Test
    void save_update() {
        productRepository.save(product);

        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        assertThat(optionalProduct).isPresent();
        Product savedProduct = optionalProduct.get();
        savedProduct.setName("new name");
        savedProduct.setDescription("new desc");
        savedProduct.setPrice(new BigDecimal("2.22"));
        savedProduct.setStock(75);
        savedProduct.setStatus(Product.Status.FROZEN);
        savedProduct.setCreationTime(new Date(12345));

        Product updatedProduct = productRepository.save(savedProduct);

        assertThat(updatedProduct)
                .usingRecursiveComparison()
                .isEqualTo(savedProduct);
    }

    @Test
    void deleteById() {
        productRepository.save(product);

        productRepository.deleteById(product.getId());
        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        assertThat(optionalProduct).isNotPresent();
    }

    @Test
    void deleteById_productNotExist() {
        productRepository.save(product);

        productRepository.deleteById(product.getId() + 1);
        Optional<Product> optionalProduct = productRepository.findById(product.getId() + 1);

        assertThat(optionalProduct).isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_name_notBlank(String name) {
        product.setName(name);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertNotBlankAnnotation(e.getConstraintViolations(), "name");
    }

    @Test
    void validation_description_notNull() {
        product.setDescription(null);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertNotNullAnnotation(e.getConstraintViolations(), "description");
    }

    @Test
    void validation_price_positive() {
        BigDecimal price = new BigDecimal(0);
        product.setPrice(price);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertPositiveAnnotation(e.getConstraintViolations(), "price");
    }

    @Test
    void validation_stock_min0() {
        product.setStock(-1);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertMinAnnotation(e.getConstraintViolations(), "stock");
    }

    @Test
    void validation_status_notNull() {
        product.setStatus(null);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertNotNullAnnotation(e.getConstraintViolations(), "status");
    }

    @Test
    void validation_organization_notNull() {
        product.setOrganization(null);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertNotNullAnnotation(e.getConstraintViolations(), "organization");
    }

    @Test
    void validation_category_notNull() {
        product.setCategory(null);

        var e = assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
        assertNotNullAnnotation(e.getConstraintViolations(), "category");
    }
}