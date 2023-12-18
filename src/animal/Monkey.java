package animal;

import java.awt.Color;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Predator;
import abstracts.Animal;

import plants.BerryBush;

import executable.DisplayInformation;

import itumulator.world.World;
import misc.Carcass;
import plants.BerryBush;

import itumulator.world.Location;

public class Monkey extends Predator {

    private Location foodLocation;
    private boolean hasSticks;
    private boolean hasBerries;
    private Location trapLocation;
    private Trap trap;
    private MonkeyPack monkeyPack;

    public Monkey() {
        super(0, 100, 300, 18, 1, 10, 0, 2,
                0.80d);
        growthStates = new String[][] { { "monkey-small", "monkey-small-sleeping" }, { "monkey", "monkey-sleeping" } };
        this.foodLocation = null;
        this.trapLocation = null;
        this.hasSticks = false;
        this.hasBerries = false;
        this.monkeyPack = null;
        this.trap = null;
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
        if (isHungry()) {
            handleHunger(world);
        } else {
            move(world, null);
        }
        super.act(world);
    }

    public void handleHunger(World world) {
        MonkeyPack monkeyPack = this.getPack();
        if (monkeyPack.hasTrap()) {
            for (Trap t : monkeyPack.getTraps()) {
                if (t.hasContents()) {
                    trapLocation = world.getLocation(t);
                    trap = t;
                    break;
                }
            }
            move(world, trapLocation);
            eatContents(world, trap);
        }

    }

    public void eatContents(World world, Trap trap) {
        trap.removeContents();
        changeEnergy(10, world);
        trapLocation = null;
        trap = null;
    }

    public MonkeyPack getPack() {
        return monkeyPack;
    }

    public void buildTrap(World world) {
        if (hasSticks && hasBerries) {
            world.setTile(world.getLocation(this), new Trap());
            hasSticks = false;
            hasBerries = false;
            hasTrap = true;
            trapLocation = world.getLocation(this);
        }
    }

    /**
     * Eats all the berries on the bush. Then replaces the
     * berrybush with a regular bush.
     * 
     * @param world
     */
    public void forage(World world) {
        if (foodLocation == null) {
            findFood(world);
        }
        move(world, foodLocation);
        BerryBush BerryBush = (BerryBush) world.getTile(foodLocation);
        BerryBush.setNoBerries(world);
        changeEnergy(4, world);
        hasSticks = true;
        hasBerries = true;
    }

    public void findFood(World world) {
        for (Location l : world.getSurroundingTiles(3)) {
            if (world.getTile(l) instanceof Carcass || world.getTile(l) instanceof BerryBush) {
                foodLocation = l;
            }
        }
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
        return false;
    }

    // pack
    // pick berries
    // destroy bushes for sticks
    // without stick can only pick berries
    // with stick can kill rabbit
    // can reproduce
    // can sleep
    // can die
    // can eat carcass
}
