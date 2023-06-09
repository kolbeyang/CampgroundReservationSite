package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a User
 */
public class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "User [username=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("isAdmin") private Boolean isAdmin;

    /**
     * Constructor, sets private variables based on input
     * @param username the username of the user
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String psasword, @JsonProperty("isAdmin") Boolean isAdmin) {
        this.username = username;
        this.password = psasword;
        this.isAdmin = isAdmin;
    }

    /**
     * Getter
     * @return the username of the user
     */
    public String getUsername() {return this.username;}

    /**
     * Sets a new password
     * @param password : the new password
     */
    public void setPassword(String psasword) {this.password = password;}

    /**
     * Getter
     * @return the password for this user
     */
    public String getPassword() {return this.password;}

    /**
     * Checks whether the given password is correct or not
     * @return True if the password is correct
     */
    public Boolean authenticatePassword(String password) {
        Boolean output = this.password.equals(password);
        System.out.println("User.authenticatePassword output " + output);
        return output;
    }

    /**
     * Getter
     * @return whether this user is an admin or not
     */
    public Boolean getIsAdmin() {return this.isAdmin;}

    /**
     * Setter
     * @param isAdmin the new value of isAdmin
     */
    public void setIsAdmin(Boolean isAdmin) {this.isAdmin = isAdmin;}

    /**
     * Two users are equal iff their usernames are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User otherUser = (User) o;
            return this.username.equals(otherUser.username);
        }
        return false;
    }

    /**
     * Returns a string representation of the user objectw
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username) + " isAdmin: " + isAdmin;
    }
}