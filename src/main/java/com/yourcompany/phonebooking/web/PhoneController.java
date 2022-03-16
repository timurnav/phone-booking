package com.yourcompany.phonebooking.web;

import com.yourcompany.phonebooking.entity.NewPhone;
import com.yourcompany.phonebooking.entity.Phone;
import com.yourcompany.phonebooking.entity.PhoneDetails;
import com.yourcompany.phonebooking.entity.User;
import com.yourcompany.phonebooking.service.PhoneDetailsService;
import com.yourcompany.phonebooking.service.PhoneService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("phones")
public class PhoneController {

    private final PhoneService phoneService;
    private final PhoneDetailsService phoneDetailsService;

    public PhoneController(PhoneService phoneService, PhoneDetailsService phoneDetailsService) {
        this.phoneService = phoneService;
        this.phoneDetailsService = phoneDetailsService;
    }

    @PostMapping
    public Phone create(@Valid NewPhone newPhone) {
        return phoneService.create(newPhone);
    }

    @PutMapping("/{phoneId}/book")
    public Phone bookPhone(@AuthenticationPrincipal User user, @PathVariable long phoneId) {
        return phoneService.bookPhone(user.getId(), phoneId);
    }

    @PutMapping("/{phoneId}/return")
    public Phone returnPhone(@AuthenticationPrincipal User user, @PathVariable long phoneId) {
        return phoneService.returnPhone(user.getId(), phoneId);
    }

    @GetMapping
    public List<Phone> getAll() {
        return phoneService.getAll();
    }

    @GetMapping("/{phoneId}/details")
    public PhoneDetails getDetails(@PathVariable long phoneId) {
        Phone phone = phoneService.getById(phoneId);
        return phoneDetailsService.getDetails(phone);
    }
}
