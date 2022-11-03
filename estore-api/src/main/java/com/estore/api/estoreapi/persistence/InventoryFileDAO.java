package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Campsite;
import com.estore.api.estoreapi.model.Reservation;
import com.estore.api.estoreapi.persistence.ReservationDAO;



/**
 * The Inventory Data Access Object
 * Handles the persistance of campsites
 */
@Component
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    Map<Integer,Campsite> campsites;  
    private ObjectMapper objectMapper; 
    private static int nextId; 
    private String filename;    

    private ReservationDAO reservationDAO;

    /**
     * Constructor
     * @param filename : the file of campsite data
     * @param objectMapper : the object mapper 
     * @throws IOException
     */
    public InventoryFileDAO(@Value("${campsites.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  
    }

    /**
     * Generates the next id 
     * @return the new id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Returns an array of all campsites
     * @return an array of all campsites
     */
    private Campsite[] getCampsitesArray() {
        return getCampsitesArray(null);
    }

    /**
     * Returns an array of all campsites with the input containsText
     * @param containsText : a string of text to check Campsite names against
     * @return an array of all campsites
     */
    private Campsite[] getCampsitesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Campsite> campsiteArrayList = new ArrayList<>();
        System.out.println("getCampsitesArray: containsText " + containsText);

        for (Campsite campsite : campsites.values()) {
            if (containsText == null || containsText == " " || campsite.getName().toLowerCase().contains(containsText)) {
                System.out.println("getCampsitesArray: found a campsite that matched search criterea");
                campsiteArrayList.add(campsite);
            }
        }

        Campsite[] campsiteArray = new Campsite[campsiteArrayList.size()];
        campsiteArrayList.toArray(campsiteArray);
        return campsiteArray;
    }

    /**
     * Saves all Java objects into JSON objects in the JSON file
     * @return whether this operation was successful
     * @throws IOException
     */
    private boolean save() throws IOException {
        Campsite[] campsiteArray = getCampsitesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),campsiteArray);
        return true;
    }

    /**
     * Loads campsites from the JSON file
     * @return the array of campsites from the JSON file
     * @throws IOException
     */
    private boolean load() throws IOException {
        campsites = new TreeMap<>();
        nextId = 0;

        Campsite[] campsiteArray = objectMapper.readValue(new File(filename),Campsite[].class);


        for (Campsite campsite : campsiteArray) {
            campsites.put(campsite.getId(),campsite);
            if (campsite.getId() > nextId)
                nextId = campsite.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Campsite[] getCampsites() {
        synchronized(campsites) {
            return getCampsitesArray();
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Campsite[] findCampsites(String containsText) {
        synchronized(campsites) {
            return getCampsitesArray(containsText);
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Campsite getCampsite(int id) {
        synchronized(campsites) {
            if (campsites.containsKey(id))
                return campsites.get(id);
            else
                return null;
        }
    }
    /**
     * @throws IllegalArgumentException
     * @inheritDoc
     */
    @Override
    public Campsite createCampsite(Campsite campsite) throws IOException, IllegalArgumentException {
        synchronized(campsites) {
            Campsite newCampsite = new Campsite(nextId(),campsite.getName(),campsite.getRate(), campsite.getXCoord(), campsite.getYCoord());
            campsites.put(newCampsite.getId(),newCampsite);
            save(); // may throw an IOException
            return newCampsite;
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Campsite updateCampsite(Campsite campsite) throws IOException {
        synchronized(campsites) {
            if (campsites.containsKey(campsite.getId()) == false)
                return null;  

            campsites.put(campsite.getId(),campsite);
            save(); // may throw an IOException
            return campsite;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean deleteCampsite(int id) throws IOException {
        synchronized(campsites) {
            if (campsites.containsKey(id)) {
                Reservation[] campsiteReservations = reservationDAO.getCampsiteReservations(id);

                for(Reservation reservation : campsiteReservations)
                {
                    reservation.setToInvalid(); // sets the campsite ID to -1
                }

                campsites.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
