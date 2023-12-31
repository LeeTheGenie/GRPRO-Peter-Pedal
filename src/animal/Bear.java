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
import misc.WolfPack;
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
        super(0, 100, 120, 0, 2, 0, 0, 2, 0.80);
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

    @Override public boolean canEat(World world,LivingBeing livingBeing) {
        if(!validateLocationExistence(world))
            return false; 

        if(livingBeing instanceof Rabbit) {
            return true;
        }

        return false; 
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
        } else if (foodInTerritory(world) && isHungry()) {
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
        if (!validateLocationExistence(world)) {
            return;
        }                                     // rabbithole.
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
        attackTarget(world);
    }

    /**
     * Goes to berrybush and eats all the berries on the bush. Then replaces the
     * berrybush with a regular bush.
     * 
     * @param world
     */
    public void forage(World world) {
        BerryBush BerryBush = (BerryBush) world.getTile(foodLocation);
        BerryBush.setNoBerries(world);
        changeEnergy(4, world);
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
            if (world.getTile(l) instanceof BerryBush || world.getTile(l) instanceof Carcass) {
                return true;
            }
        }
        return false;
    }

    public boolean targetInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    public void findFoodInTerritory(World world) {
        for (Location l : territory) {
            if (world.getTile(l) instanceof Carcass || world.getTile(l) instanceof BerryBush) {
                foodLocation = l;
            }
        }
    }

    /**
     * Finds the location of anything edible in the territory.
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
        if (world.getTile(targetLocation) instanceof Wolf) {
            Wolf wolf = (Wolf) world.getTile(targetLocation);
            WolfPack wolfpackkk = wolf.getPack();
            if (wolf.hasPack() && wolfpackkk.getSize() > 4) {
                this.die(world, "killed by wolfpack");
            } else {
                wolf.die(world, "killed by bear");
            }
        } else {
            ((LivingBeing) world.getTile(targetLocation)).die(world, "killed by bear");
            changeEnergy(-3, world);
        }

    }

    public void eatFood(World world, Location foodLocation) {
        if (world.getTile(foodLocation) instanceof BerryBush) {
            forage(world);
        }
        if (world.getTile(foodLocation) instanceof Carcass) {
            Carcass carcass = (Carcass) world.getTile(foodLocation);
            carcass.takeBite();
        }
    }
}
