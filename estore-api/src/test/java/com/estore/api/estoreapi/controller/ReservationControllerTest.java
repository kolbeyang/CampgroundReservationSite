package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.ReservationDAO;
import com.estore.api.estoreapi.model.Reservation;
import com.estore.api.estoreapi.model.ScheduleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Reservation Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class ReservationControllerTest {
    private ReservationController reservationController;
    private ReservationDAO mockReservationDAO;
    private ScheduleService mockScheduleService;

    /**
     * Before each test, create a new ReservationController object and inject
     * a mock Reservation DAO
     */
    @BeforeEach
    public void setupReservationController() {
        mockReservationDAO = mock(ReservationDAO.class);
        reservationController = new ReservationController(mockReservationDAO);
    }

    @Test
    public void testGetReservation() throws IOException {  // getReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // When the same id is passed in, our mock Reservation DAO will return the Reservation object
        when(mockReservationDAO.getReservation(reservation.getId())).thenReturn(reservation);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.getReservation(reservation.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(reservation,response.getBody());
    }

    @Test
    public void testGetReservationNotFound() throws Exception { // createReservation may throw IOException
        // Setup
        int reservationId = 99;
        // When the same id is passed in, our mock Reservation DAO will return null, simulating
        // no reservation found
        when(mockReservationDAO.getReservation(reservationId)).thenReturn(null);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.getReservation(reservationId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetReservationHandleException() throws Exception { // createReservation may throw IOException
        // Setup
        int reservationId = 99;
        // When getReservation is called on the Mock Reservation DAO, throw an IOException
        doThrow(new IOException()).when(mockReservationDAO).getReservation(reservationId);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.getReservation(reservationId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all ReservationController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateReservation() throws IOException {  // createReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // when createReservation is called, return true simulating successful
        // creation and save
        Reservation[] reservationArray = {};
        when(mockReservationDAO.getReservations()).thenReturn(reservationArray);
        when(mockReservationDAO.createReservation(reservation)).thenReturn(reservation);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(reservation,response.getBody());
    }

    @Test
    public void testCreateReservationFailed() throws IOException {  // createReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // when createReservation is called, return false simulating failed
        // creation and save
        Reservation[] reservationArray = {reservation};
        when(mockReservationDAO.getReservations()).thenReturn(reservationArray);
        when(mockReservationDAO.createReservation(reservation)).thenReturn(null);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateReservationHandleException() throws IOException {  // createReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);

        // When createReservation is called on the Mock Reservation DAO, throw an IOException
        Reservation[] reservationArray = {};
        when(mockReservationDAO.getReservations()).thenReturn(reservationArray);
        doThrow(new IOException()).when(mockReservationDAO).createReservation(reservation);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateReservation() throws IOException { // updateReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // when updateReservation is called, return true simulating successful
        // update and save
        when(mockReservationDAO.updateReservation(reservation)).thenReturn(reservation);
        ResponseEntity<Reservation> response = reservationController.updateReservation(reservation);
        reservation.setDate(500, 600);

        // Invoke
        response = reservationController.updateReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(reservation,response.getBody());
    }

    @Test
    public void testUpdateReservationFailed() throws IOException { // updateReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // when updateReservation is called, return true simulating successful
        // update and save
        when(mockReservationDAO.updateReservation(reservation)).thenReturn(null);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.updateReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateReservationHandleException() throws IOException { // updateReservation may throw IOException
        // Setup
        Reservation reservation = new Reservation(12, 12, 100, 200, "Billy", 0);
        // When updateReservation is called on the Mock Reservation DAO, throw an IOException
        doThrow(new IOException()).when(mockReservationDAO).updateReservation(reservation);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.updateReservation(reservation);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetReservationes() throws IOException { // getReservationes may throw IOException
        // Setup
        Reservation[] reservations = new Reservation[2];
        reservations[0] = new Reservation(12, 12, 100, 200, "Billy", 0);
        reservations[1] = new Reservation(13, 12, 400, 500, "Bob", 0);
        // When getReservationes is called return the reservations created above
        when(mockReservationDAO.getReservations()).thenReturn(reservations);

        // Invoke
        ResponseEntity<Reservation[]> response = reservationController.getReservations();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(reservations,response.getBody());
    }

    @Test
    public void testGetReservationesHandleException() throws IOException { // getReservationes may throw IOException
        // Setup
        // When getReservationes is called on the Mock Reservation DAO, throw an IOException
        doThrow(new IOException()).when(mockReservationDAO).getReservations();

        // Invoke
        ResponseEntity<Reservation[]> response = reservationController.getReservations();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testDeleteReservation() throws IOException { // deleteReservation may throw IOException
        // Setup
        int reservationId = 99;
        // when deleteReservation is called return true, simulating successful deletion
        when(mockReservationDAO.deleteReservation(reservationId)).thenReturn(true);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.deleteReservation(reservationId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteReservationNotFound() throws IOException { // deleteReservation may throw IOException
        // Setup
        int reservationId = 99;
        // when deleteReservation is called return false, simulating failed deletion
        when(mockReservationDAO.deleteReservation(reservationId)).thenReturn(false);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.deleteReservation(reservationId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteReservationHandleException() throws IOException { // deleteReservation may throw IOException
        // Setup
        int reservationId = 99;
        // When deleteReservation is called on the Mock Reservation DAO, throw an IOException
        doThrow(new IOException()).when(mockReservationDAO).deleteReservation(reservationId);

        // Invoke
        ResponseEntity<Reservation> response = reservationController.deleteReservation(reservationId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
