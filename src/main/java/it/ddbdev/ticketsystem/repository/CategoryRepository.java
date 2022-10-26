package it.ddbdev.ticketsystem.repository;

import it.ddbdev.ticketsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {


    @Query(value = "select c from Category c WHERE c.id = :category and c.visible = true")
    Optional<Category> getByCategoryName(@Param("category") String category);

    @Query(value = "select c from Category c where c.visible = true")
    List<Category> getCategoriesJPQL();


}
