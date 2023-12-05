package abstracts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;

public abstract class Predator extends Animal {

    /**
     * At what pct energy does the predator feel hungry at.
     * Ex: hunger = 0.80d
     */
    protected double hungerFactor; 

    protected Predator(int age, int maxAge, int maxEnergy,int matureAge,int movementCost,
    int reproductionCost,int inheritedEnergy,int metabloicRate, double hungerFactor) {
        super(age,maxAge,maxEnergy,matureAge,movementCost,reproductionCost,inheritedEnergy,metabloicRate);
        this.hungerFactor = hungerFactor;
    }

    @Override
    public void act(World world) {
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
    protected boolean canEat(LivingBeing b) {
        return false;
    }

    /** 
     * @return true if hungry,
     * false if not hungry
     */
    public boolean isHungry() {
        if(Math.floor(maxEnergy*hungerFactor)>currentEnergy)
            return true;
        return false;
    }

    /**
     * Locates the nearest target within the given range.
     * @param world
     * @param range
     * @return animal if one could be found. null if none could be found.
     */
    public Animal locateTarget(World world,int range) {
        if(!world.isOnTile(this))
            return null;

        Location currentLocation = world.getLocation(this);

        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation,range);
        //List<Location> list = new ArrayList<>(surroundingTiles);


        // Find animal wit
        
        for(Location l: surroundingTiles) {
            Object target = world.getTile(l);
            if(!(target instanceof Animal)) // hvis ikke animal
                continue;
            if(!canEat((Animal)target))     // hvis ikke kan spise
                continue;
            return (Animal)target;
        }
        return null; 
    }
}