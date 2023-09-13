package com.erp.convert_to_journals.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "sales_invoice")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer salesInvoiceId;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "sale_amount")
    Double saleAmount;

//    @Temporal(TemporalType.DATE)
    @Column(name = "sales_date")
    Date salesDate;

    @Column(name = "sales_invoice_number")
    String salesInvoiceNumber;

    @Column(name = "transaction_type_id")
    Integer transactionTypeId;

    public Sale(){}

    public Sale(Integer salesInvoiceId, String customerName, Double saleAmount, Date salesDate, String salesInvoiceNumber, Integer transactionTypeId) {
        this.salesInvoiceId = salesInvoiceId;
        this.customerName = customerName;
        this.saleAmount = saleAmount;
        this.salesDate = salesDate;
        this.salesInvoiceNumber = salesInvoiceNumber;
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getSalesInvoiceId() {
        return salesInvoiceId;
    }

    public void setSalesInvoiceId(Integer salesInvoiceId) {
        this.salesInvoiceId = salesInvoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getSalesInvoiceNumber() {
        return salesInvoiceNumber;
    }

    public void setSalesInvoiceNumber(String salesInvoiceNumber) {
        this.salesInvoiceNumber = salesInvoiceNumber;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}