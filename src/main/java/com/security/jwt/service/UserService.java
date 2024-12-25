package com.security.jwt.service;

import com.security.jwt.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private List<User> userList;

    UserService () {
        this.userList = new ArrayList<>();
        userList.add(new User(UUID.randomUUID().toString(), "Abhay Goel", "abhay@gmail.com"));
        userList.add(new User(UUID.randomUUID().toString(), "Neha Sharma", "nehaS@gmail.com"));
        userList.add(new User(UUID.randomUUID().toString(), "Ayush Mishra", "mishraAyush@gmail.com"));
        userList.add(new User(UUID.randomUUID().toString(), "Tunir Chakraborty", "TunirC@gmail.com"));
    }

    public List<User> getAllUserList() {
        return userList;
    }

}
