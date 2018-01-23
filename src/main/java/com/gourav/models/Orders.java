package com.gourav.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Entity
@Table(name = "ORDERS")
public class Orders {

    // I have included User detail in same table which can be separated out in User table

    @Id
    @Column(name = "INVOICE_ID", unique = true)
    private String invoiceId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "EMAIL")
    private String emailAddress;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @UpdateTimestamp
    private Date updatedDate;

    @Column(name = "INVOICE_PATH")
    private String invoiceLocation;

    @Column(name = "EMAIL_SENT")
    private boolean isEmailSent;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getInvoiceLocation() {
        return invoiceLocation;
    }

    public void setInvoiceLocation(String invoiceLocation) {
        this.invoiceLocation = invoiceLocation;
    }

    public boolean isEmailSent() {
        return isEmailSent;
    }

    public void setEmailSent(boolean emailSent) {
        isEmailSent = emailSent;
    }
}
