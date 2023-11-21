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
        int size = 1;
        int delay = 1000;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();
        Location place = new Location(0, 0);
        Person person = new Person();

        world.setTile(place, person);

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);

        p.show();

        for (int i = 0; i < 200; i++) {
            try {
                if (world.isNight()) {
                    world.remove(person);
                }
            } catch (Exception e) {

            }
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
            world.setTile(l, new Person());
        }

        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Person.class, di);

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
    }
}