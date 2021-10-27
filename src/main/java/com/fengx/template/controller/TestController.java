package com.fengx.template.controller;

import com.fengx.template.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public Response<?> getWelcomeMsg() {
        return Response.success("Welcome");
    }

    @GetMapping("/admin")
    public String getWelcomeAdmin() {
        return "Hello, Spring Security, me is admin";
    }

    @GetMapping("/user")
    public String getWelcomeUser() {
        return "Hello, Spring Security, me is user";
    }
}