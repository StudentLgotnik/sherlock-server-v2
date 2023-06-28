package com.sherlock.identity.controller;

import com.sherlock.identity.dto.LoginDetailsDto;
import com.sherlock.identity.dto.SignUpDetailsDto;
import com.sherlock.identity.dto.UserDetailsDto;
import com.sherlock.identity.persistance.entity.User;
import com.sherlock.identity.security.jwt.JwtService;
import com.sherlock.identity.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final UserService userService;
    private final JwtService jwtService;

    public AccountController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<UserDetailsDto> signUp(@Valid @RequestBody SignUpDetailsDto signUpDetailsDto, HttpServletResponse response) {
        User signedUp =  userService.signUp(signUpDetailsDto.toUser());
        String token = jwtService.generateToken(signedUp);
        decorateResponseWithToken(response, token);
        return tokenToResponse(UserDetailsDto.fromUser(signedUp), token);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDetailsDto> login(@Valid @RequestBody LoginDetailsDto loginDetailsDto, HttpServletResponse response) {
        User loggedIn = userService.login(loginDetailsDto.toUser());
        String token = jwtService.generateToken(loggedIn);
        decorateResponseWithToken(response, token);
        return tokenToResponse(UserDetailsDto.fromUser(loggedIn), token);
    }

    private void decorateResponseWithToken(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);
    }

    private ResponseEntity<UserDetailsDto> tokenToResponse(UserDetailsDto userDetailsDto, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body(userDetailsDto);
    }
}
