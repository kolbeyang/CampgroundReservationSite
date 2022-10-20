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

    /**
     * Getter
     * @param username : the username of the user whose reservations to get
     * @return an array of this user's reservations
     * @throws IOException
     */
    Reservation[] getUserReservations(String username, boolean paid) throws IOException;

    /**
     * Getter
     * @param username the username of the user whose cart total to get
     * @return a double that is the total of the prices of the users unpaid reservations
     * @throws IOException
     */
    double getCartTotal(String username) throws IOException;

    /**
     * Getter
     * @param id : the id of the campsite whose reservations to get
     * @return an array of this campsite's reservations
     * @throws IOException
     */
    Reservation[] getCampsiteReservations(int id) throws IOException;

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
     * Updates a reservation tp paid
     * @param id : the id of the reservation to update
     * @return the updated paid reservation
     * @throws IOException
     */
    Reservation payReservation(int id) throws IOException;

    /**
     * Deletes a reservation of the given id
     * @param id : the id of the reservation to delete
     * @return whether the reservation was successfully deleted
     * @throws IOException
     */
    boolean deleteReservation(int id) throws IOException;
}
