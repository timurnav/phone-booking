package com.yourcompany.phonebooking.entity;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "booking_audit")
public class PhoneBookingAudit extends IdentifiedEntity {

    private Long phoneId;
    private String auditOperation;
    private Instant time;

    @PrePersist
    public void prePersist() {
        time = Instant.now();
    }

    public static PhoneBookingAudit booked(long userId, Phone phone) {
        PhoneBookingAudit audit = new PhoneBookingAudit();
        audit.setPhoneId(phone.getId());
        audit.setAuditOperation(String.format("booked by %s", userId));
        return audit;
    }

    public static PhoneBookingAudit returned(long userId, Phone phone) {
        PhoneBookingAudit audit = new PhoneBookingAudit();
        audit.setPhoneId(phone.getId());
        audit.setAuditOperation(String.format("returned by %s", userId));
        return audit;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getAuditOperation() {
        return auditOperation;
    }

    public void setAuditOperation(String auditOperation) {
        this.auditOperation = auditOperation;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
