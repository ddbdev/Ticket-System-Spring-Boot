package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.entity.Category;
import it.ddbdev.ticketsystem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> getCategoryByName(String name){
        return categoryRepository.getByCategoryName(name);
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(String id){
        return categoryRepository.findById(id);
    }

    public List<Category> getCategoriesJPQL(){
        return categoryRepository.getCategoriesJPQL();
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }


}
