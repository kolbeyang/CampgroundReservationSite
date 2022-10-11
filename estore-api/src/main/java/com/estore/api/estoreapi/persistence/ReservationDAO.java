package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Reservation;

/**
 * Interface for the InventoryFileDAO
 */
public interface ReservationDAO {
    /**
     * Getter
     * @return an array of all reservations
     * @throws IOException
     */
    Reservation[] getReservations() throws IOException;

    /**
     * Getter
     * @param id : an id of the reservation to look for
     * @return the reservation of the given id
     * @throws IOException
     */
    Reservation getReservation(int id) throws IOException;

    //TODO NEW
    /**
     * Getter
     * @param userID : the id of the user whose reservations to get
     * @return an array of this user's reservations
     * @throws IOException
     */
    Reservation[] getUserReservations(String username) throws IOException;

    /**
     * Creates a reservation based on the input data
     * @param reservation : a reservation object based directly on the input JSON data
     * @return the new reservation
     * @throws IOException
     */
    Reservation createReservation(Reservation reservation) throws IOException;

    /**
     * Updates a reservation of the given id based on the input data
     * @param reservation : a reservation object based directly on the input JSON data
     * @return the updated reservation
     * @throws IOException
     */
    Reservation updateReservation(Reservation reservation) throws IOException;

    /**
     * Deletes a reservation of the given id
     * @param id : the id of the reservation to delete
     * @return whether the reservation was successfully deleted
     * @throws IOException
     */
    boolean deleteReservation(int id) throws IOException;
}
