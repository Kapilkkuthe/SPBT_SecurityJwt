package com.security.baiscSpring.controller;

import com.security.baiscSpring.model.RefreshToken;
import com.security.baiscSpring.model.User;
import com.security.baiscSpring.security.JwtHelper;
import com.security.baiscSpring.service.RefreshTokenService;
import com.security.baiscSpring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserService service;

    @Autowired
    private RefreshTokenService refreshTokenService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private void doAuthenticate(String Username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(Username, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody HashMap<String,Object> requestMap){
        HashMap<String,Object> responseMap= new HashMap<>();
        this.doAuthenticate(requestMap.get("username").toString(), requestMap.get("password").toString());

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestMap.get("username").toString());
        String token = this.helper.generateToken(userDetails);
        RefreshToken refreshToken= refreshTokenService.createRefreshToken(userDetails.getUsername());
        responseMap.put("token",token);
        responseMap.put("refreshToken",refreshToken);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refreshToken(@RequestBody HashMap<String,Object> requestMap){
        HashMap<String,Object> responseMap= new HashMap<>();
        RefreshToken refreshToken= refreshTokenService.verifyRefreshToken(requestMap.get("refreshToken").toString());

       User user=refreshToken.getUser();
       String token=this.helper.generateToken(user);
        responseMap.put("token",token);
        responseMap.put("refreshToken",refreshToken);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
