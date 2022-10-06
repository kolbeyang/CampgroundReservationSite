package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Campsite;
import com.estore.api.estoreapi.model.ScheduleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Campsite Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class InventoryControllerTest {
    private InventoryController inventoryController;
    private InventoryDAO mockInventoryDAO;
    private ScheduleService mockScheduleService;

    /**
     * Before each test, create a new InventoryController object and inject
     * a mock Campsite DAO
     */
    @BeforeEach
    public void setupInventoryController() {
        mockInventoryDAO = mock(InventoryDAO.class);
        mockScheduleService = mock(ScheduleService.class);
        inventoryController = new InventoryController(mockInventoryDAO, mockScheduleService);
    }

    @Test
    public void testGetCampsite() throws IOException {  // getCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // When the same id is passed in, our mock Campsite DAO will return the Campsite object
        when(mockInventoryDAO.getCampsite(campsite.getId())).thenReturn(campsite);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.getCampsite(campsite.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(campsite,response.getBody());
    }

    @Test
    public void testGetCampsiteNotFound() throws Exception { // createCampsite may throw IOException
        // Setup
        int campsiteId = 99;
        // When the same id is passed in, our mock Campsite DAO will return null, simulating
        // no campsite found
        when(mockInventoryDAO.getCampsite(campsiteId)).thenReturn(null);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.getCampsite(campsiteId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetCampsiteHandleException() throws Exception { // createCampsite may throw IOException
        // Setup
        int campsiteId = 99;
        // When getCampsite is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getCampsite(campsiteId);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.getCampsite(campsiteId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all InventoryController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateCampsite() throws IOException {  // createCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // when createCampsite is called, return true simulating successful
        // creation and save
        when(mockInventoryDAO.createCampsite(campsite)).thenReturn(campsite);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.createCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(campsite,response.getBody());
    }

    @Test
    public void testCreateCampsiteFailed() throws IOException {  // createCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // when createCampsite is called, return false simulating failed
        // creation and save
        when(mockInventoryDAO.createCampsite(campsite)).thenReturn(null);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.createCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateCampsiteHandleException() throws IOException {  // createCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);

        // When createCampsite is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).createCampsite(campsite);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.createCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateCampsite() throws IOException { // updateCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // when updateCampsite is called, return true simulating successful
        // update and save
        when(mockInventoryDAO.updateCampsite(campsite)).thenReturn(campsite);
        ResponseEntity<Campsite> response = inventoryController.updateCampsite(campsite);
        campsite.setName("Bolt");

        // Invoke
        response = inventoryController.updateCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(campsite,response.getBody());
    }

    @Test
    public void testUpdateCampsiteFailed() throws IOException { // updateCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // when updateCampsite is called, return true simulating successful
        // update and save
        when(mockInventoryDAO.updateCampsite(campsite)).thenReturn(null);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.updateCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateCampsiteHandleException() throws IOException { // updateCampsite may throw IOException
        // Setup
        Campsite campsite = new Campsite(99,"Foggy Valley", 13.20);
        // When updateCampsite is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).updateCampsite(campsite);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.updateCampsite(campsite);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCampsitees() throws IOException { // getCampsitees may throw IOException
        // Setup
        Campsite[] campsites = new Campsite[2];
        campsites[0] = new Campsite(99,"Foggy Valley", 13.20);
        campsites[1] = new Campsite(100,"Sunshine Falls", 15.20);
        // When getCampsitees is called return the campsites created above
        when(mockInventoryDAO.getCampsites()).thenReturn(campsites);

        // Invoke
        ResponseEntity<Campsite[]> response = inventoryController.getCampsites();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(campsites,response.getBody());
    }

    @Test
    public void testGetCampsiteesHandleException() throws IOException { // getCampsitees may throw IOException
        // Setup
        // When getCampsitees is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getCampsites();

        // Invoke
        ResponseEntity<Campsite[]> response = inventoryController.getCampsites();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchCampsitees() throws IOException { // findCampsitees may throw IOException
        // Setup
        String searchString = "all";
        Campsite[] campsites = new Campsite[2];
        campsites[0] = new Campsite(99,"Foggy Valley", 13.20);
        campsites[1] = new Campsite(100,"Sunshine Falls", 15.20);
        // When findCampsitees is called with the search string, return the two
        /// campsites above
        when(mockInventoryDAO.findCampsites(searchString)).thenReturn(campsites);

        // Invoke
        ResponseEntity<Campsite[]> response = inventoryController.searchCampsites(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(campsites,response.getBody());
    }

    @Test
    public void testSearchCampsiteesHandleException() throws IOException { // findCampsitees may throw IOException
        // Setup
        String searchString = "an";
        // When createCampsite is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).findCampsites(searchString);

        // Invoke
        ResponseEntity<Campsite[]> response = inventoryController.searchCampsites(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteCampsite() throws IOException { // deleteCampsite may throw IOException
        // Setup
        int campsiteId = 99;
        // when deleteCampsite is called return true, simulating successful deletion
        when(mockInventoryDAO.deleteCampsite(campsiteId)).thenReturn(true);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.deleteCampsite(campsiteId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteCampsiteNotFound() throws IOException { // deleteCampsite may throw IOException
        // Setup
        int campsiteId = 99;
        // when deleteCampsite is called return false, simulating failed deletion
        when(mockInventoryDAO.deleteCampsite(campsiteId)).thenReturn(false);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.deleteCampsite(campsiteId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteCampsiteHandleException() throws IOException { // deleteCampsite may throw IOException
        // Setup
        int campsiteId = 99;
        // When deleteCampsite is called on the Mock Campsite DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).deleteCampsite(campsiteId);

        // Invoke
        ResponseEntity<Campsite> response = inventoryController.deleteCampsite(campsiteId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
