import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Plant extends LivingBeing implements NonBlocking {

    int reproductiveCooldown;
    int trueReproductiveCooldown;

    Plant(int age, int maxAge, int reproductiveCooldown) {
        super(age, maxAge);
        this.reproductiveCooldown = reproductiveCooldown;
        this.trueReproductiveCooldown = reproductiveCooldown;
    }

    @Override
    public void act(World world) {
        reproductiveCooldown--;
        if (reproductiveCooldown <= 0)
            spread(world);
        ageUp(world);
    }

    @Override
    public Plant newInstance() {
        return new Plant(0, maxAge, trueReproductiveCooldown);
    }

    public void spread(World world) {
        // Get surrounding tiles
        Set<Location> neighbors = world.getSurroundingTiles();
        List<Location> list = new ArrayList<>();
        // Remove those with blocking elements
        for (Location l : neighbors) {
            if (!world.containsNonBlocking(l)) {
                list.add(l);
            }
        }

        if (list.size() == 0)
            return; // if we dont have any surounding tiles, just give up on spreading

        // take one random surrounding tile
        int randomLocation = (int) Math.floor(Math.random() * list.size());
        Location newLocation = list.get(randomLocation);

        // create a new instance of plant and put it on the world
        world.setTile(newLocation, newInstance());
        reproductiveCooldown = trueReproductiveCooldown; // reset the spread timer
    }
}
