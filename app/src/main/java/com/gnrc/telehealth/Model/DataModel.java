package com.gnrc.telehealth.Model;

import java.io.Serializable;

public class DataModel implements Serializable {

    private String id, familyhead, phone, house, address,city,dist,state,pin, gaon_panchayat,block_code,viewtext,edittext;
    private int currentcount;

    public String getId() {
        return id;
    }

    public String getEdittext() {
        return edittext;
    }

    public void setEdittext(String edittext) {
        this.edittext = edittext;
    }

    public String getViewtext() {
        return viewtext;
    }

    public void setViewtext(String viewtext) {
        this.viewtext = viewtext;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyhead() {
        return familyhead;
    }

    public void setFamilyhead(String familyhead) {
        this.familyhead = familyhead;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public String getGaon_panchayat() {
        return gaon_panchayat;
    }

    public void setGaon_panchayat(String gaon_panchayat) {
        this.gaon_panchayat = gaon_panchayat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public int getCurrentcount() {
        return currentcount;
    }

    public void setCurrentcount(int currentcount) {
        this.currentcount = currentcount;
    }
}
