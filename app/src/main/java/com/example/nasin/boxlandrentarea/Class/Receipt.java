package com.example.nasin.boxlandrentarea.Class;

import java.util.Date;

public class Receipt {
    private String receiptId;
    private Date paymentDate;
    private String picPayment;
    private String paymentStatus;
    private Booking booking;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPicPayment() {
        return picPayment;
    }

    public void setPicPayment(String picPayment) {
        this.picPayment = picPayment;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
