import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Person implements Actor {

    @Override
    public void act(World world) {
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
}
