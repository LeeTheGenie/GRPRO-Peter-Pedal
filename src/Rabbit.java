import itumulator.world.World;
import itumulator.world.Location;

import java.util.Set;

import abstracts.Animal;
import abstracts.Plant;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Rabbit extends Animal {

    private Location rabbithole;

    public Rabbit() {
        super(0, 70, 40,10,1,15,0,2);
        this.rabbithole = null;
    }

    @Override public Rabbit newInstance() {
        return new Rabbit();
    }

    @Override public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy

        if (world.isNight()) {
            findHole(world);
            digHole(world);
            //gotoHole(world);
        }

        if (world.isDay()) {
            eat(world);
            reproduce(world);
            move(world);
        }

        super.act(world); // age up & check for if energy == 0
    }

    public void eat(World world) { // spiser på den tile den står på
        int energyIncrement = 8; // hvor meget energi man får

        // Failstates
        if (currentEnergy == maxEnergy)
            return;
        if(!world.isOnTile(this))
            return;
        
        if (world.getNonBlocking(world.getLocation(this)) instanceof Plant) { // er det en plant
            world.delete(world.getNonBlocking(world.getLocation(this))); // slet den plant
            changeEnergy(energyIncrement, world);
        }
    }

    /**
     * Move to a random free location within radius of 1, costs 1 energy
     * @param world
     */
    public void move(World world) {
        if (currentEnergy > 0) {
            try {
                Set<Location> neighbors = world.getEmptySurroundingTiles();
                List<Location> list = new ArrayList<>(neighbors);
                Random r = new Random();

                int randomLocation = r.nextInt(list.size());
                Location newLocation = list.get(randomLocation);

                world.move(this, newLocation);
            } catch (Exception e) {

            }
            currentEnergy--;
        }

    }


    /**
     * Births another rabbit if there is another rabbit within radius of 1 and energy
     * @param world
     */
    // 
    public void reproduce(World world) {
        // Failstates
        if(matureAge>age) 
            return;
        if(!canAfford(reproductionCost))
            return;
        if(!world.isOnTile(this))
            return;

        // Main
        Set<Location> surroundingTiles = world.getSurroundingTiles(world.getLocation(this));
        Boolean foundMate = false;
        for (Location l : surroundingTiles) {
            if (!(world.getTile(l) instanceof Rabbit)) continue;
            foundMate = true;
        }
        
        if(!foundMate)
            return; 

        // Get surrounding tiles
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

        if(list.size()==0)
            return;

        Location newLocation = list.get(new Random().nextInt(list.size()));

        // create a new instance of Rabbit and put it on the world
        Rabbit baby = new Rabbit();
        baby.setBaby();
        world.setTile(newLocation, baby);

        currentEnergy -= reproductionCost;
    }

    public void digHole(World world) {
        try {
            Location rabbitLocation = world.getLocation(this);
            if (world.isNight() && rabbithole == null) {
                if (world.isNight() && rabbitLocation != null) {
                    this.dig = true;
                    eat(world);
                    world.setTile(rabbitLocation, new RabbitHole());
                    rabbithole = rabbitLocation;
                }
            } else
                this.dig = false;
        } catch (Exception e) {
        }
    }

    public void findHole(World world) {
        try {
            if (rabbithole == null) {
                Set<Location> tiles = world.getSurroundingTiles(2);
                for (Location l : tiles) {
                    try {
                        if (world.getNonBlocking(l) instanceof RabbitHole) {
                            rabbithole = l;
                            this.dig = false;
                            break;
                        } else {
                            this.dig = true;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * MOVE INSTANTLY TO A HOLE :(
     * 
     * 
     * @param world
     */
    public void goInHole(World world) {
        if (rabbithole != null) {
            try {
                this.toAndFrom(world, rabbithole, world.getLocation(this));
            } catch (Exception e) {

            }
        }
    }
}
