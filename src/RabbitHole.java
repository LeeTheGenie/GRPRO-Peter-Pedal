
import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;

import javax.naming.event.ObjectChangeListener;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class RabbitHole extends LivingBeing implements NonBlocking {

    ArrayList<Object> rabbits;

    @Override
    public void act(World world) {
        keepRabbit(world);
        exitRabbit(world);
    }

    @Override
    public RabbitHole newInstance() {
        return new RabbitHole();
    }

    RabbitHole() {
        super(0, 0);
        this.rabbits = new ArrayList<>();
    }

    public void keepRabbit(World world) {
        if (world.isNight() == true) {
            try {
                if (world.getTile(world.getLocation(this)) instanceof Rabbit) {

                    Object rabbit = world.getTile(world.getLocation(this));

                    this.rabbits.add(rabbit);
                    world.remove(rabbit);

                    System.out.println("Rabbit(s) resting: " + this.rabbits);
                    System.out.println(" -In " + world.getTile(world.getLocation(this)));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    public void exitRabbit(World world) {

        if (world.isDay() == true) {
            try {
                if (this.rabbits.size() > 0) {
                    for (Object rabbit : rabbits) {

                        Set<Location> neighbors = world.getEmptySurroundingTiles();
                        List<Location> list = new ArrayList<>(neighbors);
                        Random r = new Random();
                        int randomLocation = r.nextInt(list.size());

                        System.out.println(rabbit + " left " + this + "\n -Located at: " + world.getCurrentLocation());
                        this.rabbits.remove(rabbit);

                        Location newLocation = list.get(randomLocation);

                        world.setCurrentLocation(newLocation);
                        System.out.println(" -Go to tile:" + world.getCurrentLocation());
                        world.setTile(newLocation, rabbit);

                    }

                }
            } catch (java.util.ConcurrentModificationException e) {

            }
        }
    }
}
