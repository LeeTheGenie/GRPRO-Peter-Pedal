package abstracts;

import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;
import misc.Carcass;

public abstract class Predator extends Animal {

    /**
     * At what pct energy does the predator feel hungry at.
     * Ex: hunger = 0.80d
     */
    protected double hungerFactor;

    protected Predator(int age, int maxAge, int maxEnergy, int matureAge, int movementCost,
            int reproductionCost, int inheritedEnergy, int metabloicRate, double hungerFactor) {
        super(age, maxAge, maxEnergy, matureAge, movementCost, reproductionCost, inheritedEnergy, metabloicRate);
        this.hungerFactor = hungerFactor;
    }

    @Override
    public void act(World world) {
        alertNearbyPrey(world);
        super.act(world);
    }

    /**
     * Function meant to be run on a livingBeing,
     * Checks if the livingBeing is eatable
     * 
     * @param livingBeing
     * @return true/false
     * 
     */
    public boolean canEat(World world, LivingBeing livingBeing) {
        return false;
    }

    /**
     * @return true if hungry,
     *         false if not hungry
     */
    public boolean isHungry() {
        if (Math.floor(maxEnergy * hungerFactor) > currentEnergy)
            return true;
        return false;
    }

    /**
     * Locates the nearest target within the given range.
     * 
     * @param world
     * @param range
     * @return animal if one could be found. null if none could be found.
     */
    public Animal locateTarget(World world, int range) {
        if (!world.isOnTile(this))
            return null;

        Location currentLocation = world.getLocation(this);

        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, range);
        // List<Location> list = new ArrayList<>(surroundingTiles);

        // Find animal wit

        for (Location l : surroundingTiles) {
            Object target = world.getTile(l);
            if (!(target instanceof Animal)) // hvis ikke animal
                continue;
            if (!canEat(world, (Animal) target)) // hvis ikke kan spise
                continue;
            return (Animal) target;
        }
        return null;
    }

    public Carcass locateCarcass(World world, int range) {
        if (!validateLocationExistence(world))
            return null;

        for (Location l : world.getSurroundingTiles(world.getLocation(this), range)) {
            Object target = world.getTile(l);
            if (target instanceof Carcass)
                return (Carcass) target;
        }
        return null;
    }

    public void alertNearbyPrey(World world) {
        if (!validateLocationExistence(world))
            return;

        for (Location l : world.getSurroundingTiles(world.getLocation(this), 4)) {
            Object target = world.getTile(l);
            if (target instanceof Animal) {
                if (canEat(world, (LivingBeing) target)) {
                    ((Animal) target).setAlert(true);
                }
            }
        }
    }

    public void killTarget(World world) {
        if (!validateLocationExistence(world))
            return;
        for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
            Object target = world.getTile(l);
            if (target == null)
                continue;
            if (!((LivingBeing) target).validateExistence(world))
                continue;
            if (canEat(world, ((LivingBeing) target))) {
                ((LivingBeing) target).die(world);
            }
        }
    }

    public void eatTarget(World world) {
        if (!validateLocationExistence(world))
            return;
        for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
            Object target = world.getTile(l);
            if (target == null)
                continue;
            if (target instanceof Carcass) {
                ((Carcass) target).takeBite();
                changeEnergy(20, world);
            }
        }
    }
}