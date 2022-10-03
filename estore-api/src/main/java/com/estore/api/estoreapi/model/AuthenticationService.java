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
