package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.User;

/**
 * Interface for the UserFileDAO
 */
public interface UserDAO {
    /**
     * Getter
     * @return an array of all users
     * @throws IOException
     */
    User[] getUsers() throws IOException;

    /**
     * Getter
     * @param username: the unique identifier of the user
     * @return the user of the given id
     * @throws IOException
     */
    User getUser(String username) throws IOException;

    /**
     * Creates a user based on the input data
     * @param user : a user object based directly on the input JSON data
     * @return the new user
     * @throws IOException
     */
    User createUser(User user) throws IOException;

    /**
     * Updates a user of the given id based on the input data
     * Can only update the isAdmin attribute of the User
     * @param user : a user object based directly on the input JSON data
     * @return the updated user
     * @throws IOException
     */
    User updateUser(User user) throws IOException;

    /**
     * Deletes a user of the given id
     * @param id : the id of the user to delete
     * @return whether the user was successfully deleted
     * @throws IOException
     */
    boolean deleteUser(int id) throws IOException;
}
