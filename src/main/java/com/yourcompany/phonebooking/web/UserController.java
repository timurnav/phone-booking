package com.yourcompany.phonebooking.web;

import com.yourcompany.phonebooking.entity.NewUser;
import com.yourcompany.phonebooking.entity.User;
import com.yourcompany.phonebooking.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid NewUser newUser) {
        return userService.create(newUser);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }
}
