package animal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Plant;
import abstracts.Predator;

import executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import misc.WolfHole;
import misc.Carcass;

public class Wolf extends Predator {

    private LivingBeing target;
    private WolfPack wolfPack;
    private boolean alpha;
    private Set<Location> alpharange;

    // Sleeping
    private int sleepyness;
    private int bedtime;

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.80d);
        this.target = null;
        this.wolfPack = null;
        this.alpha = false;
        growthStates = new String[][] { { "wolf-small", "wolf-small-sleeping" }, { "wolf", "wolf-sleeping" } };
        this.sleepyness = 0;
        this.bedtime = 60;
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = isMature() ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {
        // System.out.println("I "+this+" am in "+wolfPack);
        if (getAlphaWolf(world) == null) { // sets the alpha wolf
            handlePack(world);
            giveOneWolfAlpha(world);
            // alpharange =
            // world.getSurroundingTiles(world.getLocation(getAlphaWolf(world)), 4); // sets
            // the initial alpha
            // range, will change
            // each step
        }
        if (!sleeping) {
            handlePack(world);
            handleGoSleep(world);
            hunt(world);
        }
        handleSleep(world);
        reproduce(world);

        super.act(world);
    }

    /**
     * A function to join all movement for wolfs together.
     * 
     * @param world
     */
    public void handleGoSleep(World world) {
        if (!world.isOnTile(this))
            return;
        if (wantToSleep()) { // if you want to sleep go to sleep
            if (hasPack()) {
                // if wolf has a pack
                WolfHole wolfHole = getPack().getWolfHole();
                if (wolfHole != null) {
                    // has a hole - go towards it
                    if (world.isOnTile(wolfHole)) {
                        if (world.getLocation(wolfHole).equals(world.getLocation(this))) {
                            enterHole(world);
                            setSleeping(true);
                            return;
                        }
                        move(world, toAndFrom(world, world.getLocation(wolfHole), world.getLocation(this)));
                        return;
                    }
                } else {
                    // does nst have a hole - dig a hole.
                    digHole(world);
                    return;
                }
            } else {
                // if wolf does not have a pack just sleep where you are
                setSleeping(true);
                return;
            }
        }

    }

    /**
     * 
     * For alpha wolf, it will try to find a target and kill it.
     * For non-alpha wolf, it will try to find the alpha wolf and follow it.
     * 
     * @param world
     */
    public void hunt(World world) {
        if (!world.isOnTile(this)) {
            return;
        }
        if (isAlpha()) { // if alpha wolf
            if (isHungry()) { // if hungry
                findTarget(world, 3); // find target in range 3
                if (target == null) { // if no target
                    move(world, null); // move randomly
                }
                if (target != null) { // if target
                    move(world, toAndFrom(world, world.getLocation(target), world.getLocation(this))); // move towards
                                                                                                       // target
                    if (!(target instanceof Bear)) {
                        killTarget(world); // kill target
                        target = null; // reset target
                    } else {
                        groupOnBear(world);
                        target = null;
                    }

                }
            }
            if (!isHungry()) { // if not hungry
                move(world, null); // move randomly
            }
        }
        if (!isAlpha() && !hasPack()) { // if not alpha and does not have a pack
            move(world, null); // move randomly
        } else if (world.isOnTile(getAlphaWolf(world)) && world.isOnTile(this) && !isAlpha() && hasPack()) { // if not
                                                                                                             // alpha
                                                                                                             // and has
                                                                                                             // a pack
                                                                                                             // and the
                                                                                                             // alphawolf
                                                                                                             // and this
                                                                                                             // wolf is
                                                                                                             // shown in
                                                                                                             // the
                                                                                                             // world
            alpharange = world.getSurroundingTiles(world.getLocation(getAlphaWolf(world)), 3); // set the alpha range to
                                                                                               // the alphawolf
            followAlpha(world); // follow the alpha
        }
    }

    public void groupOnBear(World world) {
        if (getPack().getSize() < 4) {
            return;
        }
        if (getPack().getSize() >= 4) {
            move(world, toAndFrom(world, world.getLocation(target), world.getLocation(this)));
            // if mere end to ulve ved siden af bear så dræb bear, hvis pack size under 4 så
            // flee
            killTarget(world);
        }
    }

    /**
     * Finds a target within the given radius
     * 
     * @param world
     * @param radius to search within
     */
    public void findTarget(World world, int radius) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(radius);
        for (Location l : surroundingTiles) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Bear) {
                target = (LivingBeing) world.getTile(l);
                return;
            }
        }
    }

    /**
     * Goes to a random empty tile within the alpha wolf's range, if the tile is not
     * within the alpha wolf's range, it will go to the alpha wolf.
     * 
     * @param world
     */
    public void followAlpha(World world) {
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(); // get all empty tiles
        List<Location> list = new ArrayList<>(emptyTiles); // convert to list

        Random r = new Random(); // random number generator

        int randomIndex = r.nextInt(emptyTiles.size()); // get random index
        Location randomLocation = list.get(randomIndex); // get random location
        if (alpharange.contains(randomLocation)) { // if the random location is within the alpha range
            move(world, randomLocation); // move to the random location
        } else {
            move(world, toAndFrom(world, world.getLocation(getAlphaWolf(world)), randomLocation)); // move towards the
                                                                                                   // alpha
        }

    }

    /**
     * Check for if criteria to eat is met
     * 
     * @param world
     * @return true is criteria is met, false if not
     */
    public boolean canEat(World world) { // not implemented yet.
        return true;
    }

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world) {
        if (sleeping) {
            if (sleepyness < 10) {
                exitHole(world);
            }
            sleepyness -= 10;

        } else {
            sleepyness += 1;
            if (sleepyness >= 100) {
                die(world, "sleep-exhaustion");
            }
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
        this.resting = sleeping;
    }

    /**
     * Gives the first wolf in the pack the alpha status
     * 
     * @param world
     */
    public void giveOneWolfAlpha(World world) {
        List<Wolf> wolfList = wolfPack.getWolfList();
        wolfList.get(0).setAlpha();
    }

    /**
     * Sets the wolf to alpha
     */
    public void setAlpha() {
        this.alpha = true;
    }

    /**
     * Checks if the wolf is alpha
     * 
     * @return true if alpha, false if not
     */
    public boolean isAlpha() {
        return alpha;
    }

    /**
     * Finds the alphawolf in the pack
     * 
     * @param world
     * @return alphawolf if found, null if not
     */
    public Wolf getAlphaWolf(World world) {
        if (world.isOnTile(this)) {
            for (Wolf w : wolfPack.getWolfList()) {
                if (w.isAlpha()) {
                    return w;
                }
            }
        }

        return null;
    }

    public void digHole(World world) {
        int holeDigCost = 8;
        if (!canAfford(holeDigCost))
            return;
        if (!world.isOnTile(this))
            return;

        Location wolfLocation = world.getLocation(this);

        try {
            if (world.getNonBlocking(wolfLocation) instanceof Plant) {
                ((Plant) world.getNonBlocking(wolfLocation)).die(world);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (world.containsNonBlocking(wolfLocation))
            return;

        WolfHole wolfHole = new WolfHole();
        wolfHole.setOwner(this.getPack());
        this.getPack().setWolfHole(wolfHole);
        changeEnergy(holeDigCost, world);
        world.setTile(wolfLocation, wolfHole);
    }

    public boolean wantToSleep() {
        return sleepyness > bedtime;
    }

    public boolean isHungry() {
        return maxEnergy * hungerFactor < currentEnergy;
    }

    /**
     * Tries to enter a hole underneath it.
     * Claims a hole if it encounters a empty hole
     * 
     * @param world
     */
    public void enterHole(World world) {
        if (!world.isOnTile(this))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        Object objectUnderneath = world.getNonBlocking(world.getLocation(this));

        if (!(objectUnderneath instanceof WolfHole))
            return;
        // Sucess
        WolfHole wolfHoleUnderneath = (WolfHole) objectUnderneath;

        if (objectUnderneath.equals(this.getPack().getWolfHole())) {
            world.remove(this);
            setSleeping(true);

            // System.out.println("Rabbit(s) resting: " + this.rabbits);
            // System.out.println(" -In " + world.getTile(world.getLocation(this)));
        }
    }

    public void reproduce(World world) {
        // Failstates
        if (matureAge > age)
            return;
        if (!canAfford(reproductionCost))
            return;
        if (this.validateLocationExistence(world))
            return;

        // Main

        if (this.wolfPack.hasSpace() && this.wolfPack.getSize() > 1) {
            // Check if more than one wolf is in hole
            int wolvesInHole = 0;
            for (Wolf w : this.wolfPack.getWolfList()) {
                if (!w.validateLocationExistence(world)) {
                    wolvesInHole = wolvesInHole + 1;
                }
            }
            if (this.wolfPack.inHeat()) {
                if (wolvesInHole >= 2) {
                    if (this.wolfPack.getSize() == 4) {
                        System.out.println("make 2 babe");
                        this.makeBaby(world);
                        this.makeBaby(world);
                        this.wolfPack.postNutClarity();
                    }
                    if (this.wolfPack.getSize() < 4) {
                        this.makeBaby(world);
                        this.wolfPack.postNutClarity();
                    }
                }
            }

            else {
                this.wolfPack.getHorny();
            }
        }
    }

    public void makeBaby(World world) {
        world.setCurrentLocation(world.getLocation(wolfPack.getWolfHole()));
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

        if (list.size() == 0)
            return;

        Location newLocation = list.get(new Random().nextInt(list.size()));
        Wolf baby = new Wolf();
        wolfPack.addWolf(baby);
        int tired = baby.bedtime;
        baby.sleepyness = baby.sleepyness + tired;
        world.setTile(newLocation, baby);
        changeEnergy(-reproductionCost, world);
    }

    /**
     * Atempts to exit the hole the wolf is in;
     * 
     * @param world
     * 
     */
    public void exitHole(World world) {
        if (world.isOnTile(this))
            return;
        if (this.getPack().getWolfHole() == null)
            return;
        if (!world.isOnTile(this.getPack().getWolfHole()))
            return;

        Location exitLocation = world.getLocation(this.getPack().getWolfHole());
        if (!world.isTileEmpty(exitLocation))
            return;

        // System.out.println("Go to tile:" + exitLocation);
        world.setTile(exitLocation, this);
        setSleeping(false);
    }
    /*
     * $$\
     * $$ |
     * $$$$$$\ $$$$$$\ $$$$$$$\ $$ | $$\
     * $$ __$$\ \____$$\ $$ _____|$$ | $$ |
     * $$ / $$ | $$$$$$$ |$$ / $$$$$$ /
     * $$ | $$ |$$ __$$ |$$ | $$ _$$<
     * $$$$$$$ |\$$$$$$$ |\$$$$$$$\ $$ | \$$\
     * $$ ____/ \_______| \_______|\__| \__|
     * $$ |
     * $$ |
     * \__| 'øhh cringe much?'
     */

    /**
     * A function to join all wolfpack related acitons
     * 
     * @param world
     */
    public void handlePack(World world) {
        if (!validateLocationExistence(world))
            return;
        if (hasPack()) {
            if (getPack().getSize() <= 1) { // if you are in a 1 size pack just leave
                getPack().removeWolf(this);
                leavePack();
            }
        }
        if (!hasPack()) { // if you dont have a pack find one
            searchForPack(world);
        }
    }

    /**
     * Searches nearby tiles for wolfs and creates a pack with them if the
     * conditions are right.
     * 
     * @param world
     */
    public void searchForPack(World world) {
        for (Location l : world.getSurroundingTiles()) {
            Object o = world.getTile(l);
            if (o instanceof Wolf) {
                if (((Wolf) o).hasPack()) {// if the target wolf has a pack
                    if (((Wolf) o).getPack().hasSpace()) // if there is space
                        joinPack(((Wolf) o).getPack());
                } else { // if the target wolf does not have a pack
                    createPack(world);
                    joinPack(wolfPack);
                    ((Wolf) o).joinPack(wolfPack);
                }
            }
        }
    }

    public void createPack(World world) {
        wolfPack = new WolfPack();
    }

    public void joinPack(WolfPack wolfPack) {
        // System.out.println("I "+this+" joined "+wolfPack);
        this.wolfPack = wolfPack;
        wolfPack.addWolf(this);
    }

    public void leavePack() {
        // System.out.println("I "+this+" left "+wolfPack);
        wolfPack.removeWolf(this);
        this.wolfPack = null;
    }

    public WolfPack getPack() {
        return wolfPack;
    }

    public boolean hasPack() {
        return wolfPack != null;
    }

}
