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
        lifeCount++;
        if (lifeCount % spread == 0) {
            try {
                Location stay = world.getCurrentLocation();
                world.setTile(stay, this);
                Set<Location> neighbors = world.getEmptySurroundingTiles();
                List<Location> list = new ArrayList<>(neighbors);
                Random r = new Random();

                int randomLocation = r.nextInt(list.size());
                Location newLocation = list.get(randomLocation);

                world.move(this, newLocation);
            } catch (Exception e) {

            }
        }
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public int getLifeThreshhold() {
        return lifeThreshold;
    }
}
