package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface ProductRepository extends ListCrudRepository<Product, Long> {
}
