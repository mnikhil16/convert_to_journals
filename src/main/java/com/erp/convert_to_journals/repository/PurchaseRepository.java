package com.erp.convert_to_journals.repository;

import com.erp.convert_to_journals.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    @Query(value = "SELECT * FROM purchase WHERE YEAR(purchase_date) = :year AND MONTH(purchase_date) = :month", nativeQuery = true)
    List<Purchase> findPurchaseByMonthAndYear(int month, int year);
}
