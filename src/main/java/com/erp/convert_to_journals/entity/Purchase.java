package com.erp.convert_to_journals.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer purchaseId;

    @Column(name = "supplier_name")
    String supplierName;

    @Column(name = "purchase_amount")
    Double purchaseAmount;

    @Column(name = "purchase_date")
    Date purchaseDate;

    @Column(name = "purchase_invoice_number")
    String purchaseInvoiceNumber;

    @Column(name = "transaction_type_id")
    Integer transactionTypeId;

    public Purchase(){}

    public Purchase(Integer purchaseId, String supplierName, Double purchaseAmount, Date purchaseDate, String purchaseInvoiceNumber, Integer transactionTypeId) {
        this.purchaseId = purchaseId;
        this.supplierName = supplierName;
        this.purchaseAmount = purchaseAmount;
        this.purchaseDate = purchaseDate;
        this.purchaseInvoiceNumber = purchaseInvoiceNumber;
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseInvoiceNumber() {
        return purchaseInvoiceNumber;
    }

    public void setPurchaseInvoiceNumber(String purchaseInvoiceNumber) {
        this.purchaseInvoiceNumber = purchaseInvoiceNumber;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}
