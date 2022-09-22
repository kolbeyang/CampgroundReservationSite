package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Campsite;


public interface InventoryDAO {
    Campsite[] getCampsites() throws IOException;

    Campsite[] findCampsites(String containsText) throws IOException;

    Campsite getCampsite(int id) throws IOException;

    Campsite createCampsite(Campsite campsite) throws IOException;

    Campsite updateCampsite(Campsite campsite) throws IOException;

    boolean deleteCampsite(int id) throws IOException;
}
