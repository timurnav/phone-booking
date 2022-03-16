package com.yourcompany.phonebooking.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "phones")
public class Phone extends IdentifiedEntity {

    private String brand;
    private String device;
    @ManyToOne
    @JoinColumn(name = "booked_id")
    private User bookedBy;
    private Instant bookedAt;
    @Version
    private int version;

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
}
