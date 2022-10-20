package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic Object to represent a Login request
 * Used to compare against actual User data
 */
public class LoginResponse {
    @JsonProperty("username") private String username;
    @JsonProperty("token") private String token;
    @JsonProperty("isAdmin") private Boolean isAdmin;

    public LoginResponse(@JsonProperty("username") String username, @JsonProperty("token") String token, @JsonProperty("isAdmin") Boolean isAdmin) {
        this.username = username;
        this.token = token;
        this.isAdmin = isAdmin;
    }

    /**
     * Getter
     * @return the username of the request
     */
    public String getUsername() {return this.username;}

    /**
     * Getter
     * @return the token of the requset
     */
    public String getToken() {return this.token;}

    /**
     * Getter
     * @return whether the user is an admin or not
     */
    public Boolean isAdmin() {return this.isAdmin;}
}

