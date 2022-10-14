package com.estore.api.estoreapi.persistence;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;


import com.fasterxml.jackson.databind.ObjectMapper;


import com.estore.api.estoreapi.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Tests the Reservation File DAO class
 */
@Tag("persistence")
public class ReservationFileDAOTest {
    final static long EPOCH = System.currentTimeMillis();  //Reservation time is stored in ms since 1970
    final static long DAY = 86400000;                      //ms in a day

    ReservationFileDAO reservationFileDAO;
    ObjectMapper mockObjectMapper;
    Reservation[] testReservations;
    Campsite[] testCampsites;                              //Since reservations are based on campsite, it is best to include a mock testCampsites


    /**
     * Before each test, the reservation file DAO object with a mock object mapper needs to be set
     * up for simulation
     * @throws IOException
     */
    @BeforeEach
    public void setupreservationFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        Campsite camp1 = new Campsite(1, "Lucky Meadows", 11.99);
        Campsite camp2 = new Campsite(2, "Eagle heights", 12.43);
        Campsite camp3 = new Campsite(3, "Stony Brooks", 14);
        Reservation reserve1 = new Reservation(0, camp1.getId(), EPOCH + DAY,  EPOCH + (DAY * 2)); //A reservation that is from a Day in advance from 
        Reservation reserve2 = new Reservation(1, camp2.getId(),EPOCH + DAY, EPOCH + (DAY*2) );
        Reservation reserve3 = new Reservation(2, camp1.getId(), EPOCH + (DAY * 3), EPOCH + (DAY * 5));

        testCampsites = new Campsite[3];
        testReservations = new Reservation[3];
        testCampsites[0] = camp1;
        testCampsites[1] = camp2;
        testCampsites[2] = camp3;
        testReservations[0] = reserve1;
        testReservations[1] = reserve2;
        testReservations[2] = reserve3;
    
        when(mockObjectMapper
            .readValue(new File("Whatever.txt"), Reservation[].class))
                .thenReturn(testReservations);

        reservationFileDAO = new ReservationFileDAO("Whatever.txt", mockObjectMapper);
        
    }


    /**
     * Tests that the recieved reservation list from the file DAO object is the same length
     * and contains the same elements as the testList.
     */
    @Test
    public void testgetReservations(){
        Reservation[] actual = reservationFileDAO.getReservations();
        assertEquals(actual.length,testReservations.length);
        for (int i = 0; i < testReservations.length;++i)
            assertEquals(actual[i],testReservations[i]);

    }

    /**
     * Tests that the specific reservation searched for matches the correct reservation 
     * that was originally searched for by id number
     * 
     */
    @Test
    public void testfindReservation(){
        Reservation actual1 = reservationFileDAO.getReservation(testReservations[0].getId());
        Reservation actual2 = reservationFileDAO.getReservation(testReservations[1].getId());
        assertEquals(testReservations[0], actual1 );
        assertEquals(testReservations[1], actual2);
        
    }

    /**
     * Tests that the new Reservation created in the fileDAO exists 
     */
    @Test
    public void testcreateReservation(){
        Reservation newReservation = new Reservation(3, testCampsites[0].getId(), EPOCH + (DAY* 7), EPOCH + (DAY* 8));
        Reservation result = assertDoesNotThrow(() -> reservationFileDAO.createReservation(newReservation), "An unexpected exception occurred");
        assertEquals(newReservation, result);

    }


    /**
    * Tests that the updates to the specific reservation exist in the fileDAO 
    */
    @Test
    public void testupdateReservation(){
        Reservation updatedReservation = testReservations[2];
        updatedReservation.setDate(EPOCH + DAY * 3, EPOCH + DAY * 4);
        Reservation result = assertDoesNotThrow(() -> reservationFileDAO.updateReservation(updatedReservation), "An unexpected exception occurred");
        assertEquals(updatedReservation, result);
        
    }

    /**
     * Tests that an deleted reservation does not exist in the fileDAO and the 
     * Reservations list in the fileDAO is one shorter than the testReservations.
     */
    @Test
    public void testdeleteReservation(){
        boolean result = assertDoesNotThrow(() -> reservationFileDAO.deleteReservation(testReservations[0].getId()), "An unexpected exception occurred");
        assertEquals(result, true);
        assertEquals(reservationFileDAO.getReservations().length,testReservations.length-1);
    }


    /**
    * Tests the case where an non existent reservation is deleted 
    */
    @Test
    public void testdeleteReservationNotFound(){
        Reservation fakeReserve = new Reservation(999999, 43, EPOCH, EPOCH + DAY);
        boolean result =  assertDoesNotThrow(() -> reservationFileDAO.deleteReservation(fakeReserve.getId()), "An unexpected exception occurred");
        assertFalse(result);
    }

    /**
     * Tests the case where an non existent reservation is updated
     */
    @Test
    public void testupdateReservationNotFound(){
        Reservation fakeReserve = new Reservation(999999, 43, EPOCH, EPOCH + DAY);
        Reservation result =  assertDoesNotThrow(() -> reservationFileDAO.updateReservation(fakeReserve), "An unexpected exception occurred");
        assertNull(result);
    }

    /**
     * Tests the case where a non-existent reservation or keyword that is searched for. Should return null
     */
    @Test
    public void testfindReservationNotFound(){
        Reservation result = assertDoesNotThrow(() -> reservationFileDAO.getReservation(99999), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testSaveFailure() throws IOException{
        doThrow(new IOException())
        .when(mockObjectMapper)
            .writeValue(any(File.class), any(Reservation[].class));

    Reservation newReservation = new Reservation(3, testCampsites[0].getId(), EPOCH + (DAY* 7), EPOCH + (DAY* 8));

    assertThrows(IOException.class, () -> reservationFileDAO.createReservation(newReservation));
    }

    @Test
    public void testLoadFailure() throws IOException{

        doThrow(new IOException())
        .when(mockObjectMapper)
            .readValue(new File("Whatever.txt"), Reservation[].class);

        assertThrows(IOException.class, 
                   () -> new ReservationFileDAO("Whatever.txt", mockObjectMapper), 
                  " Did not throw proper exception");
        

    }
    
}
