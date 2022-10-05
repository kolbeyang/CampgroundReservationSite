package com.estore.api.estoreapi.model;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;

/**
 * Handles Scheduling of Reservations
 */
@Component
public class ScheduleService {
    private InventoryDAO inventoryDAO;
    private ReservationDAO reservationDAO;

    /**
     * Constructor
     * @param inventoryDAO handles Campsite data
     * @param reservationDAO handles Reservation data
     */
    public ScheduleService(InventoryDAO inventoryDAO, ReservationDAO reservationDAO) {
        this.inventoryDAO = inventoryDAO;
        this.reservationDAO = reservationDAO;
    }

    /**
     * Determines if a is between b and c, exclusive
     * @param a 
     * @param b
     * @param c
     * @return true if b < a < c
     */
    private boolean aBetweenBandC(long a, long b, long c) {
        // assumes that b < c
        boolean output = b < a && a < c;
        return output;
    }

    /**
     * Determines whether the startDate and endDate conflict with any existing reservations on the campsite
     * @param startDate the beginning of the reservation in milliseconds since 1970
     * @param endDate the end of the reservation in milliseconds since 1970
     * @param reservations the arraylist of reservation IDs from a campsite
     * @return true if there are no schedule conflicts
     */
    private boolean fitsInSchedule(long startDate, long endDate, ArrayList<Integer> reservations )  {
        Reservation currentReservation;
        long compareStart;
        long compareEnd;
        boolean overlap;
        //Go through each of the reservations
        for (int reservationId : reservations) {
            try { 
                currentReservation = this.reservationDAO.getReservation(reservationId);
                if (currentReservation == null) {
                    System.out.println("ScheduleService.fitsInSchedule: Invalid Reservation ID of " + reservationId);
                    return false;
                }
                compareStart = currentReservation.getStartDate();
                compareEnd = currentReservation.getEndDate();

                // If there is overlap in the times, return false
                overlap = false;
                overlap = overlap || ( aBetweenBandC(startDate, compareStart, compareEnd) || aBetweenBandC(compareStart, startDate, endDate) );
                overlap = overlap || ( aBetweenBandC(endDate, compareStart, compareEnd) || aBetweenBandC(compareEnd, startDate, endDate) );

                if ( overlap ) {
                    System.out.println("ScheduleService: Invalid Reservation time");
                    return false;
                }
            } catch (IOException IOE) {
                System.out.println("ScheduleService.fitsInSchedule: Invalid Reservation ID of " + reservationId);
            }
                

        }
        return true;
    }

    /**
     * Checks to see if the reservation data is valid
     * the campsiteId must exist and the start and end dates must fit within the campsite's schedule
     * @param reservation the JSON data made into a reservation object
     * @return whether the data will make a valid reservation
     * @throws IOException 
     */
    public boolean isValidReservation(Reservation reservation) throws IOException{
        System.out.println("ScheduleService: checking validity");
        int campsiteId = reservation.getCampsiteId();
        Campsite campsite = inventoryDAO.getCampsite(campsiteId);
        if (campsite != null) {
            long startDate = reservation.getStartDate();
            long endDate = reservation.getEndDate();
            return fitsInSchedule(startDate, endDate, campsite.getReservations());
        } 
        System.out.println("ScheduleService.isValidReservation: Campsite of id " + campsiteId + " not found");
        return false;
    } 

    /**
     * Creates the reservation based on the data. Adds the reservation id to the campsite's arrayList of reservations
     * @param reservation the JSON data made into a reservation object
     * @return the crated reservation
     * @throws IOException 
     */
    public Reservation createReservation(Reservation reservation) throws IOException {
        Reservation created =  reservationDAO.createReservation(reservation);
        int campsiteId = reservation.getCampsiteId();
        Campsite campsite = inventoryDAO.getCampsite(campsiteId);

        campsite.addReservationId(reservation.getId());

        return created;
    }
}
