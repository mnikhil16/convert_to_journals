package com.erp.convert_to_journals.repository;


import com.erp.convert_to_journals.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;



@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Integer> {
    @Query(value = "SELECT * FROM sales_invoice WHERE YEAR(sales_date) = :year AND MONTH(sales_date) = :month", nativeQuery = true)
    List<SalesInvoice> findSalesInvoicesByMonthAndYear(int month, int year);
}





