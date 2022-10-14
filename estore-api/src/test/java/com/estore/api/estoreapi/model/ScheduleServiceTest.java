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

}
