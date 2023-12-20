package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import itumulator.world.Location;
import itumulator.world.World;
import plants.Grass;

public class GrassTest {
    @Before public void setUp() {

    }

    @After public void tearDown() {

    }

    @Test public void GrassSpreadTest() {
        // create a situation simmilar to the example in the DESIGN section 
        World w = new World(2);

        Grass grass = new Grass();
        w.setTile(new Location(0,0),grass); 
        w.setTile(new Location(0,1),new Grass()); 
        w.setTile(new Location(1,1),new Grass()); 
    
        w.setCurrentLocation(new Location(0, 0));
        Set<Location> l = w.getSurroundingTiles();

        grass.spread(w);

        //Control test
        assertEquals(true,w.getTile(new Location(0, 0))instanceof Grass);

        //Test
        assertEquals(true,w.getTile(new Location(1, 0))instanceof Grass);

        for (Location location : l) {
            assertEquals(true, w.getTile(location) instanceof Grass);
        }
        
        
        
        
        
    }

    

}
