package com.estore.api.estoreapi.model;

import java.util.logging.Logger;
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

    public Reservation(@JsonProperty("id") int id, @JsonProperty("campsiteId") int campsiteId, @JsonProperty("startDate") long startDate, @JsonProperty("endDate") long endDate) {
        this.id = id;
        this.campsiteId = campsiteId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {return id;}

    public void setDate(long startDate, long endDate) {
        assert endDate > startDate : "Invalid dates";

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getCampsiteId() {return this.campsiteId;}

    public long getStartDate() {return this.startDate;}

    public long getEndDate() {return this.endDate;}

    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,startDate,endDate);
    }
}