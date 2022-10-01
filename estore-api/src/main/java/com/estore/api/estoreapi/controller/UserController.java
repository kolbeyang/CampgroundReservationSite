package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

/**
 * User Controller handles requests for all Users.
 */
@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;

    /**
     * Constructor for UserController
     * @param userDAO : the data access object for users
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Handles the get request for a specific user
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id : the id of the user to get
     * @return a json object of the user requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        LOG.info("GET /users/" + id);
        try {
            User user = userDAO.getUser(id);
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
     * Creates a user based on the input if a user of the same name has not yet been created
     * returns status code of CONFLICT, CREATED, or INTERNAL_SERVER_ERROR
     * @param user : an object of the user to create
     * @return the new created user
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users " + user);
        try {
            User[] users = userDAO.getUsers();
            if (Arrays.asList(users).contains(user))  {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
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

    /**
     * Deletes the user with the inptu id
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id
     * @return a status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/" + id);
        try {
            if (userDAO.deleteUser(id))
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
