package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import animal.Rabbit;
import animal.Wolf;
import executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

/*
 * This test will determine if the rabbit is running away from predators 
 */

public class RabbitTest {

    @Before public void setUp() {

    }

    @After public void tearDown() {

    }

    @Test public void testRabbitEscape() {
        // create a situation simmilar to the example in the DESIGN section 
        World w = new World(7);
        Wolf wolf1 = new Wolf();
        Rabbit rabbit = new Rabbit();

        


        w.setTile(new Location(0,0),wolf1); // w1
        w.setTile(new Location(1,6),new Wolf()); // w 2
        w.setTile(new Location(3,3),rabbit);

        wolf1.act(w);
        rabbit.act(w);

        // expected location of rabbit x:4, y:3
        assertEquals(new Location(4,3), w.getLocation(rabbit));
        
    }
}
