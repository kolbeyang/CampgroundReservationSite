package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.AuthenticationService;
import com.estore.api.estoreapi.model.LoginRequest;
import com.estore.api.estoreapi.model.LoginResponse;
import com.estore.api.estoreapi.model.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;
    private AuthenticationService mockAuthenticationService;
    private ReservationDAO mockReservationDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        mockAuthenticationService = mock(AuthenticationService.class);
        mockReservationDAO = mock(ReservationDAO.class);
        userController = new UserController(mockUserDAO, mockAuthenticationService, mockReservationDAO);
    }

    @Test
    public void testGetUser() throws IOException {  // getUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // When the same id is passed in, our mock User DAO will return the User object
        String username = user.getUsername();
        doReturn(user).when(mockUserDAO).getUser(username);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        String username = "Billy";
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        doReturn(null).when(mockUserDAO).getUser(username);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { // createUser may throw IOException
        // Setup
        String username = "Billy";
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(username);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all UserController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // when createUser is called, return true simulating successful
        // creation and save
        User[] userArray = {};
        when(mockUserDAO.getUsers()).thenReturn(userArray);
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // when createUser is called, return false simulating failed
        // creation and save
        User[] userArray = {user};
        when(mockUserDAO.getUsers()).thenReturn(userArray);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);

        // When createUser is called on the Mock User DAO, throw an IOException
        User[] userArray = {};
        when(mockUserDAO.getUsers()).thenReturn(userArray);
        when(mockUserDAO.createUser(user)).thenThrow(new IOException());

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setPassword("hello");

        // Invoke
        response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User("Billy", "1234", false);
        // When updateUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUseres() throws IOException { // getUseres may throw IOException
        // Setup
        User[] users = new User[2];
        users[0] = new User("Billy", "1234", false);
        users[1] = new User("Bob", "passwerd", false);
        // When getUseres is called return the users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testGetUseresHandleException() throws IOException { // getUseres may throw IOException
        // Setup
        // When getUseres is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException { // deleteUser may throw IOException
        // Setup
        String username = "Billy";
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(username)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException { // deleteUser may throw IOException
        // Setup
        String username = "Billy";
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(username)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        String username = "Billy";
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(username);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUserReservations() throws IOException {
        Reservation[] reservations = new Reservation[2];
        reservations[0] = new Reservation(1,12,100,200,"Billy", 0);
        reservations[1] = new Reservation(2, 12, 300, 400, "Billy", 0);

        when(mockReservationDAO.getUserReservations("Billy", false)).thenReturn(reservations);

        ResponseEntity<Reservation[]> response = userController.getUserReservations("Billy", false);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(reservations,response.getBody());
    }

    /* @Test
    public void testGetUserReservationsFailed() throws IOException {
        when(mockReservationDAO.getUserReservations("Billy", false)).thenReturn(null);

        ResponseEntity<Reservation[]> response = userController.getUserReservations("Billy", false);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    } */

    @Test
    public void testGetUserReservationsHandleException() throws IOException {
        doThrow(new IOException()).when(mockReservationDAO).getUserReservations("Billy", false);

        ResponseEntity<Reservation[]> response = userController.getUserReservations("Billy", false);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUserLogin() throws IOException {
        String username = "Billy";
        User user = new User(username, "1234", false);
        LoginRequest loginRequest = new LoginRequest("Billy", "1234");

        when(mockUserDAO.userExists(username)).thenReturn(true);
        when(mockAuthenticationService.userLoggedIn(loginRequest)).thenReturn(false);
        when(mockAuthenticationService.userLogin(loginRequest)).thenReturn("1016");

        ResponseEntity<LoginResponse> response = userController.userLogin(loginRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

    }

    @Test
    public void testUserLoginIncorrectPassword() throws IOException {
        String username = "Billy";
        User user = new User(username, "1234", false);
        LoginRequest loginRequest = new LoginRequest("Billy", "uh oh");

        when(mockUserDAO.userExists(username)).thenReturn(true);
        when(mockAuthenticationService.userLoggedIn(loginRequest)).thenReturn(false);
        when(mockAuthenticationService.userLogin(loginRequest)).thenReturn(null);

        ResponseEntity<LoginResponse> response = userController.userLogin(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    /* @Test
    public void testUserLoginAlreadyLoggedIn() throws IOException {
        String username = "Billy";
        User user = new User(username, "1234", false);
        LoginRequest loginRequest = new LoginRequest("Billy", "uh oh");

        when(mockUserDAO.userExists(username)).thenReturn(true);
        when(mockAuthenticationService.userLoggedIn(loginRequest)).thenReturn(true);
        when(mockAuthenticationService.userLogin(loginRequest)).thenReturn("1016");

        ResponseEntity<LoginResponse> response = userController.userLogin(loginRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    } */

    @Test
    public void testUserLoginHandleException() throws IOException {
        String username = "Billy";
        User user = new User(username, "1234", false);
        LoginRequest loginRequest = new LoginRequest("Billy", "uh oh");

        when(mockUserDAO.userExists(username)).thenReturn(true);
        when(mockAuthenticationService.userLoggedIn(loginRequest)).thenReturn(false);
        when(mockAuthenticationService.userLogin(loginRequest)).thenThrow(new IOException());

        ResponseEntity<LoginResponse> response = userController.userLogin(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testUserLogout() throws IOException {
        when(mockAuthenticationService.userLogout("1234")).thenReturn(true);
        ResponseEntity response = userController.userLogout("1234");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testUserLogoutFailed() throws IOException {
        when(mockAuthenticationService.userLogout("1234")).thenReturn(false);
        ResponseEntity response = userController.userLogout("1234");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

