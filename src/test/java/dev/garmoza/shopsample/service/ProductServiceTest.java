package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.ProductDto;
import dev.garmoza.shopsample.dto.ProductDtoMapper;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.Product;
import dev.garmoza.shopsample.repository.CategoryRepository;
import dev.garmoza.shopsample.repository.OrganizationRepository;
import dev.garmoza.shopsample.repository.ProductRepository;
import dev.garmoza.shopsample.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductDtoMapper productDtoMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    private Product product;
    private Organization org;
    private Category category;

    private ProductDto.Create productDtoCreate;
    private ProductDto.Default productDtoDefault;
    private ProductDto.Full productDtoFull;

    @BeforeEach
    void setUp() {
        product = new Product("product name", "product desc", new BigDecimal("4.99"), 50);
        product.setId(1L);
        org = new Organization("org name", "org desc", "logo");
        category = new Category("category name");

        productDtoCreate = new ProductDto.Create(
                1L,
                2L,
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50
        );
        productDtoDefault = new ProductDto.Default(
                1L,
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50,
                Product.Status.CONFIRMATION,
                new Date(12345)
        );
        productDtoFull = new ProductDto.Full(
                1L,
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50,
                Product.Status.CONFIRMATION,
                new Date(12345),
                1L,
                2L
        );
    }

    @Test
    void saveNewProduct() {
        given(productRepository.save(Mockito.any(Product.class))).will(i -> i.getArgument(0));

        Product savedProduct = productService.saveNewProduct(org, category, product);

        then(productRepository).should().save(productArgumentCaptor.capture());
        Product value = productArgumentCaptor.getValue();
        assertThat(value).isEqualTo(savedProduct);
        assertThat(value.getOrganization()).isEqualTo(org);
        assertThat(value.getCategory()).isEqualTo(category);
    }

    @Test
    void createProduct() {
        given(productDtoMapper.toEntity(productDtoCreate)).willReturn(product);
        given(organizationRepository.findById(productDtoCreate.organizationId())).willReturn(Optional.of(org));
        given(categoryRepository.findById(productDtoCreate.categoryId())).willReturn(Optional.of(category));
        given(productRepository.save(product)).willReturn(product);
        given(productDtoMapper.toDtoDefault(product)).willReturn(productDtoDefault);

        ProductDto.Default dto = productService.createProduct(productDtoCreate);

        then(productRepository).should().save(productArgumentCaptor.capture());
        Product value = productArgumentCaptor.getValue();
        assertThat(value.getOrganization()).isEqualTo(org);
        assertThat(value.getCategory()).isEqualTo(category);
        assertThat(dto).isEqualTo(productDtoDefault);
    }

    @Test
    void createProduct_organizationNotFound() {
        // todo
    }

    @Test
    void createProduct_userNotFound() {
        // todo
    }

    @Test
    void findAllProducts() {
        // todo
    }

    @Test
    void findProductById() {
        // todo
    }

    @Test
    void updateProduct() {
        // todo
    }

    @Test
    void deleteProductById() {
        // todo
    }
}