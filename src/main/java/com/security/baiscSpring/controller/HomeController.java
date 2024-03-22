package com.security.baiscSpring.controller;

import com.security.baiscSpring.model.User;
import com.security.baiscSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService service;

    @GetMapping("/user")
    public List<User> getUser(){
        return this.service.getAllUsers();
    }

}
