package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;


import com.fasterxml.jackson.databind.ObjectMapper;


import com.estore.api.estoreapi.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



/**
 * Tests the Inventory File DAO class
 */
@Tag("persistence")
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
        Campsite camp3 = new Campsite(3, "Stony Brooks", 14);
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
            assertEquals(campsites[i], testCampsites[i]);
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
        Campsite[] containsTxt = new Campsite[campsites.length];
        Campsite[] allCampsites = inventoryFileDAO.findCampsites("s");
        for(int i = 0; i < campsites.length; i++){
            containsTxt[i] = campsites[i];
            assertEquals(containsTxt[i], inventoryFileDAO.findCampsites(campsites[i].getName())[0]);
        }

        
        //assertEquals(containsTxt, allCampsites);
    }


    @Test
    public void testcreateCampsites() throws IOException{
        Campsite newCampsite = new Campsite(4, "Warbling wonders", 34.54);
        inventoryFileDAO.createCampsite(newCampsite);
        assertEquals(newCampsite, inventoryFileDAO.getCampsite(newCampsite.getId()));
    }

    @Test
    public void testupdateCampsite(){
        Campsite updateCampsite = new Campsite(3, "Worming Wonders", 34.54);

        testCampsites[2].setName(updateCampsite.getName());
        testCampsites[2].setRate(updateCampsite.getRate());

        assertDoesNotThrow( () -> inventoryFileDAO.updateCampsite(updateCampsite), "This is a message");

        assertEquals(testCampsites[2],inventoryFileDAO.getCampsite(updateCampsite.getId()));

    }

    @Test
    public void testCampsiteNotFound(){
        Campsite result = assertDoesNotThrow(() -> inventoryFileDAO.getCampsite(1000), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testDeleteCampsiteNotFound(){
        Boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteCampsite(1000), "Unexpected exception thrown");

        assertFalse(result);
        assertEquals(inventoryFileDAO.getCampsites().length,testCampsites.length);

    }

    @Test
    public void testUpdateCampsiteNotFound(){
        Campsite fakeCamp = new Campsite(999, "Stony Brooks", 45);
        Campsite result = assertDoesNotThrow(() -> inventoryFileDAO.updateCampsite(fakeCamp), "fakeCamp should not exist" );
        assertNull(result);
    }   
    
    @Test
    public void testDeleteCampsite(){
        boolean result = assertDoesNotThrow( () -> inventoryFileDAO.deleteCampsite(1), "This ");

        assertEquals(result, true);
        assertNull(inventoryFileDAO.getCampsite(1));

    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(Campsite[].class));

        Campsite newCampsite = new Campsite(23, "Evererst Glades", 9.65);

        assertThrows(IOException.class, () -> inventoryFileDAO.createCampsite(newCampsite));
    }

    @Test
    public void testLoadException() throws IOException{

        ObjectMapper mObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException())
            .when(mObjectMapper)
                .readValue(new File("Whatever.txt"), Campsite[].class);

        assertThrows(IOException.class, 
                       () -> new InventoryFileDAO("Whatever.txt", mObjectMapper), 
                      " Did not throw proper exception");
    
    }

}
