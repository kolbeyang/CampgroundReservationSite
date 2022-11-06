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

    ReservationDAO reservationDAO;
    


    /**
     * Instantiates the value for Inventory File DAO class tests so that each one will be tested
     * @throws IOException
     */
    @BeforeEach
    public void setupinventoryFileDAO()throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        Campsite camp1 = new Campsite(1, "Lucky Meadows Campsite", 11.99, 0, 0 );
        Campsite camp2 = new Campsite(2, "Eagle heights Campsite", 12.43, 0 ,0  );
        Campsite camp3 = new Campsite(3, "Stony Brooks Campsite", 12.34, 0 , 0 );
        testCampsites = new Campsite[3];
        testCampsites[0] = camp1;
        testCampsites[1] = camp2;
        testCampsites[2] = camp3;


            // When the object mapper is supposed to read from the file
        // the mock object mapper will return the campsite array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Campsite[].class))
                .thenReturn(testCampsites);

        inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
    }


    /**
     * Tests the getting a campsite from inventoryFileDAO will have all of the correct values
     * compared to the testcampsites array
     */
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

    /**
     * Ensures that the inventoryFileDAO's getCampsites returns the same value as the 
     * testCampsite array
     */
    @Test
    public void testgetCampsite(){
        Campsite[] campsites = inventoryFileDAO.getCampsites();
       for(int i = 0; i < campsites.length; i++){
            assertEquals(inventoryFileDAO.getCampsite(campsites[i].getId()), testCampsites[i]);
       }
    }

    /**
     *
     * Ensures that the inventoryFileDAO returns the correct campsites based on the 
     * containsText keyword
     */
    @Test
    public void testfindCampsites(){
        Campsite[] campsites  = inventoryFileDAO.getCampsites();
        Campsite[] containsTxt = new Campsite[campsites.length];
        Campsite[] allCampsites = inventoryFileDAO.findCampsites("s");
        for(int i = 0; i < campsites.length; i++){
            containsTxt[i] = campsites[i];
            //assertEquals(containsTxt[i], inventoryFileDAO.findCampsites(campsites[i].getName())[0]);
            assertEquals(campsites[0], allCampsites[0]); //All campsites contain letter s
            assertEquals(campsites[1], allCampsites[1]);
            assertEquals(campsites[2], allCampsites[2]);
        }

        
        //assertEquals(containsTxt, allCampsites);
    }


    /**
     * Tests that Creating a campsite creates is properly placed in the data
     * @throws IOException
     */
    @Test
    public void testcreateCampsites() throws IOException{
        Campsite newCampsite = new Campsite(4, "Warbling wonders Campsite", 34.54,  0,0 );
        inventoryFileDAO.createCampsite(newCampsite);
        assertEquals(newCampsite, inventoryFileDAO.getCampsite(newCampsite.getId()));
    }

     /**
      * Tests that the update method in campsites works correctly
      */
    @Test
    public void testupdateCampsite(){
        Campsite updateCampsite = new Campsite(3, "Worming Wonders Campsite", 34.54, 0,0);

        testCampsites[2].setName(updateCampsite.getName());
        testCampsites[2].setRate(updateCampsite.getRate());

        assertDoesNotThrow( () -> inventoryFileDAO.updateCampsite(updateCampsite), "This is a message");

        assertEquals(testCampsites[2],inventoryFileDAO.getCampsite(updateCampsite.getId()));

    }

    /**
     * Test the case where an id with no campsite attributed to it is used in the call of
     * getCampsite
     */
    @Test
    public void testCampsiteNotFound(){
        Campsite result = assertDoesNotThrow(() -> inventoryFileDAO.getCampsite(1000), "Unexpected exception thrown");
        assertNull(result);
    }

    /**
     * Tests the case where an id no with campsite attributed is attempted to be deleted from the
     * database
     */
    @Test
    public void testDeleteCampsiteNotFound(){
        Boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteCampsite(1000), "Unexpected exception thrown");

        assertFalse(result);
        assertEquals(inventoryFileDAO.getCampsites().length,testCampsites.length);

    }

    /**
     * Tests the case where a campsite with an id that with no campsite attributed to it is attempted to be updated
     * in the database
     */
    @Test
    public void testUpdateCampsiteNotFound(){
        Campsite fakeCamp = new Campsite(999, "Stony Brooks Campsite", 45,  0,0);
        Campsite result = assertDoesNotThrow(() -> inventoryFileDAO.updateCampsite(fakeCamp), "fakeCamp should not exist" );
        assertNull(result);
    }   
    
    /**
     * Tests the deletion of a campsite from the database using a id of a existing campsite
     */
    @Test
    public void testDeleteCampsite(){
        boolean result = assertDoesNotThrow( () -> inventoryFileDAO.deleteCampsite(1), "This ");

        assertEquals(result, true);
        assertNull(inventoryFileDAO.getCampsite(1));

    }


    /**
     * Tests all cases where save() is used in the User File DAO class
     * and throws a IOexception to test them
     * @throws IOException
     */
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(Campsite[].class));

        Campsite newCampsite = new Campsite(23, "Evererst Glades Campsite", 9.65, 0,0);

        assertThrows(IOException.class, () -> inventoryFileDAO.createCampsite(newCampsite));
    }

    /**
     * Tests the Inventory File DAO constructor's error handling when load throws a
     * IOExcption
     * @throws IOException
     */
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
