import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass implements NonBlocking, Actor {
    private int lifeTime; // hvor lang tid den lever i
    private int timeToNextSpread; // hvor lang tid det tager for at den spreder sig

    public Grass() {
        this.lifeTime = 30;
        this.timeToNextSpread = 10;
    }

    public void act(World world) {
        if(timeToNextSpread<=0)
            spread(world);
        if(lifeTime<=0)
            destroyGrass(world);
        lifeTime--;
        timeToNextSpread--;
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
        timeToNextSpread = 10; // reset the spread timer
    }

    public void destroyGrass(World world) {
        world.remove(this);
    }
}
