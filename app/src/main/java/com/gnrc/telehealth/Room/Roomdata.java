package com.gnrc.telehealth.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "task")
public class Roomdata implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "familyhead")
    String familyhead;

    @ColumnInfo(name = "phone")
    String phone;

    @ColumnInfo(name = "house")
    String house;

    @ColumnInfo(name = "address")
    String address;

    @ColumnInfo(name = "city")
    String city;

    @ColumnInfo(name = "dist")
    String dist;

    @ColumnInfo(name = "state")
    String state;

    @ColumnInfo(name = "pin")
    String pin;

    @ColumnInfo(name = "gaon_panchayat")
    String gaon_panchayat;

    @ColumnInfo(name = "block_code")
    String block_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
