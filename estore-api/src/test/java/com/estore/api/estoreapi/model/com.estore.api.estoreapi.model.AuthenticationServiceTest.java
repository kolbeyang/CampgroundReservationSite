package com.estore.api.estoreapi.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.AuthenticationService;
import com.estore.api.estoreapi.model.LoginRequest;
import com.estore.api.estoreapi.persistence.UserDAO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Tag("Model-tier")
class comestoreapiestoreapimodelAuthenticationServiceTest{
    AuthenticationService test_service;
    UserDAO test_UserDAO;
    int test_seed;
    User user;

    @BeforeEach
    public void setup(){
    test_service = new AuthenticationService(test_UserDAO);
    user = new User("Billy", "BillyRox", false);
    }

    @Test
    public void test_generate_token() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        //setup
        Method method = AuthenticationService.class.getDeclaredMethod("generateToken", (Class<?>[])null);
        method.setAccessible(true);
        String expected = "1016";

        //invoke
        String token1 = (String)method.invoke(test_service, (Object [])null);
        String token2 = (String)method.invoke(test_service, (Object [])null);

        //test
        assertEquals(expected, token1);
        assertNotEquals(expected, token2);
    }

    @Test

    public void test_startsession()  throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        //setup
        Method method = AuthenticationService.class.getDeclaredMethod("startSession", User.class);
        method.setAccessible(true);
        String expected_Seed = "1016";
        HashMap<String, String> expected_tokens = new HashMap<>();
        expected_tokens.put("1016", "Billy");

        //invoke
        String token = (String)method.invoke(test_service, user);

        //test
        assertEquals(expected_Seed, token);
        Field field = AuthenticationService.class.getDeclaredField("tokens");
        field.setAccessible(true);
        Object tokens = field.get(test_service);
        assertTrue(expected_tokens.equals(tokens));
    }



}
