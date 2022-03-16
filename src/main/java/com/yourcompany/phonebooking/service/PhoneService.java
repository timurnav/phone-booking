package com.yourcompany.phonebooking.service;

import com.yourcompany.phonebooking.entity.NewPhone;
import com.yourcompany.phonebooking.entity.Phone;
import com.yourcompany.phonebooking.entity.PhoneBookingAudit;
import com.yourcompany.phonebooking.entity.User;
import com.yourcompany.phonebooking.repo.PhoneBookingAuditRepository;
import com.yourcompany.phonebooking.repo.PhoneRepository;
import com.yourcompany.phonebooking.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;

@Service
public class PhoneService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final PhoneBookingAuditRepository bookingAuditRepository;

    public PhoneService(UserRepository userRepository,
                        PhoneRepository phoneRepository,
                        PhoneBookingAuditRepository bookingAuditRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.bookingAuditRepository = bookingAuditRepository;
    }

    public Phone create(NewPhone newPhone) {
        Phone phone = new Phone();
        phone.setBrand(newPhone.getBrand());
        phone.setDevice(newPhone.getDevice());
        return phoneRepository.save(phone);
    }

    @Transactional
    public Phone bookPhone(long userId, long phoneId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find user by id " + userId));
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find phone by id " + phoneId));
        phone.setBookedBy(user);
        phone.setBookedAt(Instant.now());
        bookingAuditRepository.save(PhoneBookingAudit.booked(userId, phone));
        return phoneRepository.save(phone);
    }

    @Transactional
    public Phone returnPhone(long userId, long phoneId) {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find phone by id " + phoneId));
        User bookedBy = phone.getBookedBy();
        if (bookedBy == null || bookedBy.getId() != userId) {
            throw new IllegalArgumentException("Illegal booking user");
        }
        phone.setBookedBy(null);
        phone.setBookedAt(null);
        bookingAuditRepository.save(PhoneBookingAudit.returned(userId, phone));
        return phoneRepository.save(phone);
    }

    public List<Phone> getAll() {
        return phoneRepository.findAll();
    }

    public Phone getById(long id) {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find phone by id " + id));
    }
}
