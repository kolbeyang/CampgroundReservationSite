package com.estore.api.estoreapi.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Campsite;

@Tag("Model-tier")
class comestoreapiestoreapimodelCampsiteTest{
    Campsite test_site;
    @BeforeEach
    public void setup() throws IllegalArgumentException {
        test_site = new Campsite(7, "Test Campsite", 20.00);
    }
    @Test
    public void Constructor() throws IllegalArgumentException {
        Campsite test_site;
        Campsite test_site2;
        try{
            test_site = new Campsite(6, "Test", 5.00);
            
        }
        catch(IllegalArgumentException e){
        test_site = new Campsite(6, "Test Campsite", 5.00);
        }
        try{
            test_site2 = new Campsite(6, "Test Campsite", -5.00);
        }
        catch(IllegalArgumentException e) {
            test_site2 = new Campsite(6, "Test Campsite", 5.00);
        }
        assertEquals(test_site.equals(test_site2), true);

    }
    @Test
    public void test_getId() {
        assertEquals(test_site.getId(), 7);
    }
    @Test
    public void test_getName() {
        assertEquals(test_site.getName(), "Test Campsite");
    }
    @Test
    public void test_getRate() {
        assertEquals(test_site.getRate(), 20.00);
    }
    @Test 
    public void test_setRate() {
        //setup
        try{
        test_site.setRate(0.00);
        }
        catch(IllegalArgumentException e) {
            assertEquals(test_site.getRate(), 20.00);
        }
        //assertEquals(test_site.getRate(), 20.00);
        test_site.setRate(30.00);
        assertEquals(test_site.getRate(), 30.00);
    }
    @Test
    public void test_setName() {
        try{
            test_site.setName("Hidden Valley");
            
        }
        catch(IllegalArgumentException e){
            test_site.setName("Hidden Valley Campsite");

            assertEquals("Hidden Valley Campsite", test_site.getName());
        }
    }
    @Test
    public void test_Equals() {
        //setup
        Campsite test_site2 = new Campsite(1, "Test Campsite", 20.00);
        Campsite test_site3 = new Campsite(2, "Hidden Valley Campsite", 20.00);
        Campsite test_site4 = new Campsite(3, "hidden valley campsite", 20.00);
        Object random_object = new Object(); //Random object

        //test
        assertEquals(test_site.equals(test_site), true);
        assertEquals(test_site.equals(test_site2), true);
        assertEquals(test_site.equals(test_site3), false);
        assertEquals(test_site3.equals(test_site4), true);
        assertEquals(test_site3.equals(random_object), false);
    }

    @Test
    public void test_toString() {
        //setup
        String expected = "Campsite [id=7, name=Test Campsite, rate=20.00]";

        //test
        assertEquals(expected, test_site.toString());
    }
    @Test
    public void test_isValidRate() {
        //setup
        Double rate1 = 20.00;
        Double rate2 = -5.00;
        Double rate3 = 0.00;

        //test
        assertEquals(test_site.isValidRate(rate1), true);
        assertEquals(test_site.isValidRate(rate2), false);
        assertEquals(test_site.isValidRate(rate3), false);
    }


    
    
}
