import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass implements NonBlocking, Actor {
    private int lifeThreshold;
    private int lifeCount;
    private int spread;

    public Grass() {
        this.lifeThreshold = 30;
        this.lifeCount = 0;
        this.spread = 10;
    }

    public void act(World world) {
        if(lifeCount%spread==0)
            spread(world);
        if(lifeCount==lifeThreshold)
            destroyGrass(world);
        lifeCount++;
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
    }

    public void destroyGrass(World world) {
        world.remove(this);
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public int getLifeThreshhold() {
        return lifeThreshold;
    }
}
