package com.estore.api.estoreapi.model;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;

@Component
public class ScheduleService {
    private InventoryDAO inventoryDAO;
    private ReservationDAO reservationDAO;

    public ScheduleService(InventoryDAO inventoryDAO, ReservationDAO reservationDAO) {
        this.inventoryDAO = inventoryDAO;
        this.reservationDAO = reservationDAO;
    }

    private boolean aBetweenBandC(long a, long b, long c) {
        // assumes that b < c
        boolean output = b < a && a < c;
        System.out.println("aBetweenBandC for inputs " + a + " " + b + " " + c + " evaluated to " + output);
        return output;
    }

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

    public boolean isValidReservation(Reservation reservation) throws IOException{
        System.out.println("ScheduleService: checking validity");
        int campsiteId = reservation.getCampsiteId();
        Campsite campsite = inventoryDAO.getCampsite(campsiteId);
        if (campsite != null) {
            long startDate = reservation.getStartDate();
            long endDate = reservation.getEndDate();
            return fitsInSchedule(startDate, endDate, campsite.getReservations());
        } 
        return false;
    } 

    public Reservation createReservation(Reservation reservation) throws IOException {
        Reservation created =  reservationDAO.createReservation(reservation);
        int campsiteId = reservation.getCampsiteId();
        Campsite campsite = inventoryDAO.getCampsite(campsiteId);

        campsite.addReservationId(reservation.getId());

        return created;
    }
}
