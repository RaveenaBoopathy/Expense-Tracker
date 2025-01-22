package com.SpringBootMVC.ExpensesTracker.service;

import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;

import java.util.List;

public interface CategoryService {
    Category findCategoryByName(String name);
    Category findCategoryById(int id);
    List<Category> findAllCategories();

    List<Category> filterCategories(FilterDTO filterDTO);  // Method for filtering
}
