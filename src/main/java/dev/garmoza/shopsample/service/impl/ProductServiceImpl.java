package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.ProductDto;
import dev.garmoza.shopsample.dto.ProductDtoMapper;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.Product;
import dev.garmoza.shopsample.exception.CategoryNotFoundException;
import dev.garmoza.shopsample.exception.OrganizationNotFoundException;
import dev.garmoza.shopsample.exception.ProductNotFoundException;
import dev.garmoza.shopsample.repository.CategoryRepository;
import dev.garmoza.shopsample.repository.OrganizationRepository;
import dev.garmoza.shopsample.repository.ProductRepository;
import dev.garmoza.shopsample.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrganizationRepository organizationRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDtoMapper dtoMapper;

    @Override
    public Product saveNewProduct(Organization organization, Category category, Product product) {
        product.setOrganization(organization);
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public ProductDto.Default createProduct(ProductDto.Create productDto) {
        Product newProduct = dtoMapper.toEntity(productDto);
        Organization organization = organizationRepository.findById(productDto.organizationId())
                .orElseThrow(() -> new OrganizationNotFoundException(productDto.organizationId()));
        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.categoryId()));

        Product product = saveNewProduct(organization, category, newProduct);

        return dtoMapper.toDtoDefault(product);
    }

    @Override
    public List<ProductDto.Default> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(dtoMapper::toDtoDefault)
                .toList();
    }

    @Override
    public ProductDto.Full findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return dtoMapper.toDtoFull(product);
    }

    @Override
    public ProductDto.Default updateProduct(ProductDto.Default productDto) {
        Product updatedProduct = productRepository.findById(productDto.id())
                .map(product -> {
                    product.setName(productDto.name());
                    product.setDescription(productDto.description());
                    product.setPrice(productDto.price());
                    product.setStock(productDto.stock());
                    product.setStatus(productDto.status());
                    product.setCreationTime(productDto.creationTime());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException(productDto.id()));

        return dtoMapper.toDtoDefault(updatedProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
