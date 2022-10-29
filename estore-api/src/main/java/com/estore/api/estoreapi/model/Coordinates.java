package com.estore.api.estoreapi.model;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates {
    private static final int MAXVALUEX = 600;
    private static final int MAXVALUEY = 325;
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setXCoordinate(int xVal){
        this.x = xVal;
    }

    public void setYCoordinate(int yVal){

    }



}
