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
import java.util.ArrayList;
import java.util.Arrays;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Campsite;


@RestController
@RequestMapping("campsites")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDAO;

    public InventoryController(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

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

    @PostMapping("")
    public ResponseEntity<Campsite> createCampsite(@RequestBody Campsite campsite) {
        LOG.info("POST /campsites " + campsite);
        try {
            Campsite[] campsites = inventoryDAO.getCampsites();
            if (Arrays.asList(campsites).contains(campsite))  {
                System.out.println("The array contains the campsite already");
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
