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
        DisplayInformation rdi = new DisplayInformation(Color.black, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rdi);

        // RabbitHole
        DisplayInformation rhdi = new DisplayInformation(Color.black, "hole");
        p.setDisplayInformation(RabbitHole.class, rhdi);

        // Set grass
        world.setTile(new Location(0, 0), new Grass());
        world.setTile(new Location(3, 2), new Flower());

        // set rabbit
        int amount = 5;
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
        p.show();

        for (int i = 0; i < 3000; i++) {
            p.simulate();
        }
    }
}