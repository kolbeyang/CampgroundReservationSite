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
    static final String STRING_FORMAT = "Reservation [id=%d, startDate=%d, endDate=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("campsiteId") private int campsiteId;
    // Dates stored as millisecond since 1970
    @JsonProperty("startDate") private long startDate;
    @JsonProperty("endDate") private long endDate;
    @JsonProperty("price") private long price;
    @JsonProperty("username") private String username;

    /**
     * Constructor sets private variables based on input
     * @param id : the id of the Reservation
     * @param campsiteId : the id of the Campsite
     * @param startDate : the beginning time of the reservation in milliseconds since 1970
     * @param endDate : the end time of the reservation in milliseconds since 1970
     */
    public Reservation(@JsonProperty("id") int id, @JsonProperty("campsiteId") int campsiteId, @JsonProperty("startDate") long startDate, @JsonProperty("endDate") long endDate, @JsonProperty("username") String username) {
        this.id = id;
        this.campsiteId = campsiteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.username = username;

        //TODO to get the price
        //Campsite campsite = 
        //this.price = (TimeUnit.MILLISECONDS.toDays(this.endDate) - TimeUnit.MILLISECONDS.toDays(this.startDate)) *  
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
     * Sets the startDate and endDate
     * endDate must come after startDate
     * @param startDate : the new startDate
     * @param endDate : the new endDate
     */
    public void setDate(long startDate, long endDate) {
        assert endDate > startDate : "Invalid dates";

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
     * Returns a string representation of this reservation
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,startDate,endDate);
    }
}