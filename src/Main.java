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
        Location place = new Location(0, 0);
       
        // Display information
            // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green,"/resources/images/grass.png",true));
            // Rabbit
        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Rabbit.class, di);


        // Tiles + show
        //world.setTile(place, new Grass());
        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
    }

    public static void setupAndRunSimulation2() {
        int size = 15;
        int delay = 1000;
        int display_size = 800;
        int amount = 10;

        Random r = new Random();

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();
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

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Rabbit.class, di);

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
    }
}