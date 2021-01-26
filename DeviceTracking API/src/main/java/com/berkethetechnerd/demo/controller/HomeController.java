package com.berkethetechnerd.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/")
public class HomeController {

    @GetMapping
    public @ResponseBody String home() {
        // Welcome message
        return "Welcome to the TwoImpulse REST demo system!";
    }
}
