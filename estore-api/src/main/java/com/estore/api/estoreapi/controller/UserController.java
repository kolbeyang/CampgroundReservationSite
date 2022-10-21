package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.AuthenticationService;
import com.estore.api.estoreapi.model.LoginRequest;
import com.estore.api.estoreapi.model.LoginResponse;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Reservation;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.spi.ResolveResult;

import java.util.Arrays;

/**
 * User Controller handles requests for all Users.
 */
@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;
    private ReservationDAO reservationDAO;
    private AuthenticationService authenticationService;

    /**
     * Constructor for UserController
     * @param userDAO : the data access object for users
     */
    public UserController(UserDAO userDAO, AuthenticationService authenticationService, ReservationDAO reservationDAO) {
        this.userDAO = userDAO;
        this.authenticationService = authenticationService;
        this.reservationDAO = reservationDAO;
    }

    /**
     * Handles the get request for a specific user
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id : the id of the user to get
     * @return a json object of the user requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        LOG.info("GET /users/" + username);
        try {
            User user = userDAO.getUser(username);
            if (user != null)
                return new ResponseEntity<User>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all Users in the user
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @return a json object of all Users in the user
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        try {
            User[] usersArray = userDAO.getUsers();
            if (usersArray != null) 
                return new ResponseEntity<User[]>(usersArray, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the reservations of a specific user, either the cart or the reservations that have been paid for.
     * @param username: the username of the user (functions as an id) 
     * @param paid: boolean indicates whether to get the unpaid reservations (cart) or paid
     * @return
     */
    @GetMapping("/{username}/reservations/")
    public ResponseEntity<Reservation[]> getUserReservations(@PathVariable String username, @RequestParam boolean paid) {
        LOG.info("GET /users/" + username + "/reservations/?paid="+paid);
        try {
            // reservationDAO.getUserReservations(userID) will iterate through all reservations and return those with
            // corresponding username and with a matching paid status (true or false)
            Reservation[] reservationArray = reservationDAO.getUserReservations(username, paid);
            //System.out.println("getUserReservations: length of reservationArray" + reservationArray.length);
            if(reservationArray != null)
                return new ResponseEntity<Reservation[]>(reservationArray, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Pay for a users cart
     * @param username
     * @return array of paid reservations
     */
    @PutMapping("/{username}/reservations/purchase")
    public ResponseEntity<Reservation[]> payCart(@PathVariable String username) {
        LOG.info("PUT /users/" + username + "/reservations/purchase");
        try {
            Reservation[] paidReservations = reservationDAO.payCart(username);
            if(paidReservations != null)
                return new ResponseEntity<Reservation[]>(paidReservations, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the reservations of a specific user, either the cart or the reservations that have been paid for.
     * @param username: the username of the user (functions as an id) 
     * @param paid: boolean indicates whether to get the unpaid reservations (cart) or paid
     * @return
     */
    @GetMapping("/{username}/total")
    public ResponseEntity<Double> getCartTotal(@PathVariable String username) {
        LOG.info("GET /users/" + username + "/total");
        try {
            // reservationDAO.getCartTotal(username) will iterate through all reservations and return the total of the 
            // prices of those with corresponding username and paid status false
            double total = reservationDAO.getCartTotal(username);
            return new ResponseEntity<Double>(total, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a user based on the input if a user of the same name has not yet been created
     * returns status code of CONFLICT, CREATED, or INTERNAL_SERVER_ERROR
     * @param user : an object of the user to create
     * @return the new created user
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("Post createUser request received");
        String trimmedUsername = user.getUsername().trim();
        String[] splitUserStrings = trimmedUsername.split(" ");

        LOG.info("POST /users " + user);
        try {
            User[] users = userDAO.getUsers();
            if (Arrays.asList(users).contains(user))  {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } 
            else if( user.getUsername().contains(" ")){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else if(splitUserStrings.length > 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else {
                User created = userDAO.createUser(user);
                return new ResponseEntity<User>(created, HttpStatus.CREATED);
            }



        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the user with the input id to match the input data
     * returns a status code of NOT_FOUND, OK, or INTERNAL_SERVER_ERROR
     * @param user : a user object with input data
     * @return the updated user object
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);
        try {
            User updated = userDAO.updateUser(user);
            if (updated == null) 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else 
                return new ResponseEntity<User>(updated, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginRequest loginRequest) {
        LOG.info("POST /users/login");
        try {
            if (!userDAO.userExists(loginRequest.getUsername())) {
                LOG.info("User does not exist");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //This section prohibits a user from being logged in twice
            /* 
            if (authenticationService.userLoggedIn(loginRequest)) {
                LOG.info("User already logged in");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            */
            String token = authenticationService.userLogin(loginRequest);

            System.out.println("token is " + token);
            if (token != null) {

                LoginResponse loginResponse = new LoginResponse(loginRequest.getUsername(), token, authenticationService.isAdminToken(token));
                return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout(@RequestBody String token) {
        Boolean successful = authenticationService.userLogout(token);

        if (successful) return new ResponseEntity<>(HttpStatus.ACCEPTED);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes the user with the inptu id
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id
     * @return a status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        LOG.info("DELETE /users/" + username);
        try {
            if (userDAO.deleteUser(username))
                return new ResponseEntity<>(HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}