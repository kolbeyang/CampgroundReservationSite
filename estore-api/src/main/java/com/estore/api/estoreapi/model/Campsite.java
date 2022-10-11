package com.estore.api.estoreapi.model;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a Campsite
 */
public class Campsite {
    private static final Logger LOG = Logger.getLogger(Campsite.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Campsite [id=%d, name=%s, rate=%f]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("rate") private double rate;
    @JsonProperty("reservations") private ArrayList<Integer> reservations;

    /**
     * Constructor, sets private variables based on input
     * @param id the id of the campsite
     * @param name the name of the campsite
     */
    public Campsite(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("rate") double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.reservations = new ArrayList<Integer>();
    }

    /**
     * @return the id of the campsite
     */
    public int getId() {return id;}

    /**
     * Sets the name of the Campsite
     * @param name : the new name
     */
    public void setName(String name) {this.name = name;}
    
    /**
     * @return the name of the campsite
     */
    public String getName() {return name;}

    /**
     * @return the nightly rate of the campsite
     */
    public double getRate() {return rate;}

    /**
     * Getter
     * @return the ArrayList of reservation ids
     */
    public ArrayList<Integer> getReservations() {return this.reservations;}

    /**
     * Sets the nightly rate of the Campsite
     * @param rate : the new rate
     */
    public void setRate(double rate) {this.rate = rate;}

    public void addReservationId(int id) {this.reservations.add(id);}

    /**
     * Two campsites are equal iff their names are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Campsite) {
            Campsite otherCampsite = (Campsite) o;
            return this.name.equals(otherCampsite.name);
        }
        return false;
    }

    /**
     * Returns a string representation of the campsite objectw
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,rate);
    }
}