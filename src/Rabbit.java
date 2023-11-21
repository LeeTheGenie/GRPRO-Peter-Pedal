import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Rabbit implements Actor {

    private int age;
    private int maxAge;
    private int energy;
    private int maxEnergy;
    private int trueMaxEnergy;
    private RabbitHole hole;

    public Rabbit() {
        this.age = 0;
        this.energy = 10;
        this.trueMaxEnergy = 10;
        this.maxEnergy = 10;
        this.hole = null;
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7);

        if (energy == 0) {
            try {
                world.delete(this);
                System.out.println("død");
            } catch (Exception e) {

            }
        }
        eat(world);
        reproduce(world);
        move(world);
        age++;
    }

    public void eat(World world) {
        try {
            Set<Location> tiles = world.getSurroundingTiles(world.getLocation(this));
            for (Location l : tiles) {
                System.out.println("spis");
                if (world.getTile(l) instanceof Grass) {
                    world.delete(l);
                    System.out.println("spis");
                }
            }
        } catch (Exception e) {

        }

        if (energy < maxEnergy) {
            energy++;
        }

    }

    public void move(World world) {
        if (energy > 0) {
            try {
                Set<Location> neighbors = world.getEmptySurroundingTiles();
                List<Location> list = new ArrayList<>(neighbors);
                Random r = new Random();

                int randomLocation = r.nextInt(list.size());
                Location newLocation = list.get(randomLocation);

                world.move(this, newLocation);
            } catch (Exception e) {

            }
        }
        energy--;
    }

    public void reproduce(World world) {
        if (energy > 9) {
            Set<Location> tiles = world.getSurroundingTiles(world.getLocation(this));
            for (Location l : tiles) {
                if (world.getTile(l) instanceof Rabbit) {
                    try {
                        // Get surrounding tiles
                        Set<Location> neighbors = world.getEmptySurroundingTiles();
                        List<Location> list = new ArrayList<>(neighbors);

                        // take one random surrounding tile
                        Random r = new Random();
                        int randomLocation = r.nextInt(list.size());
                        Location newLocation = list.get(randomLocation);

                        // create a new instance of Rabbit and put it on the world
                        world.setTile(newLocation, new Rabbit());
                    } catch (Exception e) {
                        // There are no possible spaces to move to
                    }
                }
            }

        }
        energy -= 2;
    }
}
