package com.security.baiscSpring.service;

import com.security.baiscSpring.model.RefreshToken;
import com.security.baiscSpring.model.User;
import com.security.baiscSpring.repositories.RefreshTokenRepository;
import com.security.baiscSpring.repositories.UserRepository;
import com.security.baiscSpring.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Service
public class RefreshTokenService {


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper helper;

    public RefreshToken createRefreshToken(String username){
//        RefreshToken refreshToken=   RefreshToken.builder()
//                .refreshToken(UUID.randomUUID().toString())
//                .expiry(Instant.now().plusMillis(12*60*60*1000))
//                .user(userRepository.findByUsername(username).get())
//                .build();

        String generatedRefreshToken = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plusMillis(12 * 60 * 60 * 1000);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken=user.getRefreshToken();

        if (refreshToken==null){
            refreshToken = new RefreshToken(generatedRefreshToken, expiryTime, user);
        }else{
         refreshToken.setExpiry(expiryTime);
        }

        user.setRefreshToken(refreshToken);
        // save to database
        refreshTokenRepository.save(refreshToken);

        return  refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){

        RefreshToken refreshTokenObj=refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Given token does not exist in DB!"));

        if (refreshTokenObj.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshTokenObj);
            throw new RuntimeException("Refresh token is Expried!!");
        }
        return refreshTokenObj;
    }

    // this will be done when we change the contorller and service logics
//    public HashMap<String,Object> refreshJwtToken(HashMap<String,Object> requestMap){
//
//       RefreshToken refreshToken= verifyRefreshToken(requestMap.get("refreshToken").toString());
//
//       User user=refreshToken.getUser();
//       String token=this.helper.generateToken(user);
//        return null;
//    }
}
