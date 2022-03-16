package com.yourcompany.phonebooking.service;

import com.yourcompany.phonebooking.entity.NewUser;
import com.yourcompany.phonebooking.entity.User;
import com.yourcompany.phonebooking.entity.UserRole;
import com.yourcompany.phonebooking.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email " + username));
    }

    public User create(NewUser newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        String encoded = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(encoded);
        user.setRoles(Set.of(UserRole.USER));
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
