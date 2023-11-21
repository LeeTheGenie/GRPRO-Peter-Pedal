import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass extends LivingBeing implements NonBlocking {

    int reproductiveCooldown = 10; // 

    public Grass() {
        super(0,30);
    }

    public void act(World world) {
        reproductiveCooldown--;
        if(reproductiveCooldown<=0)
            spread(world);
        
        ageUp(world);
    }

    /* This method is used for grass to spread */
    public void spread(World world) {
        try {
            // Get surrounding tiles
            Set<Location> neighbors = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbors);

            // take one random surrounding tile
            Random r = new Random();
            int randomLocation = r.nextInt(list.size());
            Location newLocation = list.get(randomLocation);

            // create a new instance of grass and put it on the world
            world.setTile(newLocation, new Grass());
        } catch (Exception e) {
            // There are no possible spaces to move to
        }
        reproductiveCooldown = 10; // reset the spread timer
    }
}
