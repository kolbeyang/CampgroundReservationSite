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

/**
 * Implements the functionality for JSON file-based peristance for Heroes
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    Map<Integer,Campsite> campsites;   // Provides a local cache of the hero objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Hero
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new hero
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Hero File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public InventoryFileDAO(@Value("${campsites.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the heroes from the file
    }

    /**
     * Generates the next id for a new {@linkplain Hero hero}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Hero heroes} from the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private Campsite[] getCampsitesArray() {
        return getCampsitesArray(null);
    }

    /**
     * Generates an array of {@linkplain Hero heroes} from the tree map for any
     * {@linkplain Hero heroes} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Hero heroes}
     * in the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private Campsite[] getCampsitesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Campsite> campsiteArrayList = new ArrayList<>();

        for (Campsite campsite : campsites.values()) {
            if (containsText == null || campsite.getName().contains(containsText)) {
                campsiteArrayList.add(campsite);
            }
        }

        Campsite[] heroArray = new Campsite[campsiteArrayList.size()];
        campsiteArrayList.toArray(heroArray);
        return heroArray;
    }

    private boolean save() throws IOException {
        Campsite[] heroArray = getCampsitesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),heroArray);
        return true;
    }

    /**
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        campsites = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of heroes
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Campsite[] campsiteArray = objectMapper.readValue(new File(filename),Campsite[].class);

        // Add each hero to the tree map and keep track of the greatest id
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
            // We create a new hero object because the id field is immutable
            // and we need to assign the next unique id
            Campsite newCampsite = new Campsite(nextId(),campsite.getName(),campsite.getRate());
            campsites.put(newCampsite.getId(),newCampsite);
            save(); // may throw an IOException
            return newCampsite;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Campsite updateCampsite(Campsite campsite) throws IOException {
        synchronized(campsites) {
            if (campsites.containsKey(campsite.getId()) == false)
                return null;  // hero does not exist

            campsites.put(campsite.getId(),campsite);
            save(); // may throw an IOException
            return campsite;
        }
    }

    /**
    ** {@inheritDoc}
     */
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
