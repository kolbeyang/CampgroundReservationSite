package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



/**
 * Tests the Inventory File DAO class
 */
public class InventoryFileDAOTest {
    InventoryFileDAO inventoryFileDAO;
    ObjectMapper mockObjectMapper;
    Campsite[] testCampsites;  
    


    /**
     * Instantiates the value for Inventory File DAO class tests so that each one will be tested
     * @throws IOException
     */
    @BeforeEach
    public void setupinventoryFileDAO()throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        Campsite camp1 = new Campsite(1, "Lucky Meadows", 11.99);
        Campsite camp2 = new Campsite(2, "Eagle heights", 12.43);
        Campsite camp3 = new Campsite(3, "Stony Brook", 14);
        testCampsites = new Campsite[3];
        testCampsites[0] = camp1;
        testCampsites[1] = camp2;
        testCampsites[2] = camp3;


            // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Campsite[].class))
                .thenReturn(testCampsites);

        inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
    }


    
    @Test
    public void testgetCampsites() {
        // Invoke
        Campsite[] campsites = inventoryFileDAO.getCampsites();
        System.out.println(campsites);
        // Analyze
        assertEquals(campsites.length,testCampsites.length);

        for (int i = 0; i < campsites.length; i++)
            assertEquals(campsites[0], testCampsites[0]);
    }

    @Test
    public void testgetCampsite(){
        Campsite[] campsites = inventoryFileDAO.getCampsites();
       for(int i = 0; i < campsites.length; i++){
            assertEquals(inventoryFileDAO.getCampsite(campsites[i].getId()), testCampsites[i]);
       }
    }

    @Test
    public void testfindCampsites(){
        Campsite[] campsites  = inventoryFileDAO.getCampsites();

    }


}
