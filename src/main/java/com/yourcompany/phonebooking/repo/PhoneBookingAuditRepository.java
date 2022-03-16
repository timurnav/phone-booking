package com.yourcompany.phonebooking.repo;

import com.yourcompany.phonebooking.entity.PhoneBookingAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneBookingAuditRepository extends JpaRepository<PhoneBookingAudit, Long> {
}
