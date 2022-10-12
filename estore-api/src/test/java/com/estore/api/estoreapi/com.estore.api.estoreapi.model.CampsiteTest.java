package com.estore.api.estoreapi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.estore.api.estoreapi.model.Campsite;

@Tag("Model-tier")
class comestoreapiestoreapimodelCampsiteTest{
    Campsite test_site;
    @BeforeEach
    public void setup() throws IllegalArgumentException {
        test_site = new Campsite(7, "Test Campsite", 20.00);
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
        Campsite test_site2 = new Campsite(1, "Test Campsite", 20.00);
        Campsite test_site3 = new Campsite(2, "Hidden Valley Campsite", 20.00);
        Campsite test_site4 = new Campsite(3, "hidden valley campsite", 20.00);

        assertEquals(test_site.equals(test_site2), true);
        assertEquals(test_site.equals(test_site3), false);
        assertEquals(test_site3.equals(test_site4), true);
    }

    @Test
    public void test_toString() {
        String expected = "Campsite [id=7, name=Test Campsite, rate=20.00]";

        assertEquals(expected, test_site.toString());
    }


    
    
}
