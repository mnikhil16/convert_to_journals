package com.erp.convert_to_journals.repository;

import com.erp.convert_to_journals.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query(value = "SELECT * FROM expense WHERE YEAR(expense_date) = :year AND MONTH(expense_date) = :month", nativeQuery = true)
    List<Expense> findExpensesByMonthAndYear(int month, int year);
}
