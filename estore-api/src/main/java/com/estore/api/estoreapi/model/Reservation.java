package com.estore.api.estoreapi.model;

import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Reservation
 */
public class Reservation {
    private static final Logger LOG = Logger.getLogger(Reservation.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Reservation [id=%d, campsiteId=%d, startDate=%d, endDate=%d, username=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("campsiteId") private int campsiteId;
    // Dates stored as millisecond since 1970
    @JsonProperty("startDate") private long startDate;
    @JsonProperty("endDate") private long endDate;
    @JsonProperty("username") private String username;
    @JsonProperty("paid") private boolean paid;  /* Indicates the status of the reservation (paid vs unpaid) */
    @JsonProperty("price") private double price; /* The price of the reservation using the nightly rate of the campsite at the time of reservation creation */

    /**
     * Constructor sets private variables based on input
     * @param id : the id of the Reservation
     * @param campsiteId : the id of the Campsite
     * @param startDate : the beginning time of the reservation in milliseconds since 1970
     * @param endDate : the end time of the reservation in milliseconds since 1970
     * @param paid : the status of the reservation. When created it is unpaid, so set to false. When the user checks out, updated to paid.
     */
    public Reservation(@JsonProperty("id") int id, @JsonProperty("campsiteId") int campsiteId, @JsonProperty("startDate") long startDate, @JsonProperty("endDate") long endDate, @JsonProperty("username") String username, @JsonProperty("paid") boolean paid, @JsonProperty("price") double price) {
        this.id = id;
        this.campsiteId = campsiteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.username = username;
        this.paid = false;

        this.price = price; 
    }

    /**
     * Getter
     * @return username of the user whose reservation this is
     */
    public String getUsername() {return username;}

    /**
     * Getter
     * @return the id of this Reservation
     */
    public int getId() {return id;}

    /**
     * Getter
     * @return Price of the reservation (using the campsite rate from when the reservation was created)
     */
    public double getPrice(){return price;}

    /**
     * Sets the startDate and endDate
     * endDate must come after startDate
     * @param startDate : the new startDate
     * @param endDate : the new endDate
     * //@throws Exception e if endDate is before the startDate
     */
    public void setDate(long startDate, long endDate) {
        // try
        // {
        //     if(endDate > startDate)
        //     {
        //         throw(new Exception("Invalid dates"));
        //     }
        // } catch (Exception e)
        // {
        //     e.getMessage();
        // }


        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Getter
     * @return the campsiteID of the campsite this reservation belongs to
     */
    public int getCampsiteId() {return this.campsiteId;}

    /**
     * Getter
     * @return the time this reservation begins
     */
    public long getStartDate() {return this.startDate;}

    /**
     * Getter
     * @return the time this reservation ends
     */
    public long getEndDate() {return this.endDate;}

    /**
     * Getter
     * @return boolean indicating if the reservation is paid for
     */
    public boolean isPaid() {return this.paid;}

    /**
     * Setter
     */
    public void setPaid() {this.paid = true;}


    /**
     * Returns a string representation of this reservation
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,campsiteId,startDate,endDate,username);
    }

    /**
     * Two reservations are equal iff their id are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Reservation) {
            Reservation otherUser = (Reservation) o;
            Integer int1 = this.id;
            Integer int2 = otherUser.getId();
            boolean output =  int1.equals(int2);
            System.out.println("Reservation equality output equals " + output);
            return output;
        }
        return false;
    }
}