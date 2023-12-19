package animal;

import java.awt.Color;

import abstracts.LivingBeing;
import abstracts.Predator;

import plants.BerryBush;
import plants.Bush;
import executable.DisplayInformation;

import itumulator.world.World;

import misc.Carcass;
import misc.MonkeyFamily;
import misc.Trap;
import misc.TrapActivated;

import itumulator.world.Location;

public class Monkey extends Predator {

    private Location foodLocation;
    private boolean hasSticks;
    private boolean hasBerries;
    private Location trapLocation;
    private MonkeyFamily family;
    private int children;
    private int sleepyness;
    private int bedtime;

    public Monkey() {
        super(0, 100, 300, 50, 1, 10, 0, 2,
                0.80d);
        growthStates = new String[][] { { "monkey-small", "monkey-small-sleeping" }, { "monkey", "monkey-sleeping" } };
        this.foodLocation = null;
        this.trapLocation = null;
        this.hasSticks = false;
        this.hasBerries = false;
        this.family = null;
        this.children = 0;
        this.sleepyness = 0;
        this.bedtime = 30;
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
        if (!sleeping) {
            handleFamily(world);
        }
        if (!isAdult()) {
            followAdult(world);
        }
        if (isHungry()) {
            handleHunger(world);
        }
        handleSleep(world);
        move(world, null);

        super.act(world);
        // TODO: reproduce, traphandling, dynamicdisplayinfo med stick, sleep handling,
        // can not reproduce
        // can not destroy bushes for sticks
        // does not change display with stick
        // can not sleep properly
        // can move while sleeping
        // can not build traps

    }

    @Override
    public void die(World world) {
        super.die(world);
        leaveFamily();
    }

    /**
     * Checks if the monkey is an adult.
     * 
     * @return true is adult, false if not adult.
     */
    public boolean isAdult() {
        return (age >= matureAge);
    }

    /**
     * Follows adult monkey.
     * 
     * @param world
     */
    public void followAdult(World world) {
        if (!hasFamily()) {
            return;
        }
        MonkeyFamily family = getFamily();
        for (Monkey m : family.getFamily()) {
            if (m.isAdult()) {
                move(world, world.getLocation(m));
                break;
            }
        }
    }

    /**
     * Checks the the monkey trap for contents if there is any it eats it.
     * 
     * @param world
     */
    public void checkTrap(World world) {
        move(world, trapLocation);
        Trap trap = ((Trap) world.getNonBlocking(trapLocation));
        if (trap instanceof TrapActivated) {
            claimTrap(world);
        } else {
            return;
        }
    }

    /**
     * Handles the hunger of the monkey.
     * 
     * @param world
     */
    public void handleHunger(World world) {
        findAndEatFood(world); // test findandeatfood
        /*
         * if (trapLocation != null) {
         * checkTrap(world);
         * } else {
         * if (hasSticks && hasBerries) {
         * buildTrap(world);
         * } else {
         * forage(world);
         * }
         * }
         */
    }

    /**
     * Makes the monkey sleep.
     * 
     * @param sleeping
     */
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
        this.resting = sleeping;
    }

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world) {
        if (!world.isOnTile(this))
            return;
        if (sleeping) {
            if (sleepyness < 10) {
                setSleeping(false);
            }
            sleepyness -= 10;

        } else {
            sleepyness += 1;
            if (sleepyness >= 100) {
                die(world, "sleep-exhaustion");
            }
        }

        if (wantToSleep()) {
            setSleeping(true);
        }
    }

    /**
     * Checks if the monkey wants to sleep.
     * 
     * @return
     */
    public boolean wantToSleep() {
        return sleepyness > bedtime;
    }

    /**
     * Checks if the monkey has a family.
     * 
     * @return
     */
    public boolean hasFamily() {
        return (family != null);
    }

    /**
     * A function to join all monkeyfamily related acitons
     * 
     * @param world
     */
    public void handleFamily(World world) {
        if (!validateLocationExistence(world))
            return;
        if (!hasFamily() && isAdult()) {
            return;
        }
        if (isAdult()) {
            leaveFamily();
        }
        if (hasFamily()) {
            if (getFamily().getSize() <= 1) { // if you are in a 1 size family just leave
                getFamily().removeMonkey(this);
                leaveFamily();
            }
        }
        if (!hasFamily()) { // if you dont have a family find one
            searchForFamily(world);
        }
    }

    /**
     * Searches nearby tiles for monkeys and creates a family with them if the
     * conditions are right.
     * 
     * @param world
     */
    public void searchForFamily(World world) {
        if (isAdult()) {
            return;
        }
        for (Location l : world.getSurroundingTiles()) {
            Object o = world.getTile(l);
            if (o instanceof Monkey) {
                if (((Monkey) o).hasFamily()) {// if the target monkey has a family
                    if (((Monkey) o).getFamily().hasSpace()) // if there is space
                        joinFamily(((Monkey) o).getFamily());
                } else { // if the target monkey does not have a family
                    createFamily(world);
                    ((Monkey) o).joinFamily(family);
                }
            }
        }
    }

    /**
     * Creates a family and joins it.
     * 
     * @param world
     */
    public void createFamily(World world) {
        family = new MonkeyFamily();
        joinFamily(family);
    }

    /**
     * Join a family.
     * 
     * @param family
     */
    public void joinFamily(MonkeyFamily family) {
        this.family = family;
        family.addMonkey(this);
    }

    /**
     * Leaves the family the monkey is in.
     */
    public void leaveFamily() {
        if (family == null) {
            return;
        }
        family.removeMonkey(this);
        System.out.println("left family");
        this.family = null;
    }

    /**
     * Eats the contents of a trap if there is any.
     * 
     * @param world
     * @param trapLocation the location of the trap
     */
    public void claimTrap(World world) {
        Trap trap = ((Trap) world.getNonBlocking(trapLocation));
        ((TrapActivated) trap).claim(world);
        changeEnergy(10, world);
        trapLocation = null;
    }

    /**
     * Returns the family of the monkey.
     * 
     * @return MonkeyFamily, null if the monkey does not have a family.
     */
    public MonkeyFamily getFamily() {
        return family;
    }

    /**
     * Builds a trap for rabbits to fall into.
     * 
     * @param world
     */
    public void buildTrap(World world) {
        world.setTile(world.getLocation(this), new Trap());
        hasSticks = false;
        hasBerries = false;
        trapLocation = world.getLocation(this);
    }

    /**
     * Finds and eats food.
     * 
     * @param world
     */
    public void findAndEatFood(World world) {
        if (foodLocation == null) {
            findFood(world);
            return;
        }
        if (world.getTile(foodLocation) instanceof Bush) {
            foodLocation = null;
            return;
        }
        move(world, toAndFrom(world, foodLocation, world.getLocation(this)));
        if (world.getTile(foodLocation) instanceof BerryBush) {
            BerryBush BerryBush = (BerryBush) world.getTile(foodLocation);
            BerryBush.setNoBerries(world);
            changeEnergy(4, world);
            hasSticks = true;
            hasBerries = true;
        } else if (world.getTile(foodLocation) instanceof Carcass) {
            Carcass carcass = (Carcass) world.getTile(foodLocation);
            carcass.takeBite();
            changeEnergy(10, world);
        }
    }

    /**
     * Finds the location of food in a three tile radius.
     * 
     * @param world
     */
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
}
