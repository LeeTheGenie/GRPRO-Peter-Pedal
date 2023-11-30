import itumulator.world.World;
import itumulator.world.Location;

import java.util.Set;

import abstracts.Animal;
import abstracts.Plant;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import abstracts.Animal;

public class Rabbit extends Animal {

    private Location rabbithole;
    //private boolean dig = false;

   

    public Rabbit() {
        super(0, 70, 30,10,1,10,30);
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
        if (currentEnergy == maxEnergy) // hvis du ikke gavner af at spise så lad vær
            return;
        try {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Plant) { // er det en plant
                world.delete(world.getNonBlocking(world.getLocation(this))); // slet den plant

                currentEnergy += energyIncrement;
                if (currentEnergy > maxEnergy) // hvis den er større end max, bare set den til max fordi det er max duh
                    currentEnergy = maxEnergy;
            }
        } catch (IllegalArgumentException e) { // if the current tile does not have a nonblocking it returns
                                               // IllegalArgumentException
            // System.out.println(e.getMessage());
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


        try {
            Set<Location> tiles = world.getSurroundingTiles(world.getLocation(this));
            
            for (Location l : tiles) {
                if (world.getTile(l) instanceof Rabbit) {

                    // Get surrounding tiles
                    Set<Location> neighbors = world.getEmptySurroundingTiles();
                    List<Location> list = new ArrayList<>(neighbors);

                    // take one random surrounding tile
                    Random r = new Random();
                    int randomLocation = r.nextInt(list.size());
                    Location newLocation = list.get(randomLocation);

                    // create a new instance of Rabbit and put it on the world
                    world.setTile(newLocation, new Rabbit());
                    System.out.println("Baby");
                }
            }
            currentEnergy -= 2;
        } catch (Exception e) {

        }
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
                world.move(this, rabbithole);
            } catch (Exception e) {

            }
        }
    }
}
