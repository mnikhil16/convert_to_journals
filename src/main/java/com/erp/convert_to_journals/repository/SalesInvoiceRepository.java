package com.erp.convert_to_journals.repository;


import com.erp.convert_to_journals.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Month;
import java.time.Year;
import java.util.List;



@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Integer> {
    @Query(value = "SELECT * FROM sales_invoice WHERE YEAR(sales_date) = :year AND MONTH(sales_date) = :month", nativeQuery = true)
    List<SalesInvoice> findSalesInvoicesByMonthAndYear(int month, int year);

    @Query(value = "SELECT * FROM sales_invoice WHERE sales_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<SalesInvoice> findSalesInvoicesByStartDateAndEndDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}





