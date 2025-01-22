package com.SpringBootMVC.ExpensesTracker.service;

import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter; // Import for writing CSV files
import java.util.List;
import java.util.Date;

@Service
public class ReportService {

    // Method to generate PDF
    public void generatePDFReport(List<Expense> expenseList, Client client, HttpServletResponse response) {
        // Check if client is null and handle accordingly
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        // Set the content type for the PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expense-report.pdf");

        try {
            // Create the PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Add client information and a table of expenses
            String clientName = (client.getFirstName() != null ? client.getFirstName() : "") + " " +
                    (client.getLastName() != null ? client.getLastName() : "Unknown Client");
            document.add(new Paragraph("Expense Report for " + clientName));
            document.add(new Paragraph("Generated on: " + new Date()));

            // Add a table for the expenses
            PdfPTable table = new PdfPTable(6);
            table.addCell("S.No");
            table.addCell("Category");
            table.addCell("Amount ($)");
            table.addCell("Date");
            table.addCell("Time");
            table.addCell("Description");

            // Check if the expense list is not null and iterate through it
            if (expenseList != null && !expenseList.isEmpty()) {
                for (int i = 0; i < expenseList.size(); i++) {
                    Expense expense = expenseList.get(i);

                    // Ensure expense category is not null and retrieve the name
                    String category = (expense.getCategory() != null && expense.getCategory().getName() != null) 
                            ? expense.getCategory().getName() 
                            : "No Category"; // Accessing the name property of the Category object
                    
                    // Extracting date and time separately
                    String expenseDate = expense.getDateTime() != null ? expense.getDateTime().toLocalDate().toString() : "No Date";
                    String expenseTime = expense.getDateTime() != null ? expense.getDateTime().toLocalTime().toString() : "No Time";
                    
                    table.addCell(String.valueOf(i + 1));
                    table.addCell(category); // Safely handle null category
                    table.addCell(String.valueOf(expense.getAmount()));
                    table.addCell(expenseDate); // Date column
                    table.addCell(expenseTime); // Time column
                    table.addCell(expense.getDescription() != null ? expense.getDescription() : "No Description");
                }
            } else {
                document.add(new Paragraph("No expenses found."));
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate Excel report
    public void generateExcelReport(List<Expense> expenseList, Client client, HttpServletResponse response) throws IOException {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expense-report.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        // Header Row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("S.No");
        header.createCell(1).setCellValue("Category");
        header.createCell(2).setCellValue("Amount ($)");
        header.createCell(3).setCellValue("Date");
        header.createCell(4).setCellValue("Time");
        header.createCell(5).setCellValue("Description");

        if (expenseList != null && !expenseList.isEmpty()) {
            for (int i = 0; i < expenseList.size(); i++) {
                Expense expense = expenseList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);

                String categoryName = (expense.getCategory() != null ? expense.getCategory().getName() : "No Category");
                row.createCell(1).setCellValue(categoryName);

                row.createCell(2).setCellValue(expense.getAmount());

                String expenseDate = (expense.getDateTime() != null ? expense.getDateTime().toString() : "No Date");
                row.createCell(3).setCellValue(expenseDate);

                String expenseTime = (expense.getTime() != null ? expense.getTime().toString() : "No Time");
                row.createCell(4).setCellValue(expenseTime);

                row.createCell(5).setCellValue(expense.getDescription() != null ? expense.getDescription() : "No Description");
            }
        } else {
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("No expenses found.");
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }


    // Method to generate CSV report
    public void generateCSVReport(List<Expense> expenseList, Client client, HttpServletResponse response) throws IOException {
        // Set the content type for CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expense-report.csv");

        PrintWriter writer = response.getWriter();
        writer.append("S.No,Category,Amount ($),Date,Time,Description\n");

        // Write data rows
        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            writer.append(String.valueOf(i + 1))
                    .append(",")
                    .append(expense.getCategory().getName())
                    .append(",")
                    .append(String.valueOf(expense.getAmount()))
                    .append(",")
                    .append(expense.getDateTime() != null ? expense.getDateTime().toString() : "No Date")
                    .append(",")
                    .append(expense.getTime() != null ? expense.getTime().toString() : "No Time")
                    .append(",")
                    .append(expense.getDescription() != null ? expense.getDescription() : "No Description")
                    .append("\n");
        }

        writer.flush();
    }
}
