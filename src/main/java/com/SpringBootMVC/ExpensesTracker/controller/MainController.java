package com.SpringBootMVC.ExpensesTracker.controller;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.service.CategoryService;
import com.SpringBootMVC.ExpensesTracker.service.ExpenseService;
import com.SpringBootMVC.ExpensesTracker.service.ReportService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final ReportService reportService;

    @Autowired
    public MainController(ExpenseService expenseService, CategoryService categoryService, ReportService reportService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String landingPage(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        model.addAttribute("sessionClient", client);
        return "landing-page";
    }

    @GetMapping("/showAdd")
    public String addExpense(Model model) {
        model.addAttribute("expense", new ExpenseDTO());
        return "add-expense";
    }

    @PostMapping("/submitAdd")
    public String submitAdd(@ModelAttribute("expense") ExpenseDTO expenseDTO, HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        expenseDTO.setClientId(client.getId());

        String categoryName = expenseDTO.getCategory();
        Category category = categoryService.findCategoryByName(categoryName);

        if (category == null) {
            model.addAttribute("errorMessage", "Category not found. Please try again.");
            return "add-expense";
        }

        expenseDTO.setCategory(category.getName());
        expenseService.save(expenseDTO);
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        int clientId = client.getId();
        List<Expense> expenseList = expenseService.findAllExpensesByClientId(clientId);

        // Ensure date and time are set properly
        for (Expense expense : expenseList) {
            expense.setExpenseDateAndTime(); // Ensure this method formats the date/time correctly
        }

        // Adding categories to the model for dropdown
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("expenseList", expenseList);
        model.addAttribute("filter", new FilterDTO());
        model.addAttribute("categories", categories);

        return "list-page";
    }

    @GetMapping("/generateReport")
    public void generateReport(@RequestParam("format") String format, HttpSession session, HttpServletResponse response) throws IOException {
        Client client = (Client) session.getAttribute("client");
        List<Expense> expenseList = expenseService.findAllExpensesByClientId(client.getId());

        // Ensure date and time are set properly for the report
        for (Expense expense : expenseList) {
            expense.setExpenseDateAndTime(); // This should handle the formatting for the report
        }

        try {
            switch (format.toLowerCase()) {
                case "pdf":
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename=\"expense_report.pdf\"");
                    reportService.generatePDFReport(expenseList, client, response);
                    break;
                case "excel":
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment; filename=\"expense_report.xlsx\"");
                    reportService.generateExcelReport(expenseList, client, response);
                    break;
                case "csv":
                    response.setContentType("text/csv");
                    response.setHeader("Content-Disposition", "attachment; filename=\"expense_report.csv\"");
                    reportService.generateCSVReport(expenseList, client, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid report format.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating report.");
        }
    }

    @GetMapping("/showUpdate")
    public String showUpdate(@RequestParam("expId") int id, Model model) {
        Expense expense = expenseService.findExpenseById(id);
        ExpenseDTO expenseDTO = new ExpenseDTO();

        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setCategory(expense.getCategory() != null ? expense.getCategory().getName() : "No Category");
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDateTime(expense.getDateTime());

        model.addAttribute("expense", expenseDTO);
        model.addAttribute("expenseId", id);
        return "update-page";
    }

    @PostMapping("/submitUpdate")
    public String update(@RequestParam("expId") int id, @ModelAttribute("expense") ExpenseDTO expenseDTO, HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        expenseDTO.setExpenseId(id);
        expenseDTO.setClientId(client.getId());

        Category category = categoryService.findCategoryByName(expenseDTO.getCategory());
        if (category == null) {
            model.addAttribute("errorMessage", "Category not found. Please try again.");
            return "update-page";
        }

        expenseDTO.setCategory(category.getName());
        expenseService.update(expenseDTO);
        return "redirect:/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("expId") int id) {
        expenseService.deleteExpenseById(id);
        return "redirect:/list";
    }

    @PostMapping("/processFilter")
    public String processFilter(@ModelAttribute("filter") FilterDTO filter, Model model) {
        List<Expense> expenseList = expenseService.findFilterResult(filter);

        // Ensure date and time are set properly for filtered results
        for (Expense expense : expenseList) {
            expense.setExpenseDateAndTime(); // Ensure correct formatting
        }

        model.addAttribute("expenseList", expenseList);
        return "filter-result";
    }
}