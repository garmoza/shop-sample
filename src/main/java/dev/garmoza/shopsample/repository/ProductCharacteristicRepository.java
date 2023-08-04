package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.ProductCharacteristic;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface ProductCharacteristicRepository extends ListCrudRepository<ProductCharacteristic, Long> {
    List<ProductCharacteristic> findAllByProductId(Long productId);
}
