package com.erp.convert_to_journals.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer expenseId;

    @Column(name = "expense_name")
    String expenseName;

    @Column(name = "expense_type")
    String expenseType;

    @Column(name = "expense_amount")
    Double expenseAmount;

    @Column(name = "expense_date")
    Date expenseDate;

    @Column(name = "expense_invoice_number")
    String expenseInvoiceNumber;

    @Column(name = "transaction_type_id")
    Integer transactionTypeId;

    public Expense(){}

    public Expense(Integer expenseId, String expenseName, String expenseType, Double expenseAmount, Date expenseDate, String expenseInvoiceNumber, Integer transactionTypeId) {
        this.expenseId = expenseId;
        this.expenseName = expenseName;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseInvoiceNumber = expenseInvoiceNumber;
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseInvoiceNumber() {
        return expenseInvoiceNumber;
    }

    public void setExpenseInvoiceNumber(String expenseInvoiceNumber) {
        this.expenseInvoiceNumber = expenseInvoiceNumber;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}
