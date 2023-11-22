import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;

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
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green,"grass",true));
            // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow,"flower",false));

            // Rabbit
        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Rabbit.class, di);

            // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black,"hole",true));


        // Set grass
        world.setTile(new Location(0, 0), new Grass());
        world.setTile(new Location(size-1, size-1), new Flower());
        world.setTile(new Location(4, 5), new RabbitHole());
        p.show();

        for (int i = 0; i < 3000; i++) {
            p.simulate();
        }
    }

    public static void setupAndRunSimulation2() {
        int size = 15;
        int delay = 1000;
        int display_size = 800;
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