package com.gnrc.telehealth.Model;

public class RoomModel {
    private String familyhead, phone, house, address,city,dist,state,pin, gaon_panchayat,block_code;
    private int id;

    public RoomModel(int id,String familyhead, String phone, String house, String address, String city, String dist, String state, String pin, String gaon_panchayat, String block_code) {
        this.id = id;
        this.familyhead = familyhead;
        this.phone = phone;
        this.house = house;
        this.address = address;
        this.city = city;
        this.dist = dist;
        this.state = state;
        this.pin = pin;
        this.gaon_panchayat = gaon_panchayat;
        this.block_code = block_code;
    }

    public String getFamilyhead() {
        return familyhead;
    }

    public void setFamilyhead(String familyhead) {
        this.familyhead = familyhead;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getGaon_panchayat() {
        return gaon_panchayat;
    }

    public void setGaon_panchayat(String gaon_panchayat) {
        this.gaon_panchayat = gaon_panchayat;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
