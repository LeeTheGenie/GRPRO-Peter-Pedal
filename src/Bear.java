
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
            hunt(world);
        } else {
            moveInTerritory(world);
        }

    }

    /**
     * Gets a territory from the spawnpoint of the object. The size of the territory
     * depends on the radius.
     * 
     * @param world
     * @return A set of locations surrounding a point.
     */
    public Set<Location> getTerritory(World world) {
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
        findPrey(world);
        toAndFrom(world, world.getLocation(this), preyLocation);
        attackPrey(world);
        eatPrey(world);

    }

    /**
     * Scans the territory and checks if there is a prey in it.
     * 
     * @param world
     * @return True if there is a prey in the territory.
     *         False if there is not a prey in the territory.
     */
    public boolean preyInTerritory(World world) {
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the location of a prey in a territory.
     * 
     * @param world
     */
    public void findPrey(World world) {
        for (Location l : getTerritory(world)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                preyLocation = l;
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
        world.delete(world.getTile(preyLocation));
        System.out.println("dræbt");
        if (world.containsNonBlocking(preyLocation)) {
            world.delete(world.getNonBlocking(preyLocation));
        }
        world.setTile(preyLocation, new SmallCarcass());
        System.out.println("sat carcass");
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
        world.delete(world.getTile(preyLocation));
    }

    @Override
    public void toAndFrom(World world, Location to, Location from) {
        int x = from.getX();
        int y = from.getY();

        if (to.getX() != from.getX()) {
            if (to.getX() > from.getX()) {
                x = from.getX() + 1;
            }

            if (to.getX() < from.getX()) {
                x = from.getX() - 1;
            }
        }

        if (to.getY() != from.getY()) {
            if (to.getY() > from.getY()) {
                y = from.getY() + 1;
            }

            if (to.getY() < from.getY()) {
                y = from.getY() - 1;
            }
        }

        Location newLocation = new Location(x, y);
        System.out.println("going to " + newLocation);

        world.move(this, newLocation);

    }
}
