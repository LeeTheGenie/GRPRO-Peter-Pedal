package animal;

import itumulator.world.World;
import misc.RabbitHole;
import itumulator.world.Location;

import java.util.Set;

import abstracts.Animal;
import abstracts.Plant;
import executable.DisplayInformation;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Rabbit extends Animal {

    private RabbitHole rabbithole;
    private int holeDigCost;
    private Set<Location> dangerTiles;
    private Set<Location> safeTiles;

    public Rabbit() {
        super(0, 70, 40, 10, 1, 15, 0, 2);
        this.rabbithole = null;
        this.dangerTiles = new HashSet<>();
        this.safeTiles = new HashSet<>();
        sleeping = false;
        holeDigCost = 10;
        growthStates = new String[][] { { "rabbit-small", "rabbit-small-sleeping" },
                { "rabbit-large", "rabbit-sleeping" } };
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = (matureAge <= age) ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public Rabbit newInstance() {
        return new Rabbit();
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy

        if (world.isNight()) {
            sleeping = true;
            if (rabbithole == null) {
                digHole(world);
            } else {
                locateHole(world);
            }
            if (!resting)
                enterHole(world);

        } else {
            sleeping = false;
            // exit hole
            eat(world);

            if (wolfNearby(world, 4)) {
                flee(world);
            } else {
                move(world, null);
            }
            reproduce(world);
            if (resting)
                exitHole(world);
        }

        super.act(world);
    }

    /**
     * Sets the danger tiles to the tiles around wolfs
     * 
     * @param world
     */
    public void setDangerTiles(World world) {
        List<Location> wolfs = getNearbyWolfs(world, 4);
        for (Location l : wolfs) {
            Set<Location> wolfTiles = world.getSurroundingTiles(l, 3);
            for (Location wt : wolfTiles) {
                dangerTiles.add(wt);
            }
        }
    }

    /**
     * Sets the safe tiles to the tiles around the danger tiles
     * 
     * @param world
     */
    public void setSafeTiles(World world) {
        Set<Location> tiles = world.getSurroundingTiles(3);
        for (Location l : tiles) {
            if (!dangerTiles.contains(l)) {
                safeTiles.add(l);
            }
        }
    }

    /**
     * Returns the safe tiles
     * 
     * @return
     */
    public Set<Location> getSafeTiles() {
        return safeTiles;
    }

    /**
     * Returns a random safe tile
     * 
     * @param world
     * @return random safe location
     */
    public Location getRandomSafeTile(World world) {
        setDangerTiles(world);
        setSafeTiles(world);
        Set<Location> tiles = getSafeTiles();
        List<Location> list = new ArrayList<>(tiles);
        Random r = new Random();
        if (list.size() == 0) {
            return null;
        }
        int randomIndex = r.nextInt(list.size());
        Location randomLocation = list.get(randomIndex);
        return randomLocation;
    }

    /**
     * Moves the rabbit to a random safe tile
     * 
     * @param world
     */
    public void flee(World world) {
        Location randomTile = getRandomSafeTile(world);
        if (randomTile == null)
            return;
        move(world, toAndFrom(world, getRandomSafeTile(world), world.getLocation(this)));
    }

    /**
     * Eats plants underneath it
     * 
     * @param world
     */
    public void eat(World world) {
        int energyIncrement = 8; // hvor meget energi man får

        // Failstates
        if (currentEnergy == maxEnergy)
            return;
        if (!world.isOnTile(this))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        if (world.getNonBlocking(world.getLocation(this)) instanceof Plant) { // er det en plant
            world.delete(world.getNonBlocking(world.getLocation(this))); // slet den plant
            changeEnergy(energyIncrement, world);
        }
    }

    @Override
    public void onDeath(World world) {
        if (this.rabbithole != null)
            rabbithole.clearOwner();
        super.onDeath(world);
    }

    /**
     * locateHole() moves 1 step to the rabbits hole.
     * 
     * @param world
     */
    public void locateHole(World world) {
        if (rabbithole == null)
            return;
        if (!world.isOnTile(this))
            return;

        Location rabbitLocation = world.getLocation(this);
        Location holeLocation = world.getLocation(rabbithole);

        move(world, toAndFrom(world, holeLocation, rabbitLocation));
    }

    /**
     * Births another rabbit if there is another rabbit within radius of 1 and
     * energy
     * 
     * @param world
     */
    public void reproduce(World world) {
        // Failstates
        if (matureAge > age)
            return;
        if (!canAfford(reproductionCost))
            return;
        if (!world.isOnTile(this))
            return;

        // Main
        Set<Location> surroundingTiles = world.getSurroundingTiles(world.getLocation(this));
        Boolean foundMate = false;
        for (Location l : surroundingTiles) {
            if (!(world.getTile(l) instanceof Rabbit))
                continue;
            foundMate = true;
        }

        if (!foundMate)
            return;

        // Get surrounding tiles
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

        if (list.size() == 0)
            return;

        Location newLocation = list.get(new Random().nextInt(list.size()));

        // create a new instance of Rabbit and put it on the world
        Rabbit baby = new Rabbit();
        baby.setBaby();
        world.setTile(newLocation, baby);

        currentEnergy -= reproductionCost;
    }

    /**
     * Digs a hole at current location
     * 
     * @param world
     */
    public void digHole(World world) {
        if (!canAfford(holeDigCost))
            return;
        if (!world.isOnTile(this))
            return;

        Location rabbitLocation = world.getLocation(this);

        eat(world);
        if (world.containsNonBlocking(rabbitLocation))
            return;
        rabbithole = new RabbitHole(this);
        changeEnergy(holeDigCost, world);

        world.setTile(rabbitLocation, rabbithole);
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

        if (!(objectUnderneath instanceof RabbitHole))
            return;
        // Sucess
        RabbitHole rabbitHoleUnderneath = (RabbitHole) objectUnderneath;

        if (!rabbitHoleUnderneath.isClaimed()) {
            // System.out.println(this+" trying to claim: "+rabbitHoleUnderneath+" with
            // owner: "+rabbitHoleUnderneath.getOwner());
            try {
                rabbitHoleUnderneath.setOwner(this);
                rabbithole = rabbitHoleUnderneath;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (objectUnderneath.equals(rabbithole)) {
            world.remove(this);
            resting = true;

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
        if (rabbithole == null)
            return;
        if (!world.isOnTile(rabbithole))
            return;

        Location exitLocation = world.getLocation(rabbithole);
        if (!world.isTileEmpty(exitLocation))
            return;

        // System.out.println("Go to tile:" + exitLocation);
        world.setTile(exitLocation, this);
        resting = false;
    }

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

    public Wolf getWolfNearby(World world) {
        Wolf wolf = null;
        if (world.isOnTile(this)) {
            Set<Location> tiles = world.getSurroundingTiles(1);
            for (Location l : tiles) {
                if (world.getTile(l) instanceof Wolf) {
                    return (Wolf) world.getTile(l);
                }
            }
        }
        return wolf;
    }

    public boolean wolfNearby(World world, int radius) {
        if (world.isOnTile(this)) {
            Set<Location> tiles = world.getSurroundingTiles(radius);
            for (Location l : tiles) {
                if (world.getTile(l) instanceof Wolf) {
                    return true;
                }
            }
        }
        return false;
    }
}
