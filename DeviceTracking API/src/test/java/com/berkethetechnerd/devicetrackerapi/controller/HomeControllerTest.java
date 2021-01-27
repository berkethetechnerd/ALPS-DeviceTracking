package com.berkethetechnerd.devicetrackerapi.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for homepage (/ROOT)
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    @InjectMocks
    HomeController homeController;

    @Test
    public void it_should_say_hello() {
        // Init
        String welcomeText = """
                Welcome to the DeviceTracker REST API system!\s
                To send new data use /data endpoint with new data instance [POST]\s
                To fetch all data use /data/all endpoint [GET]""";

        // Execute
        String resultText = homeController.home();

        // Check
        assertEquals(welcomeText, resultText);
    }
}
