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
    public void testgetName() {
        assertEquals(test_site.getName(), "Test Campsite");
    }
    @Test
    public void testgetRate() {
        assertEquals(test_site.getRate(), 20.00);
    }
    @Test
    public void testsetName() {
        
    }


    
    
}
