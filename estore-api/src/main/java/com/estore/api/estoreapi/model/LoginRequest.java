package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic Object to represent a Login request
 * Used to compare against actual User data
 */
public class LoginRequest {
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;

    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter
     * @return the username of the request
     */
    public String getUsername() {return this.username;}

    /**
     * Getter
     * @return the psasword of the requset
     */
    public String getPassword() {return this.password;}
}
