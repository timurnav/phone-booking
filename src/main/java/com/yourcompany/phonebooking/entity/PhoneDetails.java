package com.yourcompany.phonebooking.entity;

import com.yourcompany.phonebooking.service.fonoapi.DeviceEntity;

import java.time.Instant;

public class PhoneDetails {

    private long id;
    private String brand;
    private String device;
    private User bookedBy;
    private Instant bookedAt;

    private String technology;
    private String bands2g;
    private String bands3g;
    private String bands4g;

    public static PhoneDetails of(Phone phone, DeviceEntity deviceEntity) {
        PhoneDetails details = of(phone);
        details.setTechnology(deviceEntity.getTechnology());
        details.setBands2g(deviceEntity.get_2g_bands());
        details.setBands3g(deviceEntity.get_3g_bands());
        details.setBands4g(deviceEntity.get_4g_bands());
        return details;
    }

    public static PhoneDetails of(Phone phone) {
        PhoneDetails details = new PhoneDetails();
        details.setId(phone.getId());
        details.setDevice(phone.getDevice());
        details.setBrand(phone.getBrand());
        details.setBookedBy(phone.getBookedBy());
        details.setBookedAt(phone.getBookedAt());
        return details;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    public Instant getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(Instant bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getBands2g() {
        return bands2g;
    }

    public void setBands2g(String bands2g) {
        this.bands2g = bands2g;
    }

    public String getBands3g() {
        return bands3g;
    }

    public void setBands3g(String bands3g) {
        this.bands3g = bands3g;
    }

    public String getBands4g() {
        return bands4g;
    }

    public void setBands4g(String bands4g) {
        this.bands4g = bands4g;
    }
}
