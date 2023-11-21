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
    private RabbitHole hole;

    public Rabbit() {
        this.age = 0;
        this.maxAge = 70;
        this.energy = 10;
        this.maxEnergy = 10;
        this.hole = null;
    }

    @Override
    public void act(World world) {
        maxEnergy = maxEnergy - (age / 5);
        if (age == maxAge) {
            try {
                world.delete(this);
            } catch (Exception e) {

            }
        }
        if (energy == 0) {
            try {
                world.delete(this);
            } catch (Exception e) {

            }
        }
        eat(world);
        move(world);
        age++;
    }

    public void eat(World world) {
        try {
            if (world.getTile(world.getLocation(this)) instanceof Grass
                    && world.getTile(world.getLocation(this)) instanceof Rabbit) {
                world.delete(world.getTile(world.getLocation(this)));
                System.out.println("hej");
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
        if (energy > 0 && world.getSurroundingTiles() instanceof Rabbit) {
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
                System.out.println("hej2");
            } catch (Exception e) {
                // There are no possible spaces to move to
            }
        }
        energy -= 2;
    }
}
