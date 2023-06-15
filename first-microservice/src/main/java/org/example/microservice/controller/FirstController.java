package org.example.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/message")
    public String test(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }
}
