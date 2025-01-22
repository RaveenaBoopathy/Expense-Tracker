package com.SpringBootMVC.ExpensesTracker.service;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.repository.ExpenseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ClientService clientService;
    private final CategoryService categoryService;
    private final EntityManager entityManager;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ClientService clientService,
                              CategoryService categoryService, EntityManager entityManager) {
        this.expenseRepository = expenseRepository;
        this.clientService = clientService;
        this.categoryService = categoryService;
        this.entityManager = entityManager;
    }

    @Override
    public Expense findExpenseById(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(ExpenseDTO expenseDTO) {
        // Create a new Expense object and convert LocalDateTime for dateTime
        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDateTime(expenseDTO.getDateTime());
        expense.setDescription(expenseDTO.getDescription());
        expense.setClient(clientService.findClientById(expenseDTO.getClientId()));

        // Fetch or fallback category
        Category category = categoryService.findCategoryByName(expenseDTO.getCategory());
        if (category == null) {
            category = categoryService.findCategoryByName("No Category"); // Ensure this exists in DB
        }
        expense.setCategory(category);

        // Save the expense
        expenseRepository.save(expense);
    }

    @Transactional
    @Override
    public void update(ExpenseDTO expenseDTO) {
        Expense existingExpense = expenseRepository.findById(expenseDTO.getExpenseId()).orElse(null);

        if (existingExpense != null) {
            existingExpense.setAmount(expenseDTO.getAmount());
            existingExpense.setDateTime(expenseDTO.getDateTime());
            existingExpense.setDescription(expenseDTO.getDescription());

            // Fetch or fallback category
            Category category = categoryService.findCategoryByName(expenseDTO.getCategory());
            if (category == null) {
                category = categoryService.findCategoryByName("No Category");
            }
            existingExpense.setCategory(category);

            // Save the updated expense
            expenseRepository.save(existingExpense);
        }
    }

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findAllExpensesByClientId(int id) {
        return expenseRepository.findByClientId(id);
    }

    @Transactional
    @Override
    public void deleteExpenseById(int id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public List<Expense> findFilterResult(FilterDTO filter) {
        String query = "SELECT e FROM Expense e WHERE 1=1";

        if (!"all".equals(filter.getCategory())) {
            Category category = categoryService.findCategoryByName(filter.getCategory());
            if (category != null) {
                query += " AND e.category.id = " + category.getId();
            }
        }

        query += " AND e.amount BETWEEN " + filter.getFrom() + " AND " + filter.getTo();

        if (!"all".equals(filter.getYear())) {
            query += " AND FUNCTION('YEAR', e.dateTime) = " + filter.getYear();
        }

        if (!"all".equals(filter.getMonth())) {
            query += " AND FUNCTION('MONTH', e.dateTime) = " + filter.getMonth();
        }

        TypedQuery<Expense> expenseTypedQuery = entityManager.createQuery(query, Expense.class);
        return expenseTypedQuery.getResultList();
    }
}