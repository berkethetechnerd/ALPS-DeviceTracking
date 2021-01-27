package com.berkethetechnerd.devicetrackerapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/")
public class HomeController {

    @GetMapping
    public @ResponseBody String home() {
        return """
                Welcome to the DeviceTracker REST API system!\s
                To send new data use /data endpoint with new data instance [POST]\s
                To fetch all data use /data/all endpoint [GET]""";
    }
}
