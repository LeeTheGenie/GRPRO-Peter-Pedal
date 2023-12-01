
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
    private Location preyLocation;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 40);
        this.territory = new HashSet<>();
        this.preyLocation = null;

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
            // currentEnergy -= 1;
        } else {
            return;
        }
    }

    public void huntInTerritory(World world) {
        findPrey(world);
        toAndFrom(world, world.getLocation(this), preyLocation);
        attackPrey(world);
        eatPrey(world);

    }

    public boolean preyInTerritory(World world) {
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    public void findPrey(World world) {
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                preyLocation = l;
            }
        }
    }

    public void attackPrey(World world) {
        world.delete(world.getTile(preyLocation));
        System.out.println("dræbt");
        if (world.containsNonBlocking(preyLocation)) {
            world.delete(world.getNonBlocking(preyLocation));
        }
        world.setTile(preyLocation, new SmallCarcass());
        System.out.println("sat carcass");
        currentEnergy -= 3;
    }

    public void eatPrey(World world) {
        int energyIncrement = 5;
        if (currentEnergy == maxEnergy) // hvis du ikke gavner af at spise så lad vær
            return;
        currentEnergy += energyIncrement;
        if (currentEnergy > maxEnergy) // hvis den er større end max, bare set den til max fordi det er max duh
            currentEnergy = maxEnergy;
        world.delete(world.getTile(preyLocation));
        System.out.println("spist");
    }
}
