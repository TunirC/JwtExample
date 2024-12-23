package com.security.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
public class HomeController {

    @GetMapping("/user")
    public String user() {
        log.info("Getting user details");
        return "Users";
    }

}
