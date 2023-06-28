package org.example.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/message")
    public String test(@RequestHeader("x-auth-id") String authorizationHeader) {
        if (authorizationHeader != null) {
            return "Authorized! " + authorizationHeader;
        }
        else {
            return "Not authorized!";
        }
    }
}
