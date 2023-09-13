package com.erp.convert_to_journals.service;

import com.erp.convert_to_journals.entity.*;
import com.erp.convert_to_journals.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Date;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Component
public class JournalEntryService {

    Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ReceivableRepository receivableRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    PayableRepository payableRepository;

    @Autowired
    PurchaseRepository purchaseRepository;



    @Scheduled(cron ="0 47 23 * * ?")
    public void executeAndSaveJournalEntries() throws IOException {

        Date startDate = Date.valueOf("2022-04-01");
        Date endDate = Date.valueOf("2023-03-31");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!startDate.after(endDate)) {
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based
            int year = calendar.get(Calendar.YEAR);
            Month monthNumber = Month.of(month);
            String monthName = monthNumber.toString();

            logger.info("Scheduled task executing..." + startDate + ":" + endDate + monthName + "-" + year);


            // Fetch sales data for the current month and year
            List<Sale> saleList = saleRepository.findSalesInvoicesByMonthAndYear(month, year);
            List<Receivable> receivableList = receivableRepository.findReceivablesByMonthAndYear(month, year);
            List<Expense> expenseList = expenseRepository.findExpensesByMonthAndYear(month, year);
            List<Payable> payableList = payableRepository.findPayableByMonthAndYear(month, year);
            List<Purchase> purchaseList = purchaseRepository.findPurchaseByMonthAndYear(month, year);

            logger.info("Found {} sales for {}_{}", saleList.size(), monthName, year);
            logger.info("Found {} receivables for {}_{}", receivableList.size(), monthName, year);
            logger.info("Found {} expenses for {}_{}", expenseList.size(), monthName, year);
            logger.info("Found {} payables for {}_{}", payableList.size(), monthName, year);
            logger.info("Found {} purchases for {}_{}", purchaseList.size(), monthName, year);


            if (!saleList.isEmpty()) {
                createExcelForJournalEntriesSale(month, year, saleList, "JournalEntriesForSale.xlsx");
                createPdfsForJournalEntriesSale(month, year, saleList);
                createExcelForJournalEntriesReceivable(month, year, receivableList, "JournalEntriesForReceivable.xlsx");
                createPdfsForJournalEntriesReceivable(month, year, receivableList);
                createExcelForJournalEntriesExpense(month, year, expenseList, "JournalEntriesForExpense.xlsx");
                createPdfsForJournalEntriesExpense(month, year, expenseList);
                createExcelForJournalEntriesPayable(month, year, payableList, "JournalEntriesForPayable.xlsx");
                createPdfsForJournalEntriesPayable(month, year, payableList);
                createExcelForJournalEntriesPurchase(month, year, purchaseList, "JournalEntriesForPurchase.xlsx");
                createPdfsForJournalEntriesPurchase(month, year, purchaseList);
            }

            // Move to the next month
            calendar.add(Calendar.MONTH, 1);
            startDate = new java.sql.Date(calendar.getTimeInMillis());
        }
    }

    public void createExcelForJournalEntriesSale(int month, int year, List<Sale> saleList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Sale sale : saleList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(sale.getSalesDate().toString());
                row1.createCell(1).setCellValue("sold goods to " + sale.getCustomerName() + " for rs " + sale.getSaleAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(sale.getSaleAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue(sale.getSalesDate().toString());
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(sale.getSaleAmount());
                row2.createCell(5).setCellValue("nominal account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesSale(int month, int year, List<Sale> saleList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_Sale_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Sale sale : saleList) {
                table.addCell(sale.getSalesDate().toString());
                table.addCell("sold goods to " + sale.getCustomerName() + " for rs " + sale.getSaleAmount());
                table.addCell("cash a/c");
                table.addCell("d");
                table.addCell(sale.getSaleAmount().toString());
                table.addCell("real account");

                table.addCell("");
                table.addCell("");
                table.addCell("to sales a/c");
                table.addCell("c");
                table.addCell(sale.getSaleAmount().toString());
                table.addCell("nominal account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesReceivable(int month, int year, List<Receivable> receivableList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Receivable receivable : receivableList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(receivable.getReceivableDate().toString());
                row1.createCell(1).setCellValue("sold goods to " + receivable.getCustomerName() + " for rs " + receivable.getReceivableAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(receivable.getReceivableAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(receivable.getReceivableAmount());
                row2.createCell(5).setCellValue("nominal account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesReceivable(int month, int year, List<Receivable> receivableList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_receivable_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Receivable receivable : receivableList) {
                table.addCell(receivable.getReceivableDate().toString());
                table.addCell("sold goods to " + receivable.getCustomerName() + " for rs " + receivable.getReceivableAmount());
                table.addCell("cash a/c");
                table.addCell("d");
                table.addCell(receivable.getReceivableAmount().toString());
                table.addCell("real account");

                table.addCell("");
                table.addCell("");
                table.addCell("to sales a/c");
                table.addCell("c");
                table.addCell(receivable.getReceivableAmount().toString());
                table.addCell("nominal account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesExpense(int month, int year, List<Expense> expenseList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Expense expense: expenseList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(expense.getExpenseDate().toString());
                row1.createCell(1).setCellValue("Paid for "+expense.getExpenseType()+" to " + expense.getExpenseName() + " rs " + expense.getExpenseAmount());
                row1.createCell(2).setCellValue(expense.getExpenseType()+" a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(expense.getExpenseAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(expense.getExpenseAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesExpense(int month, int year, List<Expense> expenseList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_expense_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Expense expense: expenseList) {
                table.addCell(expense.getExpenseDate().toString());
                table.addCell("paid for "+expense.getExpenseType()+" to " + expense.getExpenseName() + " rs " + expense.getExpenseAmount());
                table.addCell(expense.getExpenseType()+" a/c");
                table.addCell("d");
                table.addCell(expense.getExpenseAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to cash a/c");
                table.addCell("c");
                table.addCell(expense.getExpenseAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesPayable(int month, int year, List<Payable> payableList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Payable payable: payableList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(payable.getPayableDate().toString());
                row1.createCell(1).setCellValue("purchased goods from " + payable.getSupplierName() + " worth rs " + payable.getPayableAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(payable.getPayableAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(payable.getPayableAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesPayable(int month, int year, List<Payable> payableList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_payable_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Payable payable: payableList) {
                table.addCell(payable.getPayableDate().toString());
                table.addCell("purchased goods from " + payable.getSupplierName() + " worth rs " + payable.getPayableAmount());
                table.addCell("purchase a/c");
                table.addCell("d");
                table.addCell(payable.getPayableAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to cash a/c");
                table.addCell("c");
                table.addCell(payable.getPayableAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesPurchase(int month, int year, List<Purchase> purchaseList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Purchase purchase: purchaseList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(purchase.getPurchaseDate().toString());
                row1.createCell(1).setCellValue("purchased goods from " + purchase.getSupplierName() + " worth rs " + purchase.getPurchaseAmount());
                row1.createCell(2).setCellValue("purchase a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(purchase.getPurchaseAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(purchase.getPurchaseAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesPurchase(int month, int year, List<Purchase> purchaseList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_purchases_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Purchase purchase: purchaseList) {
                table.addCell(purchase.getPurchaseDate().toString());
                table.addCell("purchased goods from " + purchase.getSupplierName() + " worth rs " + purchase.getPurchaseAmount());
                table.addCell("purchase a/c");
                table.addCell("d");
                table.addCell(purchase.getPurchaseAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to cash a/c");
                table.addCell("c");
                table.addCell(purchase.getPurchaseAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}