package com.SpringBootMVC.ExpensesTracker.service;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;


import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findCategoryByName(String name) {
        List<Category> categories = categoryRepository.findByName(name);
        if (categories.size() > 1) {
            // Handle this scenario, for example, log a warning or throw an exception
            System.out.println("Multiple categories found with the name: " + name);
        }
        return categories.isEmpty() ? null : categories.get(0);  // Return first match or null if empty
    }

    @Override
    public Category findCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> filterCategories(FilterDTO filterDTO) {
        if (filterDTO.getCategory() != null) {
            return categoryRepository.findByName(filterDTO.getCategory());
        }
        return categoryRepository.findAll();  // Return all if no filter applied
    }
}