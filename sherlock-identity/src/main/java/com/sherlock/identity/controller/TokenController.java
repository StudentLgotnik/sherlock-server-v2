package com.sherlock.identity.controller;

import com.sherlock.identity.dto.UserDetailsDto;
import com.sherlock.identity.security.jwt.JwtService;
import com.sherlock.identity.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {

    private final JwtService jwtService;
    private final UserService userService;

    public TokenController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(jwtService.isValid(jwtToken));
    }

    @PostMapping(path = "/to/user")
    public ResponseEntity<UserDetailsDto> toUser(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(UserDetailsDto.fromUser(userService.getOrCreate(jwtService.toUser(jwtToken))));
    }
}
