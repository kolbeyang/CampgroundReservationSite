package com.estore.api.estoreapi.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.estore.api.estoreapi.persistence.UserDAO;



import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Tag("Model-tier")
class comestoreapiestoreapimodelAuthenticationServiceTest{
    AuthenticationService test_service;
    private UserDAO mockUserDAO;
    UserDAO test_UserDAO;
    int test_seed;
    User user;
    User admin;

    @BeforeEach
    public void setup(){
        
        mockUserDAO = mock(UserDAO.class);
        user = new User("Billy", "BillyRox", false);
        admin = new User("Boss Man", "BossRox", true);
        test_service = new AuthenticationService(mockUserDAO);

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
    @Test
    public void testIsUserToken() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //setup
        Method method = AuthenticationService.class.getDeclaredMethod("startSession", User.class);
        method.setAccessible(true);

        //invoke
        method.invoke(test_service, user);
        Boolean result1 = test_service.isUserToken("Billy", "1016");
        Boolean result2 = test_service.isUserToken("BillyStinx", "1017"); //Not added user

        //test
        assertEquals(true, result1);
        assertEquals(null, result2); //returns null because there is no user with that username

    }
    @Test //doesn't pass curently
    public void testIsAdminToken() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        //setup
        when(mockUserDAO.getUser("Billy")).thenReturn(user);
        when(mockUserDAO.getUser("Boss Man")).thenReturn(admin);

        Method method = AuthenticationService.class.getDeclaredMethod("startSession", User.class);
        method.setAccessible(true);

        //invoke
        String token1 = (String)method.invoke(test_service, user);
        String token2 = (String)method.invoke(test_service, admin);
        Boolean result1 = test_service.isAdminToken(token1); //User token
        Boolean result2 = test_service.isAdminToken(token2); //Admin token
        Boolean result3 = test_service.isAdminToken("1000"); //Arbitrary token that isn't in tokens

        //test
        assertEquals(result1, false);
        assertEquals(result2, true);
        assertEquals(result3, null);
        
        

    }
    @Test //doesn't pass
    public void testUserExists(){
        //setup
        LoginRequest request1 = new LoginRequest("Billy", "BillyRox");
        LoginRequest request2 = new LoginRequest("Sally", "SallyRox");//User that doesn't exist
        when(mockUserDAO.userExists("Billy")).thenReturn(true);
        when(mockUserDAO.userExists("Boss Man")).thenReturn(false);

        //invoke
        Boolean result1 = test_service.userExists(request1);
        Boolean result2 = test_service.userExists(request2);

        //test
        assertEquals(true, result1);
        assertEquals(false, result2);


    }

    @Test //doesn't pass
    public void testUserLoggedIn() throws IOException {
        //setup
        LoginRequest request1 = new LoginRequest("Billy", "BillyRox");
        LoginRequest request2 = new LoginRequest("Boss Man", "Wrong_Password");
        when(mockUserDAO.getUser("Billy")).thenReturn(user);
        when(mockUserDAO.getUser("Boss Man")).thenReturn(admin);
        //LoginRequest request3 = new LoginRequest("Sally", "SallyRox");//User that doesn't exist (Will break code in current form)

        //invoke
        test_service.userLogin(request1); //attempts to login user with valid password
        test_service.userLogin(request2);
        Boolean result1 = test_service.userLoggedIn(request1);
        Boolean result2 = test_service.userLoggedIn(request2);

        //test
        assertEquals(true, result1);
        assertEquals(false, result2); //No token should be found due to incorrect password




    }

    @Test 
    public void testUserLogin() throws IOException {
        //setup
        LoginRequest request1 = new LoginRequest("Billy", "BillyRox");
        LoginRequest request2 = new LoginRequest("Boss Man", "Wrong_Password");
        when(mockUserDAO.getUser("Billy")).thenReturn(user);
        when(mockUserDAO.getUser("Boss Man")).thenReturn(admin);
        //LoginRequest request3 = new LoginRequest("Sally", "SallyRox");//User that doesn't exist (Will break code in current form)

        //invoke
        String Token1 = test_service.userLogin(request1);
        String result2 = test_service.userLogin(request2);
        //Boolean result3 = test_service.userLoggedIn(request3);

        //test
        assertEquals("1016", Token1); //Expects Token because valid user and correct password
        assertEquals(null, result2);//Expects Null because valid user but incorrect password
        //assertEquals(false, result3); //Expects False because user doesn't exist

    }

    @Test
    public void testUserLogout() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //setup
        Method method = AuthenticationService.class.getDeclaredMethod("startSession", User.class);
        method.setAccessible(true);

        //invoke
        String token1 = (String)method.invoke(test_service, user);
        String token2 = "1000";//arbitrary token that wouldn't exist 
        Boolean result1 = test_service.userLogout(token1);
        Boolean result2 = test_service.userLogout(token2);

        //test
        assertEquals(true, result1);
        assertEquals(false, result2);

    }


}
