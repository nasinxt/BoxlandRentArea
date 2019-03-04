package com.example.nasin.boxlandrentarea.Class;

import java.util.Date;

public class Booking {
    private String bookingId;
    private Date bookingDateTime;
    private String bookingStatus;
    private Date payDepositDate;
    private Tenant tenant;
    private AreaDetail areadetail = new AreaDetail();


    public Booking(String bookingId, Date bookingDateTime, String bookingStatus, Tenant tenant, AreaDetail areadetail) {
        this.bookingId = bookingId;
        this.bookingDateTime = bookingDateTime;
        this.bookingStatus = bookingStatus;
        this.tenant = tenant;
        this.areadetail = areadetail;
    }
    public Booking() {

    }

    public Date getPayDepositDate() {
        return payDepositDate;
    }

    public void setPayDepositDate(Date payDepositDate) {
        this.payDepositDate = payDepositDate;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setBookingDateTime(Date bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setAreadetail(AreaDetail areadetail) {
        this.areadetail = areadetail;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Date getBookingDateTime() {
        return bookingDateTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public AreaDetail getAreadetail() {
        return areadetail;
    }
}
