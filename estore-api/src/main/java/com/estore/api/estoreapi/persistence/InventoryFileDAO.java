package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Campsite;

@Component
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    Map<Integer,Campsite> campsites;  
    private ObjectMapper objectMapper; 
    private static int nextId; 
    private String filename;    

    public InventoryFileDAO(@Value("${campsites.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private Campsite[] getCampsitesArray() {
        return getCampsitesArray(null);
    }

    private Campsite[] getCampsitesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Campsite> campsiteArrayList = new ArrayList<>();

        for (Campsite campsite : campsites.values()) {
            if (containsText == null || campsite.getName().contains(containsText)) {
                campsiteArrayList.add(campsite);
            }
        }

        Campsite[] campsiteArray = new Campsite[campsiteArrayList.size()];
        campsiteArrayList.toArray(campsiteArray);
        return campsiteArray;
    }

    private boolean save() throws IOException {
        Campsite[] campsiteArray = getCampsitesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),campsiteArray);
        return true;
    }

    private boolean load() throws IOException {
        campsites = new TreeMap<>();
        nextId = 0;

        Campsite[] campsiteArray = objectMapper.readValue(new File(filename),Campsite[].class);

        for (Campsite campsite : campsiteArray) {
            campsites.put(campsite.getId(),campsite);
            if (campsite.getId() > nextId)
                nextId = campsite.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    @Override
    public Campsite[] getCampsites() {
        synchronized(campsites) {
            return getCampsitesArray();
        }
    }

    @Override
    public Campsite[] findCampsites(String containsText) {
        synchronized(campsites) {
            return getCampsitesArray(containsText);
        }
    }

    @Override
    public Campsite getCampsite(int id) {
        synchronized(campsites) {
            if (campsites.containsKey(id))
                return campsites.get(id);
            else
                return null;
        }
    }

    @Override
    public Campsite createCampsite(Campsite campsite) throws IOException {
        synchronized(campsites) {
            Campsite newCampsite = new Campsite(nextId(),campsite.getName(),campsite.getRate());
            campsites.put(newCampsite.getId(),newCampsite);
            save(); // may throw an IOException
            return newCampsite;
        }
    }

    @Override
    public Campsite updateCampsite(Campsite campsite) throws IOException {
        synchronized(campsites) {
            if (campsites.containsKey(campsite.getId()) == false)
                return null;  

            campsites.put(campsite.getId(),campsite);
            save(); // may throw an IOException
            return campsite;
        }
    }


    @Override
    public boolean deleteCampsite(int id) throws IOException {
        synchronized(campsites) {
            if (campsites.containsKey(id)) {
                campsites.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
