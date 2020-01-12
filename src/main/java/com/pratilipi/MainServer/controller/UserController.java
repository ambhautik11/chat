package com.pratilipi.MainServer.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.pratilipi.MainServer.Service.UserService;
import com.pratilipi.MainServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {


    private AmazonDynamoDB amazonDynamoDB;
    private UserService userService;

    UserController(@Autowired AmazonDynamoDB amazonDynamoDB,@Autowired UserService userService){
        this.amazonDynamoDB = amazonDynamoDB;
        this.userService = userService;
    }

    @PostMapping("/user")
    @CrossOrigin(origins="*", maxAge=3600)
    private User createUser(@RequestBody User user) {
        userService.signUp(user);
        return user;
    }

    @GetMapping("/user")
    @CrossOrigin(origins="*", maxAge=3600)
    private List<String> getUsers() {
        return userService.getAllUserIds();
    }
}
