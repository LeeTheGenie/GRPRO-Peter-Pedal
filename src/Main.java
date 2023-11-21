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
        int delay = 100;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        // Display information
        // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass", true));
        // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow, "flower", false));

        // Rabbit
        DisplayInformation di = new DisplayInformation(Color.black, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, di);

        // Set grass
        world.setTile(new Location(0, 0), new Grass());
        world.setTile(new Location(3, 2), new Flower());
        p.show();

        for (int i = 0; i < 3000; i++) {
            p.simulate();
        }
    }
}