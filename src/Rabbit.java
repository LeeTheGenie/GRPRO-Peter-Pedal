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
        this.maxAge = 70;
        this.energy = 10;
        this.trueMaxEnergy = 10;
        this.maxEnergy = 10;
        this.hole = null;
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7);
        if (age == maxAge || energy == 0)
            try {
                world.delete(this);
            } catch (Exception e) {

            }

        if (world.isNight()) {
            digHole(world);
            try {
                world.remove(this);
            } catch (Exception e) {

            }
        }
        eat(world);
        reproduce(world);
        move(world);
        age++;
    }

    // virker ikke
    public void eat(World world) {
        try {
            Set<Location> tiles = world.getSurroundingTiles();
            for (Location l : tiles) {
                if (world.getTile(l) instanceof Grass) {
                    world.delete(l);
                }
            }
        } catch (Exception e) {

        }
        if (energy < maxEnergy) {
            energy++;
        }
    }

    // Move to a random free location within radius of 1
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

    // makes another rabbit if another rabbit older than 8 days and ha
    public void reproduce(World world) {

        if (energy > 7 && age > 8) {
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
                    }
                }
                energy -= 2;
            } catch (Exception e) {

            }
        }
    }

    // virker ikke
    public void digHole(World world) {
        // sletter kanin og ikke grass / flower :'(
        try {
            world.delete(world.getTile(world.getLocation(this)));
            hole = new RabbitHole();
            world.setTile(world.getCurrentLocation(), hole);
        } catch (Exception e) {

        }
    }
}
