package com.shiyang.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/catalogServiceFallBack")
    public String userServiceFallBackMethod() {
        return "CATALOG Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/checkoutServiceFallBack")
    public String departmentServiceFallBackMethod() {
        return "CHECKOUT Service is taking longer than Expected." +
                " Please try again later";
    }
}
