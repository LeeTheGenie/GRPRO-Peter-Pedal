package animal;

import java.awt.Color;

import abstracts.LivingBeing;
import abstracts.Predator;

import executable.DisplayInformation;

import itumulator.world.World;
import misc.Carcass;
import plants.BerryBush;

import itumulator.world.Location;

public class Monkey extends Predator {

    private Location foodLocation;
    private Location targetLocation;
    private boolean hasStick;

    public Monkey() {
        super(0, 100, 300, 18, 1, 10, 0, 2,
                0.80d);
        growthStates = new String[][] { { "monkey-small", "monkey-small-sleeping" }, { "monkey", "monkey-sleeping" } };
        this.foodLocation = null;
        this.targetLocation = null;
        this.hasStick = false;
    }

    @Override
    public Monkey newInstance() {
        return new Monkey();
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = (matureAge <= age) ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {

        super.act(world);
    }

    /**
     * Eats all the berries on the bush. Then replaces the
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
     * Function meant to be run on a livingBeing,
     * Checks if the livingBeing is eatable
     * 
     * @param livingBeing
     * @return true/false
     * 
     */
    protected boolean canEat(World world, LivingBeing livingBeing) {
        if (livingBeing instanceof BerryBush) {
            return true;
        }
        if (livingBeing instanceof Carcass) {
            return true;
        }
        if (livingBeing instanceof Rabbit) {
            if (hasStick) {
                return true;
            }
        }
        return false;
    }

    public boolean getHasStick() {
        return hasStick;
    }
    // pack
    // pick berries
    // destroy bushes for sticks
    // without stick can only pick berries
    // with stick can kill rabbit
}
