package animal;

import itumulator.world.World;
import misc.RabbitHole;
import itumulator.world.Location;

import java.util.Set;

import abstracts.Animal;
import abstracts.Plant;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Rabbit extends Animal {

    private RabbitHole rabbithole;
    int holeDigCost;

    public Rabbit() {
        super(0, 70, 40, 10, 1, 15, 0, 2);
        this.rabbithole = null;
        holeDigCost = 10;
    }

    @Override
    public Rabbit newInstance() {
        return new Rabbit();
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy

        if (world.isNight()) {
            if (rabbithole == null) {
                digHole(world);
            } else {
                locateHole(world);
            }
            if (!resting)
                enterHole(world);

        } else { 
            // exit hole
            eat(world);
            move(world, null);
            reproduce(world);
            if (resting)
                exitHole(world);
        }

        super.act(world);
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
}