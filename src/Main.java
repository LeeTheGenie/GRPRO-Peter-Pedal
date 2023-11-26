import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Program p = FileLoader("data\\input-filer 2\\test.txt");
            p.show();
            for (int i = 0; i < 300; i++) {
                p.run();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /*
     * Fileloader takes one argument (String fileLocation) and returns a program
     * where in the world of the program, the given file is loaded and objects are
     * put in.
     * Throws FileNotFoundException and NullPointerException
     */
    static Program FileLoader(String fileLocation) throws FileNotFoundException, NullPointerException {
        // Variables
        int size = 1; // will change
        int delay = 100;
        int display_size = 800;

        // get file + scanner from file (ERROR LIKELY TO THROW HERE!)
        File f = new File(fileLocation);
        Scanner sc = new Scanner(f);
        sc.useDelimiter("[\r\t\f -]");

        // Creating the new program
        size = Integer.parseInt(sc.nextLine()); // Get world size from first line
        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        // Display information
            // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass3", true));
            // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow, "flower2", false));
            // Rabbit
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.black, "rabbit-small"));
            // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black, "hole", false));

        // Create a hashmap of all the creatures that can be added to the world. 
        // (String animalName)->(Instance of animal)
        // Then to create a new fresh animal - just do .newInstance();
        HashMap<String, LivingBeing> allTypes = new HashMap<String, LivingBeing>();
        allTypes.put("grass", new Grass());
        allTypes.put("rabbit", new Rabbit());
        allTypes.put("burrow", new RabbitHole());

        // how many objects we have added: 0 in the ground, 0 in the land, 0 in the sky
        // for making sure we dont add more objects than there is space for
        int[] addedObjects = { 0, 0, 0 };
        final int space = size * size;

        // scan the file and add the object
        while (sc.hasNextLine()) {
            // SETUP
            String typeOfCreature = sc.next();
            LivingBeing sampleCreature = allTypes.get(typeOfCreature);
            if (sampleCreature == null) {
                System.out.println(
                        "GIVEN OBJECT: \"" + typeOfCreature + "\" NOT RECOGNIZED ENDING PLACEMENT OPERATIONS!");
                break;
                // TODO: Add errors
            }
            if (!sc.hasNextInt()) {
                // TODO: Add error?
                System.out.println("NO AMOUNT SPECIFIER FOR OBJECT: \"" + typeOfCreature + "\" ENDING PLACEMENT OPERATIONS!");
                System.out.println("\tFOLLOWING LINE: \"" + sc.nextLine() + "\"");
                continue;
            }
            int min = sc.nextInt();
            int max = 0;
            if (sc.hasNextInt()) {
                max = sc.nextInt();
            }
            Random r = new Random();

            // Assert the amount of objects to put
            int diff = Math.abs(max - min); // find the difference between the highest and lowest
            int randAmt = r.nextInt(diff); // create a random number up until the difference
            int finalAmt = randAmt + min; // add the random amount to the minimum amount to find the final amount
            // System.out.println("d:"+diff+", r:"+randAmt+", f:"+finalAmt);

            // get which plane its on
            int zPointer = 1;
            if (sampleCreature instanceof NonBlocking) {
                zPointer = 0;
            }

            for (int i = 0; i < finalAmt; i++) { // create finalAmt objects
                boolean hasPlaced = false;
                // is there space?
                if (addedObjects[zPointer] >= space) {
                    // TODO: ADD ERROR?
                    System.out.println("NO MORE SPACE ON PLANE [" + zPointer + "] FOR:\"" + sampleCreature
                        + "\" WITH: \"" +addedObjects[zPointer] + "/" + space + "\" ADDED OBJECTS, ENDING PLACEMENT OPERATIONS!");
                    break;
                }
                // TODO: FIRST TRY RANDOM THEN TRY SEARCH METHOD, WHEN IS WHAT BETTER?
                while (!hasPlaced) {
                    // FIND RANDOM TILE
                    int x = r.nextInt(size);
                    int y = r.nextInt(size);
                    Location l = new Location(x, y);

                    // test for the specific plane if it can fit
                    switch (zPointer) {
                        case 0:
                            if (!world.containsNonBlocking(l)) {
                                world.setTile(l, sampleCreature.newInstance());
                                hasPlaced = true;
                                addedObjects[0]++;
                            }
                            break;

                        case 1:
                            if (world.isTileEmpty(l)) {
                                world.setTile(l, sampleCreature.newInstance());
                                hasPlaced = true;
                                addedObjects[1]++;
                            }

                        break;
                    }
                }
            }

            if (sc.hasNextLine())
                sc.nextLine();
        }

        // Return
        sc.close(); 
        return p;
    }
}