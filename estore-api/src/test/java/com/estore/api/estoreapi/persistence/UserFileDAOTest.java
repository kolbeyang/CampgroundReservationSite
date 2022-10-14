package com.estore.api.estoreapi.persistence;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
 * Tests the User File DAO class
 */
@Tag("persistence")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    ObjectMapper mockObjectMapper;
    User[] testUsers;

     /**
     * Instantiates the value for Inventory File DAO class tests so that each one will be tested
     * @throws IOException
     */
    @BeforeEach
    public void setupuserFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);

        User user1 = new User("BillyBob", "1234", true);
        User user2 = new User("Michael", "2355", false);
        User user3 = new User("Kolbe", "8909", false);
        testUsers = new User[3];
        testUsers[0] = user1;
        testUsers[1] = user2;
        testUsers[2] = user3;

        when(mockObjectMapper
            .readValue(new File("Whatever.txt"), User[].class))
                .thenReturn(testUsers);

        userFileDAO = new UserFileDAO("Whatever.txt", mockObjectMapper);
    }
    

    /**
     * Tests getting the users list from the userFileDAO matches the test User list
     */
    @Test
    public void testgetUsers(){
        User[] actual = userFileDAO.getUsers();
        for(int i = 0; i < testUsers.length; i++){
            assertEquals(userFileDAO.getUser(actual[i].getUsername()),actual[i]);
        }

    }


    /**
     * Tests that getting a specific user will match the corresponding User from the testUsers based on username
     */
    @Test
    public void testgetUser(){
        for(int i = 0; i < testUsers.length; i++){
            assertEquals(testUsers[i], userFileDAO.getUser(testUsers[i].getUsername()));
        }
    }

    /**
     * Tests that creating a new valid user does not throw an IOException and that the newUser appears
     * in the userFileDAO's user list
     */
    @Test
    public void testcreateUser(){
        User newUser = new User("Troy", "GoodFoodGuy", false);
        assertDoesNotThrow(()->userFileDAO.createUser(newUser), "Something unexpected occurred");
        assertEquals(newUser, userFileDAO.getUser(newUser.getUsername()));

    }


    /**
     * Tests that updating an existing valud user does not throw an unexpected exception and that the 
     * the updated isAdmin is correct
     */
    @Test
    public void testupdateUser(){
        User user = new User(testUsers[2].getUsername(), testUsers[2].getPassword(), true);
        

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getUsername());
        assertEquals(actual,user);
        //For now this test not working is fine but later on we need to specify what changing the admin will 
        //
        assertTrue(actual.getIsAdmin());
        
    }

    /**
     * Tests that deleting an existing user does not throw any unexpected exceptions and that the
     * userFileDAO user data structure is one less in length then the test users after deletion.
     */
    @Test
    public void testdeleteUser(){
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(testUsers[0].getUsername()),
        "Unexpected exception thrown");

        assertEquals(result,true);
        assertEquals(userFileDAO.getUsers().length,testUsers.length-1);
    }

    /**
     * Tests that user FileDAO will return a null value if a searched username does not exist
     */
    @Test
    public void testUserNotFound(){
        User result = assertDoesNotThrow(() -> userFileDAO.getUser("Doesnt Exist"), "Unexpected exception thrown");
        assertNull(result);
    }

    /**
     * Tests that user FileDAO will return a null value if attempting to delete an non-existant user. Also tests that the
     * userFileDAO user data structure is one less in length then the test users after deletion.
     */
    @Test
    public void testDeleteUserNotFound(){
        Boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("Doesnt Exist"), "Unexpected exception thrown");
        assertNull(userFileDAO.getUser("Doesnt Exist"));
        assertEquals(userFileDAO.getUsers().length,testUsers.length);
    }

    /**
     * Tests that the user FileDAO will not throw a exeption if a user that is not contained in the user FileDAO is attempted
     * to be update. Also asserts that the resulting call to updateUser() will return a null value.
     */
    @Test
    public void testUpdateUserNotFound(){
        User fakeUser = new User("Dr. Hoobert", "Glades", false);
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(fakeUser), "Unexpected exception thrown" );
        assertNull(result);
    }   

    /**
     * Tests that the IOException is properly thrown when something in the process of creating a newUser goes wrong.
     * @throws IOException
     */
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(User[].class));

        User newUser = new User("Dr. Hoobert", "Glades", false);

        assertThrows(IOException.class, () -> userFileDAO.createUser(newUser));
    }

    /**
     * Tests that the IOExcpetion is properly thrown when something in the process of creating the user File DAO object
     * goes wrong
     * @throws IOException
     */
    @Test
    public void testLoadException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("Whatever.txt"), User[].class);

        assertThrows(IOException.class, 
                       () -> new UserFileDAO("Whatever.txt", mockObjectMapper), 
                      " Did not throw proper exception");
    }

}
