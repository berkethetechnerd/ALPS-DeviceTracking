package com.berkethetechnerd.demo.controller;

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
        String welcomeText = "Welcome to the TwoImpulse REST demo system!";

        // Execute
        String resultText = homeController.home();

        // Check
        assertEquals(welcomeText, resultText);
    }
}
