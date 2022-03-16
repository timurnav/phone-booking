package com.yourcompany.phonebooking.repo;

import com.yourcompany.phonebooking.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
