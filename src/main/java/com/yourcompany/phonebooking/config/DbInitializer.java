package com.yourcompany.phonebooking.config;

import com.yourcompany.phonebooking.entity.Phone;
import com.yourcompany.phonebooking.entity.User;
import com.yourcompany.phonebooking.entity.UserRole;
import com.yourcompany.phonebooking.repo.PhoneRepository;
import com.yourcompany.phonebooking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DbInitializer implements ApplicationRunner {

    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final boolean populateDb;

    public DbInitializer(PhoneRepository phoneRepository,
                         UserRepository userRepository,
                         PasswordEncoder encoder,
                         @Value("${db.populate:false}") boolean populateDb) {
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.populateDb = populateDb;
    }

    @Override
    public void run(ApplicationArguments args) {
        String email = "admin@admin.com";
        if (!populateDb || userRepository.findByEmail(email).isPresent()) {
            return;
        }
        User admin = new User();
        admin.setRoles(Set.of(UserRole.ADMIN, UserRole.USER));
        admin.setEmail(email);
        admin.setPassword(encoder.encode("password"));
        userRepository.save(admin);

        createPhone("Samsung", "Galaxy S9");
        createPhone("Samsung", "Galaxy S8");
        createPhone("Samsung", "Galaxy S8");
        createPhone("Motorola", "Nexus 6");
        createPhone("Oneplus", "9");
        createPhone("Apple", "iPhone 13");
        createPhone("Apple", "iPhone 12");
        createPhone("Apple", "iPhone 11");
        createPhone("Apple", "iPhone X");
        createPhone("Nokia", "3310");
    }

    private void createPhone(String brand, String device) {
        Phone phone = new Phone();
        phone.setBrand(brand);
        phone.setDevice(device);
        phoneRepository.save(phone);
    }
}
