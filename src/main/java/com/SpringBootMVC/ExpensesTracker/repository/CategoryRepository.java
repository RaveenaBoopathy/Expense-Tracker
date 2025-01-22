package com.SpringBootMVC.ExpensesTracker.repository;

import com.SpringBootMVC.ExpensesTracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Find a list of categories by name
    List<Category> findByName(String name);
}
