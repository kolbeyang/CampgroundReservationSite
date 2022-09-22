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
    // Dates stored as millisecond since 197[0]
    @JsonProperty("startDate") private long startDate;
    @JsonProperty("endDate") private long endDate;

    public Reservation(@JsonProperty("id") int id, @JsonProperty("startDate") long startDate, @JsonProperty("endDate") long endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {return id;}

    public void setDate(long startDate, long endDate) {
        assert endDate > startDate : "Invalid dates";

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getStartDate() {return this.startDate;}

    public long getEndDate() {return this.endDate;}

    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,startDate,endDate);
    }
}