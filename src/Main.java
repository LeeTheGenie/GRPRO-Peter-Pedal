import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Random;
import java.awt.Image;

public class Main {

    public static void main(String[] args) {
        setupAndRunSimulation();
    }

    public static void setupAndRunSimulation() {
        // World Setup
        int size = 5;
        int delay = 1000;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        // Display information
        // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass", true));
        // Rabbit
        DisplayInformation rdi = new DisplayInformation(Color.red, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rdi);

        // Set grass
        world.setTile(new Location(0, 0), new Grass());

        // Set rabbits
        int amount = 10;
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);
            // Så længe pladsen ikke er tom, forsøger vi med en ny tilfældig plads:
            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
            // og herefter kan vi så anvende den:
            world.setTile(l, new Rabbit());
        }

        // Show
        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
    }
}