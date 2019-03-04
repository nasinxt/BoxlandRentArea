package com.example.nasin.boxlandrentarea.Class;

import java.util.ArrayList;

public class AreaDetail {
    private String areaId;
    private ArrayList<String> areaPic = new ArrayList<String>();
    private String detail;
    private String size;
    private double price;
    private double deposit;
    private String status;
    private AreaZone areazone;

    public AreaDetail(String areaId, ArrayList<String> areaPic, String detail, String size, double price, double deposit, String status, AreaZone areazone) {
        this.areaId = areaId;
        this.areaPic = areaPic;
        this.detail = detail;
        this.size = size;
        this.price = price;
        this.deposit = deposit;
        this.status = status;
        this.areazone = areazone;
    }

    public AreaDetail() {

    }


    public String getAreaId() {
        return areaId;
    }

    public ArrayList<String> getAreaPic() {
        return areaPic;
    }

    public String getDetail() {
        return detail;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public double getDeposit() {
        return deposit;
    }

    public String getStatus() {
        return status;
    }

    public AreaZone getAreazone() {
        return areazone;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setAreaPic(ArrayList<String> areaPic) {
        this.areaPic = areaPic;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAreazone(AreaZone areazone) {
        this.areazone = areazone;
    }

}
