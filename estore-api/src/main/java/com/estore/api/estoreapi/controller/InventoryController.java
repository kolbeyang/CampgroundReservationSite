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
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Campsite;
import com.estore.api.estoreapi.model.Reservation;
import com.estore.api.estoreapi.model.ScheduleService;
import com.estore.api.estoreapi.persistence.ReservationDAO;

/**
 * Inventory Controller handles requests for all Campsites.
 */
@RestController
@RequestMapping("campsites")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDAO;
    private ReservationDAO reservationDAO;
    private ScheduleService scheduleService;

    /**
     * Constructor for InventoryController
     * @param inventoryDAO : the data access object for campsites
     */
    public InventoryController(InventoryDAO inventoryDAO, ScheduleService scheduleService) {
        this.inventoryDAO = inventoryDAO;
        this.scheduleService = scheduleService;
    }

    /**
     * Handles the get request for a specific campsite
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id : the id of the campsite to get
     * @return a json object of the campsite requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Campsite> getCampsite(@PathVariable int id) {
        LOG.info("GET /campsites/" + id);
        try {
            Campsite campsite = inventoryDAO.getCampsite(id);
            if (campsite != null)
                return new ResponseEntity<Campsite>(campsite,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all Campsites in the inventory
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @return a json object of all Campsites in the inventory
     */
    @GetMapping("")
    public ResponseEntity<Campsite[]> getCampsites() {
        LOG.info("GET /campsites");
        try {
            Campsite[] campsitesArray = inventoryDAO.getCampsites();
            if (campsitesArray != null) 
                return new ResponseEntity<Campsite[]>(campsitesArray, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all reservations for a specific campsite.
     * @param id : id of the campsite whose reservations to get
     * @return a json object of all reservations for this campsite
     */
    @GetMapping("/{id}/reservations")
    public ResponseEntity<Reservation[]> getCampsiteReservations(@PathVariable int id) {
        LOG.info("GET /campsites/" + id + "/reservations");
        try {
            Reservation[] reservationArray = reservationDAO.getCampsiteReservations(id);
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
     * Searches through all the campsites and finds only those whose name contains the input string
     * returns a status code of OK or INTERNAL_SERVER_ERROR
     * requests of the form /campsites/?name=____
     * @param name : the string to look for 
     * @return a JSON object of all campsites with the given string in their name
     */
    @GetMapping("/")
    public ResponseEntity<Campsite[]> searchCampsites(@RequestParam String name) {
        LOG.info("GET /campsites/?name="+name);
        try {
            Campsite[] output = inventoryDAO.findCampsites(name);
            return new ResponseEntity<Campsite[]>(output, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * Creates a campsite based on the input if a campsite of the same name has not yet been created
     * returns status code of CONFLICT, CREATED, or INTERNAL_SERVER_ERROR
     * @param campsite : an object of the campsite to create
     * @return the new created campsite
     * @throws IllegalAccessException
     */
    @PostMapping("")
    public ResponseEntity<Campsite> createCampsite(@RequestBody Campsite campsite) throws IllegalAccessException {
        LOG.info("POST /campsites " + campsite);
        try {
            Campsite[] campsites = inventoryDAO.getCampsites();
            if (Arrays.asList(campsites).contains(campsite))  {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                Campsite created = inventoryDAO.createCampsite(campsite);
                return new ResponseEntity<Campsite>(created, HttpStatus.CREATED);
            }



        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        LOG.info("POST /campsites " + reservation);
        try {
            if (scheduleService.isValidReservation(reservation)) {
                Reservation created = scheduleService.createReservation(reservation);
                return new ResponseEntity<Reservation>(created, HttpStatus.CREATED);
            } else {
                //Invalid
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the campsite with the input id to match the input data
     * returns a status code of NOT_FOUND, OK, or INTERNAL_SERVER_ERROR
     * @param campsite : a campsite object with input data
     * @return the updated campsite object
     */
    @PutMapping("")
    public ResponseEntity<Campsite> updateCampsite(@RequestBody Campsite campsite) {
        LOG.info("PUT /campsites " + campsite);
        try {
            Campsite updated = inventoryDAO.updateCampsite(campsite);
            if (updated == null) 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else 
                return new ResponseEntity<Campsite>(updated, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes the campsite with the inptu id
     * returns a status code of OK, NOT_FOUND, or INTERNAL_SERVER_ERROR
     * @param id
     * @return a status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Campsite> deleteCampsite(@PathVariable int id) {
        LOG.info("DELETE /campsites/" + id);
        try {
            if (inventoryDAO.deleteCampsite(id))
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
