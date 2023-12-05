package animal;

import java.util.Set;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;
import plants.BerryBush;
import misc.SmallCarcass;
import abstracts.Predator;
import abstracts.LivingBeing;

public class Bear extends Predator {
    private Set<Location> territory;
    private Location spawnLocation;
    private Location targetLocation;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 120, 0, 0, 0, 0, 2, 0.80);
        this.territory = new HashSet<>();
        this.targetLocation = null;
    }

    @Override
    public void act(World world) {
        if (this.spawnLocation == null) {
            this.spawnLocation = world.getLocation(this);
            setTerritory(world);
        }
        if (foodInTerritory(world)) {
            findTargetInTerritory(world);
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
     * Sets a territory with the radius 2 from the spawnpoint of the object.
     * 
     * @param world
     * @return A set of locations surrounding a point.
     */
    public Set<Location> setTerritory(World world) {
        Set<Location> tiles = world.getSurroundingTiles(spawnLocation, 2);
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
    public void moveInTerritory(World world) { // there is a bug where the bear is removed somehow and not deleted so
                                               // the program crashes. Might be caused by rabbits reproduced method or
                                               // rabbithole.
        Set<Location> emptyTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(emptyTiles);

        Random r = new Random();

        int randomLocation = r.nextInt(emptyTiles.size());
        Location newLocation = list.get(randomLocation);

        if (territory.contains(newLocation)) {
            move(world, newLocation);
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
        move(world, toAndFrom(world, targetLocation, world.getLocation(this)));
        attackPrey(world);
        eatPrey(world);
    }

    /**
     * Goes to berrybush and eats all the berries on the bush. Then replaces the
     * berrybush with a regular bush.
     * 
     * @param world
     */
    public void forage(World world) {

        move(world, toAndFrom(world, targetLocation, world.getLocation(this)));
        BerryBush BerryBush = (BerryBush) world.getTile(targetLocation);
        BerryBush.setNoBerries(world);
        currentEnergy += 4;
    }

    /**
     * Scans the territory and checks if there is anything edible in it.
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
     * Finds the location of anything edible in the territory.
     * 
     * @param world
     */
    public void findTargetInTerritory(World world) {
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
        ((LivingBeing) world.getTile(targetLocation)).die(world, "killed by bear");

        if (world.containsNonBlocking(targetLocation)) {
            world.delete(world.getNonBlocking(targetLocation));
        }

        currentEnergy -= 3;
        System.out.println("dræbt");
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
