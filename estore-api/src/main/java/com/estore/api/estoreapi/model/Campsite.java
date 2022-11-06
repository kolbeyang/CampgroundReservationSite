package com.estore.api.estoreapi.model;

import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Point;

/**
 * Represents a Campsite
 */
public class Campsite {
    private static final Logger LOG = Logger.getLogger(Campsite.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Campsite [id=%d, name=%s, rate=%.2f x=%d, y=%d]";
    static final int MAX_XVALUE = 650;
    static final int MAX_YVALUE = 325;

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("rate") private double rate;
    @JsonProperty("x") private int x;
    @JsonProperty("y") private int y;

    /**
     * Constructor, sets private variables based on input
     * @param id the id of the campsite
     * @param name the name of the campsite
     * @param rate the cost of the campsite per night in dollars
     * @throws IllegalArgumentException
     */
    
    public Campsite(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("rate") double rate,
    @JsonProperty("x") int x, @JsonProperty("y") int y)  throws IllegalArgumentException {
        if(!isValidName(name)) {
            throw new IllegalArgumentException("<ERROR: Name must contain the word Campsite>");
        }
        else if(!isValidRate(rate)) {
            throw new IllegalArgumentException("<ERROR: Invalid Rate");
        }

        this.id = id;
        this.name = name;
        this.rate = rate;
        this.x = x;
        this.y = y;
        
    }
    /**
     * Constructor for when a campsite name and rate aren't provided
     * @param id the id of the campsite
     */
    /*
    public Campsite(@JsonProperty("id") int id) {
        this.id = id;
        this.name = "Unnamed Campsite";
        this.rate = 20.00;
    }
    */

    /**
     * Constructor for when a rate isn't provided
     * @param id the id of the campsite
     * @param name the name of the campsite
     */
    /*
    public Campsite(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
        this.rate = 20.00;
    }
    */


    /**
     * the Coordinates of the campsite
     * @return
     */
    public int getXCoord(){
        return this.x;
    }

    public int getYCoord(){
        return this.y;
    }

    /**
     * @return the id of the campsite
     */
    public int getId() {return id;}

    /**
     * Sets the name of the Campsite
     * @param name : the new name
     */
    public void setName(String name) {
        if(!isValidName(name)) {
            throw new IllegalArgumentException("<ERROR: Name must contain the word Campsite>");
        }
        this.name = name;
    }
    
    /**
     * @return the name of the campsite
     */
    public String getName() {return name;}

    /**
     * @return the nightly rate of the campsite
     */
    public double getRate() {return rate;}


    /**
     * Sets the nightly rate of the Campsite
     * @param rate : the new rate
     */
    public void setRate(double rate) {
        if(!isValidRate(rate)){
            throw new IllegalArgumentException("<ERROR: Invalid Rate");
        }
        this.rate = rate;
        }
    


    /**
     * Two campsites are equal iff their names are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Campsite) {
            Campsite otherCampsite = (Campsite) o;
            return this.name.toLowerCase().equals(otherCampsite.name.toLowerCase());
        }
        return false;
    }

    /**
     * Returns a string representation of the campsite objectw
     */
    @Override
    public String toString() {

        return String.format(STRING_FORMAT,id,name,rate, x, y);
    }

    /**
     * Returns a boolean value based on whether the name paramater is valid
     * @param name 
     */
    public boolean isValidName(String name) {
        name = name.toLowerCase();
        if(name.contains("campsite")) {
            return true;
        }
        return false;
    }

    /**
     * Returns a boolean value based on whether the rate paramater is valid.
     * Stubbed out
     * @param value
     * @return
     */
    public boolean isValidRate(Double value) {
        if(value <= 0) {
            return false;
        }
        return true;
        
    }

    public boolean isValidPoint(Point coord){
            return coord.x > MAX_XVALUE || coord.y > MAX_YVALUE || coord.x < 0 || coord.y < 0;
    }

}