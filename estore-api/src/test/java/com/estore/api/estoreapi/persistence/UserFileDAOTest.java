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

@Tag("persistence")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    ObjectMapper mockObjectMapper;
    User[] testUsers;

 
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
    

    @Test
    public void testgetUsers(){
        User[] actual = userFileDAO.getUsers();
        for(int i = 0; i < testUsers.length; i++){
            assertEquals(userFileDAO.getUser(actual[i].getUsername()),actual[i]);
        }

    }

    @Test
    public void testgetUser(){
        for(int i = 0; i < testUsers.length; i++){
            assertEquals(testUsers[i], userFileDAO.getUser(testUsers[i].getUsername()));
        }
    }

    @Test
    public void testcreateUser() throws IOException{
        User newUser = new User("Troy", "GoodFoodGuy", false);
        userFileDAO.createUser(newUser);
        assertEquals(newUser, userFileDAO.getUser(newUser.getUsername()));

    }

    @Test
    public void testupdateUser(){
        User user = testUsers[2];
        

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getUsername());
        assertEquals(actual,user);
        //assertTrue(actual.getIsAdmin());
        
    }

    @Test
    public void testdeleteUser(){
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(testUsers[0].getUsername()),
        "Unexpected exception thrown");

        assertEquals(result,true);
        assertEquals(userFileDAO.getUsers().length,testUsers.length-1);
    }

    @Test
    public void testUserNotFound(){
        User result = assertDoesNotThrow(() -> userFileDAO.getUser("Doesnt Exist"), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testDeleteUserNotFound(){
        Boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("Doesnt Exist"), "Unexpected exception thrown");
        assertNull(userFileDAO.getUser("Doesnt Exist"));
        assertEquals(userFileDAO.getUsers().length,testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound(){
        User fakeUser = new User("Dr. Hoobert", "Glades", false);
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(fakeUser), "Unexpected exception thrown" );
        assertNull(result);
    }   

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(User[].class));

        User newUser = new User("Dr. Hoobert", "Glades", false);

        assertThrows(IOException.class, () -> userFileDAO.createUser(newUser));
    }

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
