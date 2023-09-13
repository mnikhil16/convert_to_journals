package com.erp.convert_to_journals.repository;


import com.erp.convert_to_journals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;



@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query(value = "SELECT * FROM sales_invoice WHERE YEAR(sales_date) = :year AND MONTH(sales_date) = :month", nativeQuery = true)
    List<Sale> findSalesInvoicesByMonthAndYear(int month, int year);

    @Query(value = "SELECT * FROM sales_invoice WHERE sales_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Sale> findSalesInvoicesByStartDateAndEndDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}





