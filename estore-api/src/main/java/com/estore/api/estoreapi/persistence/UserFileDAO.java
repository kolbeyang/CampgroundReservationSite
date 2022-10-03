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

import com.estore.api.estoreapi.model.User;

/**
 * The Inventory Data Access Object
 * Handles the persistance of users
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    Map<String,User> users;  
    private ObjectMapper objectMapper; 
    private static int nextId; 
    private String filename;    

    /**
     * Constructor
     * @param filename : the file of user data
     * @param objectMapper : the object mapper 
     * @throws IOException
     */
    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  
    }

    /**
     * Returns an array of all users
     * @return an array of all users
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Returns an array of all users with the input containsText
     * @param containsText : a string of text to check User names against
     * @return an array of all users
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getUsername().contains(containsText)) {
                userArrayList.add(user);
            }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves all Java objects into JSON objects in the JSON file
     * @return whether this operation was successful
     * @throws IOException
     */
    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads users from the JSON file
     * @return the array of users from the JSON file
     * @throws IOException
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        User[] userArray = objectMapper.readValue(new File(filename),User[].class);


        for (User user : userArray) {
            users.put(user.getUsername(),user);
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public User getUser(String username) {
        synchronized(users) {
            if (users.containsKey(username))
                return users.get(username);
            else
                return null;
        }
    }
    /**
     * @inheritDoc
     */
    @Override
    public User createUser(User user) throws IOException {
        synchronized(users) {
            User newUser = new User(user.getUsername(), user.getPassword(),user.getIsAdmin());
            users.put(newUser.getUsername(),newUser);
            save(); // may throw an IOException
            return newUser;
        }
    }
    /**
     * @inheritDoc
     * Only updates the isAdmin status, no other properties can change
     */
    @Override
    public User updateUser(User user) throws IOException {
        synchronized(users) {
            if (users.containsKey(user.getUsername()) == false)
                return null;  

            User existingUser = users.get(user.getUsername());
            existingUser.setIsAdmin(user.getIsAdmin());

            users.put(user.getUsername(),user);
            save(); // may throw an IOException
            return existingUser;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
