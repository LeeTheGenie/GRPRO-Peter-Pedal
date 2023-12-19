package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class WolfTest {
    World w;

    @Before
    public void setUp() {
        w = new World(5);
        System.out.println("test");
    }

    @After
    public void tearDown() {

    }

    @Test public void testMoveCloser() {
        assertEquals(0, 0);
    }
}
