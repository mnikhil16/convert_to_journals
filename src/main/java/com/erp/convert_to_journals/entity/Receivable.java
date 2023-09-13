package com.erp.convert_to_journals.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "sales_invoice")
public class Receivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer receivableId;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "receivable_amount")
    Double receivableAmount;

    @Column(name = "receivable_date")
    Date receivableDate;

    @Column(name = "transaction_type_id")
    Integer transactionTypeId;

    public Receivable(){}

    public Receivable(Integer receivableId, String customerName, Double receivableAmount, Date receivableDate, Integer transactionTypeId) {
        this.receivableId = receivableId;
        this.customerName = customerName;
        this.receivableAmount = receivableAmount;
        this.receivableDate = receivableDate;
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getReceivableId() {
        return receivableId;
    }

    public void setReceivableId(Integer receivableId) {
        this.receivableId = receivableId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Double receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public Date getReceivableDate() {
        return receivableDate;
    }

    public void setReceivableDate(Date receivableDate) {
        this.receivableDate = receivableDate;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}
