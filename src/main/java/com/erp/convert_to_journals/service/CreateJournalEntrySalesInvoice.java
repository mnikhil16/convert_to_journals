package com.erp.convert_to_journals.service;

import com.erp.convert_to_journals.entity.SalesInvoice;
import com.erp.convert_to_journals.repository.SalesInvoiceRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
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
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CreateJournalEntrySalesInvoice {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntrySalesInvoice.class);

    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;

    @Scheduled(cron = "30 13 20 * * ?")
    public void executeAndSaveJournalEntries() {

        Date startDate = Date.valueOf("2022-04-01");
        Date endDate = Date.valueOf("2023-03-31");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!startDate.after(endDate)) {
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based
            int year = calendar.get(Calendar.YEAR);
            logger.info("Scheduled task executing..." + startDate + ":" + endDate + month + "-" + year);


            // Fetch sales data for the current month and year
            List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findSalesInvoicesByMonthAndYear(month, year);
            logger.info("Found {} sales invoices for {}_{}", salesInvoiceList.size(), month, year);


            if (!salesInvoiceList.isEmpty()) {
                createJournalEntriesForSale(month, year, salesInvoiceList, "JournalEntries.xlsx");
            }

            // Move to the next month
            calendar.add(Calendar.MONTH, 1);
            startDate = new java.sql.Date(calendar.getTimeInMillis());
        }
    }

    public void createJournalEntriesForSale(int month, int year, List<SalesInvoice> salesInvoiceList, String existingFilePath) {
        String monthYearString = month + "_" + year;

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

            for (SalesInvoice salesInvoice : salesInvoiceList) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(salesInvoice.getSalesDate().toString());
                row1.createCell(1).setCellValue("sold goods to " + salesInvoice.getCustomerName() + " for rs " + salesInvoice.getAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(salesInvoice.getAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue(salesInvoice.getSalesDate().toString());
                row2.createCell(1).setCellValue("sold goods to " + salesInvoice.getCustomerName() + " for rs " + salesInvoice.getAmount());
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(salesInvoice.getAmount());
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
}

            // Create a PDF
//            String pdfFileName = "journal_entries_" + monthYearString + ".pdf";
//            try {
//                Document document = new Document();
//                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
//                document.open();
//
//                PdfPTable table = new PdfPTable(6);
//                Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
//                        .forEach(columnTitle -> {
//                            PdfPCell header = new PdfPCell();
//                            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                            header.setBorderWidth(2);
//                            header.setPhrase(new Phrase(columnTitle));
//                            table.addCell(header);
//                        });
//
//                for (SalesInvoice salesInvoice : salesInvoiceList) {
//                    table.addCell(salesInvoice.getSalesDate().toString());
//                    table.addCell("sold goods to " + salesInvoice.getCustomerName() + " for rs " + salesInvoice.getAmount());
//                    table.addCell("cash a/c");
//                    table.addCell("d");
//                    table.addCell(salesInvoice.getAmount().toString());
//                    table.addCell("real account");
//
//                    table.addCell(salesInvoice.getSalesDate().toString());
//                    table.addCell("sold goods to " + salesInvoice.getCustomerName() + " for rs " + salesInvoice.getAmount());
//                    table.addCell("to sales a/c");
//                    table.addCell("c");
//                    table.addCell(salesInvoice.getAmount().toString());
//                    table.addCell("nominal account");
//                }
//
//                document.close();
//                logger.trace("PDF file created: " + pdfFileName);
