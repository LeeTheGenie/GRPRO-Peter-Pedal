import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Plant extends LivingBeing implements NonBlocking {

    int reproductiveCooldown;

    Plant(int age, int maxAge,int reproductiveCooldown) {
        super(age, maxAge);
        this.reproductiveCooldown = reproductiveCooldown;
    }

    @Override public void act(World world) {
        reproductiveCooldown--;
        if(reproductiveCooldown<=0)
            spread(world);
        ageUp(world);
    }
    
    public Plant CreateNew(){
        return new Plant(age, maxAge, reproductiveCooldown);
    }

    public void spread(World world) {
        try {
            // Get surrounding tiles
            Set<Location> neighbors = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbors);

            // take one random surrounding tile
            Random r = new Random();
            int randomLocation = r.nextInt(list.size());
            Location newLocation = list.get(randomLocation);

            // create a new instance of plant and put it on the world
            world.setTile(newLocation,CreateNew());
        } catch (Exception e) {
            // There are no possible spaces to move to
        }
        reproductiveCooldown = 10; // reset the spread timer
    }
}
