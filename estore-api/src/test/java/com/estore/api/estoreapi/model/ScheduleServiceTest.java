package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.AuthenticationService;
import com.estore.api.estoreapi.model.LoginRequest;
import com.estore.api.estoreapi.model.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Model-tier")
public class ScheduleServiceTest {
    private ScheduleService scheduleService;

    private UserDAO mockUserDAO;
    private AuthenticationService mockAuthenticationService;
    private InventoryDAO mockInventoryDAO;
    private ReservationDAO mockReservationDAO;

    @BeforeEach
    public void setupScheduleService() {
        mockUserDAO = mock(UserDAO.class);
        mockAuthenticationService = mock(AuthenticationService.class);
        mockReservationDAO = mock(ReservationDAO.class);
        mockInventoryDAO = mock(InventoryDAO.class);
        scheduleService = new ScheduleService(mockInventoryDAO, mockReservationDAO);
    }

    @Test
    public void testCreateReservation() throws IOException {
        Reservation reservation1 = new Reservation(10,10,100,200,"Billy", 0);
        Reservation reservation2 = new Reservation(10,10,300,200,"Billy", 0);

        when(mockReservationDAO.createReservation(reservation1)).thenReturn(reservation1);
        when(mockReservationDAO.createReservation(reservation2)).thenReturn(reservation2);

        Reservation output1 = scheduleService.createReservation(reservation1);
        Reservation output2 = scheduleService.createReservation(reservation2);

        assertEquals(output1, reservation1);
        assertEquals(output2, reservation2);
    }

    @Test
    public void testIsValidReservation() throws IOException {
        Reservation reservation1 = new Reservation(10,10,100,200,"Billy", 0);
        Reservation reservation2 = new Reservation(10,10,200,300,"Billy", 0);

        Reservation goodReservation = new Reservation(10,10,400,500,"Billy", 0);
        Reservation badReservation = new Reservation(10,10,150,500,"Billy", 0);
        Reservation differentReservation = new Reservation(10,12,150,500,"Billy", 0);

        Reservation[] reservations = {reservation1, reservation2};

        Campsite campsite1 = new Campsite(10, "Foggy Valley", 10);
        
        when(mockInventoryDAO.getCampsite(10)).thenReturn(campsite1);
        when(mockInventoryDAO.getCampsite(12)).thenReturn(null);
        when(mockReservationDAO.getCampsiteReservations(10)).thenReturn(reservations);

        Boolean goodIsValid = scheduleService.isValidReservation(goodReservation);
        Boolean badIsValid = scheduleService.isValidReservation(badReservation);
        Boolean differentIsValid = scheduleService.isValidReservation(differentReservation);

        assertEquals(goodIsValid, true);
        assertEquals(badIsValid, false);
        assertEquals(differentIsValid, false);
    }

}
