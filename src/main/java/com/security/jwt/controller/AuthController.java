package com.security.jwt.controller;

import com.security.jwt.exceptions.BadCredentialsException;
import com.security.jwt.model.JwtRequest;
import com.security.jwt.model.JwtResponse;
import com.security.jwt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;

    @Autowired
    AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        String requestEmailId = jwtRequest.getEmailId();
        String requestPassword = jwtRequest.getPassword();

        authenticateUser(requestEmailId, requestPassword);

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestEmailId);
        String token = jwtHelper.generateToken(userDetails);

        return new ResponseEntity<>(JwtResponse.builder().username(userDetails.getUsername()).token(token).build(), HttpStatus.OK);
    }

    public void authenticateUser(String emailId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(emailId, password);
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid User Credentials Found! ", e);
        }
    }
}
