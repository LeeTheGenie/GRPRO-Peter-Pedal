import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        setupAndRunSimulation();
    }

    public static void setupAndRunSimulation() {
        // World Setup
        int size = 8;
        int delay = 1000;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        // Display information
            // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass", true));
            // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow, "flower", false));
            // Rabbit
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.black, "rabbit-small"));
            // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black,"hole",false));

        // Set grass
        world.setTile(new Location(0, 0), new Grass());
        world.setTile(new Location(3, 2), new Flower());
        p.show();

        // set rabbit
        int amount = 20;
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new Rabbit());
        }

        // show
        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }
}