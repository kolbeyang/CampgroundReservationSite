package com.estore.api.estoreapi.model;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Campsite
 */
public class Campsite {
    private static final Logger LOG = Logger.getLogger(Campsite.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Campsite [id=%d, name=%s, rate=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("rate") private double rate;

    /**
     * @param id the id of the campsite
     * @param name the name of the campsite
     */
    public Campsite(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("rate") double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public int getId() {return id;}

    public void setName(String name) {this.name = name;}
    
    public String getName() {return name;}

    public double getRate() {return rate;}

    public void setRate(double rate) {this.rate = rate;}



    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,rate);
    }
}