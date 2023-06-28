package com.sherlock.identity.controller;

import com.sherlock.identity.dto.UserDetailsDto;
import com.sherlock.identity.security.jwt.JwtService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {

    private final JwtService jwtService;

    public TokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping(path = "/validate")
    public ResponseEntity<UserDetailsDto> validate(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(UserDetailsDto.fromUser(jwtService.validate(jwtToken)));
    }
}
