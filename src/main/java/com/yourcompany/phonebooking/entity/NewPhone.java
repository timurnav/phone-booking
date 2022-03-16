package com.yourcompany.phonebooking.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewPhone {

    @NotNull
    @NotEmpty
    private String brand;
    @NotNull
    @NotEmpty
    private String device;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
