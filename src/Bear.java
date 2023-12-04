import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;

import abstracts.Animal;

public class Bear extends Animal {
    private Set<Location> territory;
    private Location spawnLocation;
    private Location targetLocation;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 40);
        this.territory = new HashSet<>();
        this.targetLocation = null;

    }

    public void act(World world) {
        if (this.spawnLocation == null) {
            this.spawnLocation = world.getLocation(this);
            setTerritory(world);
        }
        if (foodInTerritory(world)) {
            findTarget(world);
            if (world.getTile(targetLocation) instanceof BerryBush) {
                forage(world);
            } else {
                hunt(world);
            }
        } else {
            moveInTerritory(world);
        }
    }

    /**
     * Sets a territory from the spawnpoint of the object. The size of the territory
     * depends on the radius.
     * 
     * @param world
     * @return A set of locations surrounding a point.
     */
    public Set<Location> setTerritory(World world) {
        Set<Location> tiles = world.getSurroundingTiles(spawnLocation, 3);
        for (Location l : tiles) {
            territory.add(l);
        }
        territory.add(world.getLocation(this));
        return territory;
    }

    /**
     * Moves the object to a random tile inside its territory.
     * 
     * @param world
     */
    public void moveInTerritory(World world) {
        Set<Location> emptyTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(emptyTiles);

        Random r = new Random();

        int randomLocation = r.nextInt(emptyTiles.size());
        Location newLocation = list.get(randomLocation);

        if (territory.contains(newLocation)) {
            world.move(this, newLocation);
            currentEnergy -= 1;
        } else {
            return;
        }
    }

    /**
     * Moves the object to a prey then kills it and eats it.
     * 
     * @param world
     */
    public void hunt(World world) {
        try {
            toAndFrom(world, world.getLocation(this), targetLocation);
        } catch (Exception e) {
            // TODO: handle exception.
        }
        attackPrey(world);
        eatPrey(world);
    }

    /**
     * Goes to berrybush and eats all the berries on the bush.
     * 
     * @param world
     */
    public void forage(World world) {
        toAndFrom(world, world.getLocation(this), targetLocation);
        world.delete(world.getTile(targetLocation));
        world.setTile(targetLocation, new Bush());
        currentEnergy += 4;
    }

    /**
     * Scans the territory and checks if there is a prey in it.
     * 
     * @param world
     * @return True if there is a prey in the territory.
     *         False if there is not a prey in the territory.
     */
    public boolean foodInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf
                    || world.getTile(l) instanceof BerryBush) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the location of food in a territory.
     * 
     * @param world
     */
    public void findTarget(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf
                    || world.getTile(l) instanceof BerryBush) {
                targetLocation = l;
            }
        }
    }

    /**
     * Attacks a prey and kills it, and spawns a carcass on the same tile. If there
     * is already a non-blocking object it just replaces it.
     * 
     * @param world
     */
    public void attackPrey(World world) {
        world.delete(world.getTile(targetLocation));

        if (world.containsNonBlocking(targetLocation)) {
            world.delete(world.getNonBlocking(targetLocation));
        }
        world.setTile(targetLocation, new SmallCarcass());

        currentEnergy -= 3;
    }

    /**
     * Eats the prey and increases currentenergy. But if energy is already max then
     * the method return and does noting.
     * 
     * @param world
     */
    public void eatPrey(World world) {
        int energyIncrement = 5;
        if (currentEnergy == maxEnergy) // hvis du ikke gavner af at spise så lad vær
            return;
        currentEnergy += energyIncrement;
        if (currentEnergy > maxEnergy) // hvis den er større end max, bare set den til max fordi det er max duh
            currentEnergy = maxEnergy;
        world.delete(world.getTile(targetLocation));
    }
}
