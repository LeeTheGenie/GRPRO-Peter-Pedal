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
        int size = 2; //ændre size tilbage
        int delay = 100;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        // Display information
        // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass", true));
        // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow, "flower2", false));
        // Rabbit
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.black, "rabbit-small"));
        // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black, "hole", false));

        // Set grass
        world.setTile(new Location(0, 0), new Rabbit());
        world.setTile(new Location(1, 0), new Rabbit());
        world.setTile(new Location(1,1 ), new RabbitHole());
        //world.setTile(new Location(3, 2), new Flower()); 
        /*for(int i=0;i<8;i++){ //husk at fjern test
            for(int y=0;y<8;y++){
                world.setTile(new Location(i, y), new RabbitHole());}
            }*/
        p.show();

        // set rabbit
       /*   int amount = 10;
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            if (!world.containsNonBlocking(l))
                world.setTile(l, new Grass());
        }*/
 
        // show
        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }
}