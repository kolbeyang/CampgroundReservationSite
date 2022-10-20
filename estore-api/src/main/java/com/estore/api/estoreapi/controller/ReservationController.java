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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import com.estore.api.estoreapi.persistence.ReservationDAO;
import com.estore.api.estoreapi.model.Reservation;

/**
 * Reservation Controller handles requests for all Reservations.
 */
@RestController
@RequestMapping("reservations")
public class ReservationController {
    private static final Logger LOG = Logger.getLogger(ReservationController.class.getName());
    private ReservationDAO reservationDAO;

    /**
     * Constructor for ReservationController
     * @param reservationDAO : the data access object for reservations
     */
    public ReservationController(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Handles the get request for a specific reservation
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id : the id of the reservation to get
     * @return a json object of the reservation requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable int id) {
        LOG.info("GET /reservations/" + id);
        try {
            Reservation reservation = reservationDAO.getReservation(id);
            if (reservation != null)
                return new ResponseEntity<Reservation>(reservation,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all Reservations in the reservation
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @return a json object of all Reservations in the reservation
     */
    @GetMapping("")
    public ResponseEntity<Reservation[]> getReservations() {
        LOG.info("GET /reservations");
        try {
            Reservation[] reservationsArray = reservationDAO.getReservations();
            if (reservationsArray != null) 
                return new ResponseEntity<Reservation[]>(reservationsArray, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a reservation based on the input
     * returns status code of CONFLICT, CREATED, or INTERNAL_SERVER_ERROR
     * @param reservation : an object of the reservation to create
     * @return the new created reservation
     */
    @PostMapping("")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        LOG.info("POST /reservations " + reservation);
        try {
            Reservation[] reservations = reservationDAO.getReservations();
            if (Arrays.asList(reservations).contains(reservation))  {
                System.out.println("arrays.asList reservations contains the reservation already, returning status code conflict");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                Reservation created = reservationDAO.createReservation(reservation);
                return new ResponseEntity<Reservation>(created, HttpStatus.CREATED);
            }

        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the reservation with the input id to match the input data
     * returns a status code of NOT_FOUND, OK, or INTERNAL_SERVER_ERROR
     * @param reservation : a reservation object with input data
     * @return the updated reservation object
     */
    @PutMapping("")
    public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation reservation) {
        LOG.info("PUT /reservations " + reservation);
        try {
            Reservation updated = reservationDAO.updateReservation(reservation);
            if (updated == null) 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else 
                return new ResponseEntity<Reservation>(updated, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes the reservation with the inptu id
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id
     * @return a status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id) {
        LOG.info("DELETE /reservations/" + id);
        try {
            if (reservationDAO.deleteReservation(id))
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
