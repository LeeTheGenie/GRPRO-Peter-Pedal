import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;

import abstracts.Animal;

public class Bear extends Animal {
    private Set<Location> territory;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 40);
        this.territory = new HashSet<>();
    }

    public void act(World world) {

        if (preyInTerritory(world)) {
            huntInTerritory(world);
        } else {
            moveInTerritory(world);
        }

    }

    public Set<Location> getTerritory(World world) {
        Set<Location> tiles = world.getSurroundingTiles(world.getLocation(this), 3);
        for (Location l : tiles) {
            territory.add(l);
        }
        return territory;
    }

    public void moveInTerritory(World world) {
        Random r = new Random();
        int x = r.nextInt(territory.size());
        int y = r.nextInt(territory.size());
        Location moveTo = new Location(x, y);
        world.move(this, moveTo);
    }

    public void huntInTerritory(World world) {
        toAndFrom(world, world.getLocation(this), findPrey(world));
    }

    public boolean preyInTerritory(World world) {
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    public Location findPrey(World world) {
        Location preyLocation = null;
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                preyLocation = l;
            }
        }
        return preyLocation;
    }

}
