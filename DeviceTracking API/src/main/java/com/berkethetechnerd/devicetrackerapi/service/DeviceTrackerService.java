package com.berkethetechnerd.devicetrackerapi.service;

import com.berkethetechnerd.devicetrackerapi.entity.SensorData;
import com.berkethetechnerd.devicetrackerapi.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceTrackerService {

    // Sensor data storage connection
    private final SensorRepository sensorRepository;

    @Autowired
    public DeviceTrackerService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Queries for all sensor data registered in the DB
     * @return List of all sensor data
     */
    public List<SensorData> getAllSensorData() {
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorRepository.findAll().forEach(sensorDataList::add); // Turn Iterator into a List
        return sensorDataList;
    }

    /**
     * Adds a new sensor data instance into DB
     * @param newDataInstance: SensorData instance to register into DB
     * @return The registered new SensorData instance
     */
    public SensorData addNewData(SensorData newDataInstance) {
        // Create date value
        Date createTime = new Date();
        newDataInstance.setCreate_time(createTime);

        // Save and return sensor data instance
        return sensorRepository.save(newDataInstance);
    }
}
