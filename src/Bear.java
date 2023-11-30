
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;

import abstracts.Animal;

public class Bear extends Animal {
    private Set<Location> territory;
    private Location spawnLocation;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 40);
        this.territory = new HashSet<>();

    }

    public void act(World world) {
        if (this.spawnLocation == null) {
            this.spawnLocation = world.getLocation(this);
            getTerritory(world);
        }
        if (preyInTerritory(world)) {
            huntInTerritory(world);
        } else {
            moveInTerritory(world);
        }

    }

    public Set<Location> getTerritory(World world) {
        Set<Location> tiles = world.getSurroundingTiles(spawnLocation, 3);
        for (Location l : tiles) {
            territory.add(l);
        }
        territory.add(world.getLocation(this));
        return territory;
    }

    public void moveInTerritory(World world) {
        Set<Location> emptyTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(emptyTiles);

        Random r = new Random();

        int randomLocation = r.nextInt(emptyTiles.size());
        Location newLocation = list.get(randomLocation);

        if (territory.contains(newLocation)) {
            world.move(this, newLocation);
        } else {
            return;
        }
    }

    public void huntInTerritory(World world) {
        toAndFrom(world, world.getLocation(this), findPrey(world));
        // eat prey
        // if bear doesnt benefit from eating just kill and spawn corpse?
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
