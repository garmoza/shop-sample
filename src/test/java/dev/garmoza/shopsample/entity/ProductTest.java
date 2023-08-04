package dev.garmoza.shopsample.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("product name", "desc", new BigDecimal("7.75"), 100);
    }

    @Test
    void createProduct() {
        Product product1 = new Product("product name", "desc", new BigDecimal("7.75"), 100);

        assertNull(product1.getId());
        assertEquals("product name", product1.getName());
        assertEquals("desc", product1.getDescription());
        assertEquals(new BigDecimal("7.75"), product1.getPrice());
        assertEquals(100, product1.getStock());
        assertEquals(Product.Status.CONFIRMATION, product1.getStatus());
        assertNull(product1.getCreationTime());
        assertNull(product1.getOrganization());
        assertNull(product1.getCategory());
    }

    @Test
    void setId() {
        product.setId(2L);

        assertEquals(2L, product.getId());
    }

    @Test
    void setName() {
        product.setName("new name");

        assertEquals("new name", product.getName());
    }

    @Test
    void setDescription() {
        product.setDescription("new desc");

        assertEquals("new desc", product.getDescription());
    }

    @Test
    void setPrice() {
        product.setPrice(new BigDecimal("13.2"));

        assertEquals(new BigDecimal("13.2"), product.getPrice());
    }

    @Test
    void setStock() {
        product.setStock(25);

        assertEquals(25, product.getStock());
    }

    @Test
    void setStatus() {
        product.setStatus(Product.Status.FROZEN);

        assertEquals(Product.Status.FROZEN, product.getStatus());
    }

    @Test
    void setCreationTime() {
        product.setCreationTime(new Date(12345));

        assertEquals(new Date(12345), product.getCreationTime());
    }

    @Test
    void setOrganization() {
        Organization org = new Organization("org name", "org desc", "logo");
        product.setOrganization(org);

        assertEquals(org, product.getOrganization());
    }

    @Test
    void setCategory() {
        Category category = new Category("category name");
        product.setCategory(category);

        assertEquals(category, product.getCategory());
    }
}