package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.naming.spi.ResolveResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Reservation;

/**
 * The Inventory Data Access Object
 * Handles the persistance of reservations
 */
@Component
public class ReservationFileDAO implements ReservationDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    Map<Integer,Reservation> reservations;  
    private ObjectMapper objectMapper; 
    private static int nextId; 
    private String filename;    

    /**
     * Constructor
     * @param filename : the file of reservation data
     * @param objectMapper : the object mapper 
     * @throws IOException
     */
    public ReservationFileDAO(@Value("${reservations.file}") String filename,ObjectMapper objectMapper) throws IOException {
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
     * Returns an array of all reservations
     * @return an array of all reservations
     */
    private Reservation[] getReservationsArray() {
        ArrayList<Reservation> reservationArrayList = new ArrayList<>();

        for (Reservation reservation : reservations.values()) {
            reservationArrayList.add(reservation);
        }

        Reservation[] reservationArray = new Reservation[reservationArrayList.size()];
        reservationArrayList.toArray(reservationArray);
        return reservationArray;
    }

    /**
     * Returns an array of all reservations of a specified user
     * @return an array of the reservations of a specified user
     */
    private Reservation[] getUserReservationsArray(String username) {
        ArrayList<Reservation> userReservations = new ArrayList<>();

        for(Reservation reservation : reservations.values()) {
            if(reservation.getUsername().equals(username))
                userReservations.add(reservation);
        }

        Reservation[] userReservationsArray = new Reservation[userReservations.size()];
        userReservations.toArray(userReservationsArray);
        return userReservationsArray;
    }

    /**
     * Returns an array of all reservations of a specified campsite
     * @return an array of the reservations of a specified campsite
     */
    private Reservation[] getCampsiteReservationsArray(int id) {
        ArrayList<Reservation> campsiteReservations = new ArrayList<>();

        for(Reservation reservation : reservations.values()) {
            if(reservation.getCampsiteId() == id)
                campsiteReservations.add(reservation);
        }

        Reservation[] campsiteReservationsArray = new Reservation[campsiteReservations.size()];
        campsiteReservations.toArray(campsiteReservationsArray);
        return campsiteReservationsArray;
    }


    /**
     * Saves all Java objects into JSON objects in the JSON file
     * @return whether this operation was successful
     * @throws IOException
     */
    private boolean save() throws IOException {
        Reservation[] reservationArray = getReservationsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),reservationArray);
        return true;
    }

    /**
     * Loads reservations from the JSON file
     * @return the array of reservations from the JSON file
     * @throws IOException
     */
    private boolean load() throws IOException {
        reservations = new TreeMap<>();
        nextId = 0;

        Reservation[] reservationArray = objectMapper.readValue(new File(filename),Reservation[].class);


        for (Reservation reservation : reservationArray) {
            reservations.put(reservation.getId(),reservation);
            if (reservation.getId() > nextId)
                nextId = reservation.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Reservation[] getReservations() {
        synchronized(reservations) {
            return getReservationsArray();
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Reservation getReservation(int id) {
        synchronized(reservations) {
            if (reservations.containsKey(id))
                return reservations.get(id);
            else
                return null;
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Reservation[] getUserReservations(String username) {
        synchronized(reservations) {
            return getUserReservationsArray(username);
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Reservation[] getCampsiteReservations(int id) {
        synchronized(reservations) {
            return getCampsiteReservationsArray(id);
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Reservation createReservation(Reservation reservation) throws IOException {
        synchronized(reservations) {
            Reservation newReservation = new Reservation(nextId(),reservation.getCampsiteId(),reservation.getStartDate(), reservation.getEndDate(), reservation.getUsername());
            reservations.put(newReservation.getId(),newReservation);
            save(); // may throw an IOException
            return newReservation;
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public Reservation updateReservation(Reservation reservation) throws IOException {
        synchronized(reservations) {
            if (reservations.containsKey(reservation.getId()) == false)
                return null;  

            reservations.put(reservation.getId(),reservation);
            save(); // may throw an IOException
            return reservation;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean deleteReservation(int id) throws IOException {
        synchronized(reservations) {
            if (reservations.containsKey(id)) {
                reservations.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
