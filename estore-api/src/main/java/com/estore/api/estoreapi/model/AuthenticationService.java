package com.estore.api.estoreapi.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.ReservationDAO;
import com.estore.api.estoreapi.persistence.UserDAO;

/**
 * Handles authentication of logins, generates session tokens
 */
@Component
public class AuthenticationService {
    private UserDAO userDAO;
    //maps tokens with usernames
    private HashMap<String, String> tokens;
    private int seed;

    /**
     * Constructor
     * @param inventoryDAO handles Campsite data
     * @param reservationDAO handles Reservation data
     */
    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.seed = 1015;
    }

    private String generateToken() {
        this.seed ++;
        return String.valueOf(this.seed);
    }

    private String startSession(User user) {
        String token = generateToken();
        this.tokens.put(token, user.getUsername());
        return token;
    }

    /**
     * Verifies that the token is associated with the given user
     * @param username the username of the user to check
     * @param token the token of the user
     * @return Whether the token is associated with the user
     * @throws IOException
     */
    public Boolean isUserToken(String username, String token) {
        if (!tokens.containsKey(token)) {
            System.out.println("Token not found");
            return null;
        }
        return tokens.get(token).equals(username);
    }

    /**
     * CHocks whether the given token is associated with an admin user
     * @param token : the token to check
     * @return whether the token is an admin token
     * @throws IOException
     */
    public Boolean isAdminToken(String token) throws IOException{
        if (!tokens.containsKey(token)) {
            System.out.println("Token not found");
            return null;
        }
        String username = tokens.get(token);
        User user = userDAO.getUser(username);
        return user.getIsAdmin();
    }

    public String userLogin(LoginRequest loginRequest) throws IOException{
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userDAO.getUser(username);
        if (user.authenticatePassword(password)) {
            //password was correct
            return startSession(user);
        } else {
            //incorrect password
            return null;
        }
    }

}
