package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit tests for the Reservation class
 * 
 * @author Sherry Robinson
 */
@Tag("Model-tier")
public class ReservationTest {
    @Test
    public void testCtor() {
        // Setup
        int expectedID = 1234;
        int expectedCampsiteID = 99;
        long expectedStartDate = 1666666666;
        long expectedEndDate = 1777777777;
        String expectedUsername = "GoodUsername";
    
        // Invoke
        Reservation reservation = new Reservation(expectedID, expectedCampsiteID, expectedStartDate, expectedEndDate, expectedUsername);
  
        // Analyze
        assertEquals(expectedID, reservation.getId());
        assertEquals(expectedCampsiteID, reservation.getCampsiteId());
        assertEquals(expectedStartDate, reservation.getStartDate());
        assertEquals(expectedEndDate, reservation.getEndDate());
        assertEquals(expectedUsername, reservation.getUsername());
    }

    @Test
    public void testDate() {
        // Setup
        int id = 1234;
        int campsiteID = 99;
        long startDate = 1666666666;
        long endDate = 1777777777;
        String username = "GoodUsername";

        Reservation reservation = new Reservation(id, campsiteID, startDate, endDate, username);

        int expectedStartDate = 1666677777;
        int expectedEndDate = 1777766666;

        // Invoke
        reservation.setDate(expectedStartDate, expectedEndDate);

        // Analyze
        assertEquals(expectedStartDate, reservation.getStartDate());
        assertEquals(expectedEndDate, reservation.getEndDate());
    }

    @Test
    public void testToString() {
         // Setup
         int id = 1234;
         int campsiteID = 99;
         long startDate = 1666666666;
         long endDate = 1777777777;
         String username = "GoodUsername";
 
         Reservation reservation = new Reservation(id, campsiteID, startDate, endDate, username);

         String expectedString = String.format(Reservation.STRING_FORMAT,id,campsiteID,startDate,endDate,username);

         // Invoke
         String acutalString = reservation.toString();

         // Analyze
         assertEquals(expectedString, acutalString);
    }
}
