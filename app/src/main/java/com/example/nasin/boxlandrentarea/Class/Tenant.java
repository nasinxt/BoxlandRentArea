package com.example.nasin.boxlandrentarea.Class;

public class Tenant {

    private String idcardnum;
    private String fname;
    private String lname;
    private String gender;
    private String phoneNum;
    private String email;
    private String Address;
    private String storename;
    private String storeDetail;
    private String username;
    private String password;

    public String getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(String storeDetail) {
        this.storeDetail = storeDetail;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {

        return fname;
    }

    public String getIdcardnum() {
        return idcardnum;
    }

    public String getLname() {
        return lname;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return Address;
    }


    public void setIdcardnum(String idcardnum) {
        this.idcardnum = idcardnum;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
