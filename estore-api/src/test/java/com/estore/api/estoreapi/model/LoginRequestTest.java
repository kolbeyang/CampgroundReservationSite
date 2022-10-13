package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit tests for the LoginRequest class
 * 
 * @author Sherry Robinson
 */
@Tag("Model-tier")
public class LoginRequestTest {
    @Test
    public void testCtor() {
        // Setup
        String expectedUsername = "GoodName";
        String expectedPassword = "GoodPassword";

        // Invoke
        LoginRequest loginRequest = new LoginRequest(expectedUsername, expectedPassword);

        // Analyze
        assertEquals(expectedUsername, loginRequest.getUsername());
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

}
