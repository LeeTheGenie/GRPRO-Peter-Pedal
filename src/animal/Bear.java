package animal;

import java.util.Set;

import java.util.HashSet;
import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;

import plants.BerryBush;

import misc.Carcass;

import abstracts.Predator;
import abstracts.LivingBeing;

import executable.DisplayInformation;

public class Bear extends Predator {
    private Set<Location> territory;
    private Location spawnLocation;
    private Location targetLocation;
    private Location foodLocation;

    @Override
    public Bear newInstance() {
        return new Bear();
    }

    public Bear() {
        super(0, 100, 120, 0, 1, 0, 0, 2, 0.80);
        this.territory = new HashSet<>();
        this.targetLocation = null;
        growthStates = new String[][] { { "bear-small", "bear-small-sleeping" }, { "bear", "bear-sleeping" } };
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = isMature() ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {
        if (this.spawnLocation == null) {
            this.spawnLocation = world.getLocation(this);
            setTerritory(world);
        }
        if (targetInTerritory(world)) {
            findTargetInTerritory(world);
            hunt(world);
        }
        if (foodInTerritory(world) && isHungry()) {
            findFoodInTerritory(world);
            move(world, toAndFrom(world, foodLocation, world.getLocation(this)));
            eatFood(world, foodLocation);
        }
        moveInTerritory(world);

        super.act(world);
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
     * Sets the spawn location of the object.
     * 
     * @param location to set the spawn location to.
     */
    public void setSpawnLocation(Location location) {
        spawnLocation = location;
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
     * Checks if the object is next to a prey or food.
     * 
     * @param world
     * @return True if next to a prey or food.
     */
    public boolean nextTo(World world) {
        Location currentLocation = world.getLocation(this);
        if (foodLocation != null) {
            if (currentLocation.getX() - foodLocation.getX() == 1
                    || currentLocation.getX() - foodLocation.getX() == -1) {
                return true;

            } else if (currentLocation.getY() - foodLocation.getY() == 1
                    || currentLocation.getY() - foodLocation.getY() == -1) {
                return true;
            }
        }
        if (targetLocation != null) {
            if (currentLocation.getX() - targetLocation.getX() == 1
                    || currentLocation.getX() - targetLocation.getX() == -1) {
                return true;

            } else if (currentLocation.getY() - targetLocation.getY() == 1
                    || currentLocation.getY() - targetLocation.getY() == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the object to a prey then kills it.
     * 
     * @param world
     */

    public void hunt(World world) {
        move(world, toAndFrom(world, targetLocation, world.getLocation(this)));
        if (nextTo(world)) {
            attackTarget(world);
        }

    }

    /**
     * Goes to berrybush and eats all the berries on the bush.
     * 
     * @param world
     */
    public void forage(World world) {
        BerryBush BerryBush = (BerryBush) world.getTile(foodLocation);
        if (nextTo(world)) {
            BerryBush.setNoBerries(world);
        }
        changeEnergy(4, world);
    }

    /**
     * Scans the territory and checks if there is food in it.
     * 
     * @param world
     * @return True if there is food in the territory.
     */
    public boolean foodInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof BerryBush || world.getTile(l) instanceof Carcass) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans the territory and checks if there is any prey in it.
     * 
     * @param world
     * @return True if there is a prey in the territory.
     */
    public boolean targetInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the location of food in the territory.
     * 
     * @param world
     */
    public void findFoodInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Carcass || world.getTile(l) instanceof BerryBush) {
                foodLocation = l;
            }
        }
    }

    /**
     * Finds the location of animal to kill in the territory.
     * 
     * @param world
     */
    public void findTargetInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
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
    public void attackTarget(World world) {
        if (world.getTile(targetLocation) instanceof Wolf) { // tjek hvor mange wolf der er omkring sig hvis der er to
                                                             // eller mindre dræb dem ellers flee
            List<Location> wolves = getNearbyWolfs(world, 1);
            if (wolves.size() < 4) {
                for (Location l : wolves) {
                    ((Wolf) world.getTile(l)).die(world, "killed by bear");
                    changeEnergy(-3, world);
                }
            }
        } else {
            ((LivingBeing) world.getTile(targetLocation)).die(world, "killed by bear");
            changeEnergy(-3, world);
        }

    }

    /**
     * Eats food if it is a berrybush it eats all the berries on the bush. If it is
     * a carcass it takes a bite of the carcass.
     * 
     * @param world
     * @param foodLocation location of the food
     */
    public void eatFood(World world, Location foodLocation) {
        if (world.getTile(foodLocation) instanceof BerryBush) {
            forage(world);
        }
        if (world.getTile(foodLocation) instanceof Carcass) {
            Carcass carcass = (Carcass) world.getTile(foodLocation);
            carcass.takeBite();
        }
    }

    /**
     * Creates a ArrayList with location of all wolves withing a radius from a given
     * point.
     * 
     * @param world
     * @param radius to search in
     * @return ArrayList with all locations of the wolves within the radius
     */
    public List<Location> getNearbyWolfs(World world, int radius) {
        Set<Location> tiles = world.getSurroundingTiles(radius);
        List<Location> wolfsNearby = new ArrayList<>();
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                wolfsNearby.add(l);
            }
        }
        return wolfsNearby;
    }
}
