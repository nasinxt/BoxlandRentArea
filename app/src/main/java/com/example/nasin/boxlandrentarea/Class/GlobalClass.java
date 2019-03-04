package com.example.nasin.boxlandrentarea.Class;


import android.app.Application;

import java.util.ArrayList;

public class GlobalClass extends Application {
    String TenantId;
    ArrayList<AreaDetail> areadetail = new ArrayList<AreaDetail>();
    AreaDetail areaDetailClass = new AreaDetail();
    ArrayList<Tenant> tenantArrayList = new ArrayList<Tenant>();
    Tenant tenant = new Tenant();
    ArrayList<AreaZone> areaZone = new ArrayList<AreaZone>();
    String areaID;
    String zoneID;
    String bookingID;
    ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    ArrayList<String> bookingList = new ArrayList<String>();
    ArrayList<Booking> BookingArray = new ArrayList<Booking>();
    ArrayList<String> storeTypeList = new ArrayList<String>();

    public Receipt getReceiptClass() {
        return receiptClass;
    }

    public void setReceiptClass(Receipt receiptClass) {
        this.receiptClass = receiptClass;
    }

    Receipt receiptClass = new Receipt();



    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public GlobalClass() {
    }


    public ArrayList<Tenant> getTenantArray() {
        return tenantArrayList;
    }
    public Tenant getTenant() {
        return tenant;
    }



    public ArrayList<AreaDetail> getAreadetail() {
        return areadetail;
    }



    public AreaDetail getAreaDetailClass() {
        return areaDetailClass;
    }

    public void setAreaDetailClass(AreaDetail areaDetailClass) {
        this.areaDetailClass = areaDetailClass;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public ArrayList<String> getBookingList() {
        return bookingList;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }


    public ArrayList<AreaZone> getAreaZone() {
        return areaZone;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public ArrayList<String> getStoreTypeList() {
        return storeTypeList;
    }

    public ArrayList<Booking> getBookingArray() {
        return BookingArray;
    }

    public void setBookingArray(ArrayList<Booking> bookingArray) {
        BookingArray = bookingArray;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }
}
