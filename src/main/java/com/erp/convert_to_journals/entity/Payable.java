package com.erp.convert_to_journals.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "payable")
public class Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer payableId;

    @Column(name = "supplier_name")
    String supplierName;

    @Column(name = "payable_amount")
    Double payableAmount;

    @Column(name = "payable_date")
    Date payableDate;

    @Column(name = "payable_invoice_number")
    String payableInvoiceNumber;

    @Column(name = "transaction_type_id")
    Integer transactionTypeId;

    public Payable(){}

    public Payable(Integer payableId, String supplierName, Double payableAmount, Date payableDate, String payableInvoiceNumber, Integer transactionTypeId) {
        this.payableId = payableId;
        this.supplierName = supplierName;
        this.payableAmount = payableAmount;
        this.payableDate = payableDate;
        this.payableInvoiceNumber = payableInvoiceNumber;
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getPayableId() {
        return payableId;
    }

    public void setPayableId(Integer payableId) {
        this.payableId = payableId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Date getPayableDate() {
        return payableDate;
    }

    public void setPayableDate(Date payableDate) {
        this.payableDate = payableDate;
    }

    public String getPayableInvoiceNumber() {
        return payableInvoiceNumber;
    }

    public void setPayableInvoiceNumber(String payableInvoiceNumber) {
        this.payableInvoiceNumber = payableInvoiceNumber;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}
