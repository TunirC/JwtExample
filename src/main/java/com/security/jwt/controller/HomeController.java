package com.security.jwt.controller;

import com.security.jwt.model.User;
import com.security.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private UserService userService;

    @Autowired
    HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> user() {
        log.info("Getting complete user list");
        return userService.getAllUserList();
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

}
