package com.berkethetechnerd.devicetrackerapi.controller;

import com.berkethetechnerd.devicetrackerapi.entity.SensorData;
import com.berkethetechnerd.devicetrackerapi.service.DeviceTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/data")
public class SensorController {

    // Connected service to make queries
    private final DeviceTrackerService deviceTrackerService;

    @Autowired
    public SensorController(DeviceTrackerService deviceTrackerService) {
        this.deviceTrackerService = deviceTrackerService;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<SensorData> getAllSensorData() {
        return deviceTrackerService.getAllSensorData();
    }

    @PostMapping
    public @ResponseBody SensorData addNewSensorData(@RequestBody SensorData newDataInstance) {
        return deviceTrackerService.addNewData(newDataInstance);
    }

}
