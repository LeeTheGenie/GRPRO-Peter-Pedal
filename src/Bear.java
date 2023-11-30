import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;

import abstracts.Animal;

public class Bear extends Animal {
    private List<Location> territory;
    private Location centerPoint;

    @Override
    public Rabbit newInstance() {
        return new Rabbit();
    }

    public Bear(int age, int maxAge, int maxEnergy, Location centerPoint) {
        super(age, maxAge, maxEnergy);
        this.territory = new ArrayList<>();
        this.centerPoint = centerPoint;
    }

    public void act(World world) {
        getTerritory(world);
        moveInTerritory(world);
    }

    public List<Location> getTerritory(World world) {
        Set<Location> tiles = world.getSurroundingTiles(centerPoint, 3);
        for (Location l : tiles) {
            territory.add(l);
        }
        return territory;
    }

    public void moveInTerritory(World world) {
        Random r = new Random();
        int x = r.nextInt(territory.size());
        int y = r.nextInt(territory.size());
        Location moveTo = new Location(x, y);
        world.move(this, moveTo);

    }
}
