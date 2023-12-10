package animal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Plant;
import abstracts.Predator;
import abstracts.Animal;

import executable.DisplayInformation;

import itumulator.world.Location;
import itumulator.world.World;

import misc.RabbitHole;
import misc.WolfHole;
import misc.Carcass;

public class Wolf extends Predator {

    private Location target;
    private Location carcass;
    private WolfPack pack;

    // Sleeping
    private int sleepyness;

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.80d);
        this.target = null;
        this.pack = null;
        growthStates = new String[][] { { "wolf-small", "wolf-small-sleeping" }, { "wolf", "wolf-sleeping" } };
        sleepyness = 0;
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = isMature() ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {
        if (!sleeping) {
            handleMovement(world);
        }
        handleSleep(world);
        super.act(world);
    }

    @Override
    public boolean canEat(World world, LivingBeing livingBeing) {
        if (livingBeing instanceof Bear) {
            if (getPack().getWolfPackSize() < 3) {
                return false;
            }
        }
        return true;

    }

    public void findTarget(World world) {
        Set<Location> tiles = world.getSurroundingTiles(3);
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Carcass) {
                carcass = l;
            }
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Bear) {

                target = l;
            }
        }
    }

    /**
     * A function to join all movement for wolfs together.
     * 
     * @param world
     */
    public void handleMovement(World world) {
        if (target == null) {
            findTarget(world);
            if (target == null) {
                move(world, null);
            }
            System.out.println(target);
        }
        if (target != null) {
            move(world, toAndFrom(world, target,
                    world.getLocation(this)));
            killTarget(world);
            target = null;
        }

        if (pack != null) { // if you are lonely go find friends
            if (!wolfNearby(world, 1) && wolfNearby(world, 3)) {
                moveCloser(world);
                // er not sure på det her.

            }
            // if none go wander around
            move(world, null);
        }
    }

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world) {
        if (sleeping) {
            if (sleepyness < 10) {
                setSleeping(false);
            }
            sleepyness -= 10;
        } else {
            sleepyness += 1;
            if (sleepyness <= 0) {
                die(world, "sleep-exhaustion");
            }
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
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

    /**
     * nicky please explain what this does
     * 
     * @param world
     */
    public void moveCloser(World world) {
        Set<Location> tiles = world.getSurroundingTiles(3);
        Location wolfLocation = world.getLocation(this);
        Location newLocation = null;
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                newLocation = toAndFrom(world, l, wolfLocation);
            }
        }
        move(world, newLocation);
    }

    public void killTarget(World world) {
        System.out.println("Kill");
        if (canEat(world, (LivingBeing) world.getTile(target))) {
            ((LivingBeing) world.getTile(target)).die(world, "wolf");
        }
    }

    public void eatTarget(World world) {
        move(world, carcass);
        ((Carcass) world.getTile(carcass)).takeBite(world);
    }

    public void createPack(World world) {
        pack = new WolfPack();
        Wolf wolf = getWolfNearby(world);
        pack.addWolf(this);
        pack.addWolf(wolf);
        this.setPack(pack);
        wolf.setPack(pack);
    }

    public void joinPack(World world) {
        LivingBeing wolf = locateTarget(world, 1);
        ((Wolf) wolf).getPack().addWolf(this);
    }

    public WolfPack getPack() {
        return pack;
    }

    public boolean hasPack(World world, List<Location> wolfs) {
        for (Location l : wolfs) {
            Wolf wolf = (Wolf) world.getTile(l);
            if (wolf.getPack() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPack() {
        return pack != null;
    }

    public boolean wantToSleep() {
        return sleepyness > 60;
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

    /**
     * Atempts to exit the hole the rabbit is in;
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

    public boolean packFull(World world) {
        LivingBeing wolf = locateTarget(world, 1);
        if (((Wolf) wolf).getPack().getWolfPackSize() < 6) {
            return false;
        }
        return true;
    }

    public boolean targetNearby(World world) {
        for (Location l : world.getSurroundingTiles(3)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf
                    || world.getTile(l) instanceof Bear) {
                // target = (LivingBeing) world.getTile(l);
                return true;
            }
        }
        return false;
    }

    public void setPack(WolfPack pack) {
        this.pack = pack;
    }
}
