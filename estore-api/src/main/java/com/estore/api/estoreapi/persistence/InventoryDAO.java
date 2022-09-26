package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Campsite;

/**
 * Interface for the InventoryFileDAO
 */
public interface InventoryDAO {
    /**
     * Getter
     * @return an array of all campsites
     * @throws IOException
     */
    Campsite[] getCampsites() throws IOException;

    /**
     * Finds all campsites whose name contains the containsText
     * @param containsText : the string to look for in campsites' names
     * @return an array of campsites
     * @throws IOException
     */
    Campsite[] findCampsites(String containsText) throws IOException;

    /**
     * Getter
     * @param id : an id of the campsite to look for
     * @return the campsite of the given id
     * @throws IOException
     */
    Campsite getCampsite(int id) throws IOException;

    /**
     * Creates a campsite based on the input data
     * @param campsite : a campsite object based directly on the input JSON data
     * @return the new campsite
     * @throws IOException
     */
    Campsite createCampsite(Campsite campsite) throws IOException;

    /**
     * Updates a campsite of the given id based on the input data
     * @param campsite : a campsite object based directly on the input JSON data
     * @return the updated campsite
     * @throws IOException
     */
    Campsite updateCampsite(Campsite campsite) throws IOException;

    /**
     * Deletes a campsite of the given id
     * @param id : the id of the campsite to delete
     * @return whether the campsite was successfully deleted
     * @throws IOException
     */
    boolean deleteCampsite(int id) throws IOException;
}
