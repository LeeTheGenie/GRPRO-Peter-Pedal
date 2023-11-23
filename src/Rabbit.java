import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Rabbit extends Animal {
    private RabbitHole hole;

    public Rabbit() {
        super(0, 70, 30);
        this.hole = null;
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy
        /*
         * if (world.isNight()) {
         * digHole(world);
         * try {
         * world.remove(this);
         * } catch (Exception e) {
         * 
         * }
         * }
         */

        eat(world);
        //reproduce(world);
        move(world);
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
            System.out.println(e.getMessage());
        }
    }

    // Move to a random free location within radius of 1, costs 1 energy
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
        }
        currentEnergy--;
    }

    // makes another rabbit if there is another rabbit within radius of 1 and energy
    // is greater than 7 and age greater than 8 costs 2 energy
    public void reproduce(World world) {

        if (currentEnergy > 7 && age > 8) {
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
                currentEnergy -= 2;
            } catch (Exception e) {

            }
        }
    }

    // digs a hole and enters it when it is night
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
