package com.erp.convert_to_journals.repository;

import com.erp.convert_to_journals.entity.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Integer> {
    @Query(value = "SELECT * FROM receivable WHERE YEAR(receivable_date) = :year AND MONTH(receivable_date) = :month", nativeQuery = true)
    List<Receivable> findReceivablesByMonthAndYear(int month, int year);
}