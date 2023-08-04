package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAllByParentIsNull();
}
