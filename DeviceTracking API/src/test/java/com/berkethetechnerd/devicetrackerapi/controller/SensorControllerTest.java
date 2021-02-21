package com.berkethetechnerd.devicetrackerapi.controller;

import com.berkethetechnerd.devicetrackerapi.entity.SensorData;
import com.berkethetechnerd.devicetrackerapi.service.DeviceTrackerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests for SensorController API
 */
@RunWith(MockitoJUnitRunner.class)
public class SensorControllerTest {

    @InjectMocks
    SensorController sensorController;

    @Mock
    DeviceTrackerService deviceTrackerService;

    @Test
    public void it_should_return_all_two_sensor_data() {
        // Init
        SensorData testData = new SensorData();
        Integer testId = 123;
        testData.setId(testId);

        SensorData testData2 = new SensorData();
        Integer testId2 = 1234;
        testData2.setId(testId2);

        List<SensorData> testList = Arrays.asList(testData, testData2);

        // Setup
        when(deviceTrackerService.getAllSensorData()).thenReturn(testList);

        // Execute
        List<SensorData> resultList = sensorController.getAllSensorData();

        // Check
        assertEquals(testList, resultList);
        assertEquals(testList.size(), resultList.size());
    }

    @Test
    public void it_should_return_no_data() {
        // Init
        List<SensorData> testList = new ArrayList<>();

        // Setup
        when(deviceTrackerService.getAllSensorData()).thenReturn(testList);

        // Execute
        List<SensorData> resultList = sensorController.getAllSensorData();

        // Check
        assertEquals(testList, resultList);
        assertEquals(testList.size(), resultList.size());
    }

    @Test
    public void it_should_add_new_data_and_return_data() {
        // Init
        Integer testId = 123;
        String testSensorName = "Bluetooth";
        Date testStartTime = new Date();
        Date testEndTime = new Date();
        SensorData testData = new SensorData(testId, "Test-id", testSensorName, testStartTime, testEndTime);

        // Setup
        when(deviceTrackerService.addNewData(testData)).thenReturn(testData);

        // Execute
        SensorData resultSensorData = sensorController.addNewSensorData(testData);

        // Check
        assertEquals(testData, resultSensorData);
        assertEquals(testData.getId(), resultSensorData.getId());
        assertEquals(testData.getSensor_name(), resultSensorData.getSensor_name());
        assertEquals(testData.getStart_time(), resultSensorData.getStart_time());
        assertEquals(testData.getEnd_time(), resultSensorData.getEnd_time());
    }
}