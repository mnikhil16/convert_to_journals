package com.erp.convert_to_journals.repository;

import com.erp.convert_to_journals.entity.Payable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayableRepository extends JpaRepository<Payable, Integer> {
    @Query(value = "SELECT * FROM payable WHERE YEAR(payable_date) = :year AND MONTH(payable_date) = :month", nativeQuery = true)
    List<Payable> findPayableByMonthAndYear(int month, int year);
}
