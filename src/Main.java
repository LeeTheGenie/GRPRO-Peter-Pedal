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
import java.lang.IllegalArgumentException;

import abstracts.LivingBeing;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Program p = fileLoader("data/input-filer 1/test.txt");
            p.show();
            for (int i = 0; i < 3000; i++) {
                p.run();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    static void getDisplayInformation(Program p) {
        // Display information
        // Grass
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass3", true));
        // Flower
        p.setDisplayInformation(Flower.class, new DisplayInformation(Color.yellow, "flower2", false));
        // Rabbit
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.black, "rabbit-small"));
        // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black, "hole", false));
    }

    public static HashMap<String, LivingBeing> classReferenceMap; // i want this public so we dont create a new instance
                                                                  // every time

    static HashMap<String, LivingBeing> getClassReferenceMap() {
        // Create a hashmap of all the creatures that can be added to the world.
        // (String animalName)->(Instance of animal)
        // Then to create a new fresh animal - just do .newInstance();
        if (classReferenceMap != null)
            return classReferenceMap;

        classReferenceMap = new HashMap<String, LivingBeing>();
        classReferenceMap.put("grass", new Grass());
        classReferenceMap.put("rabbit", new Rabbit());
        classReferenceMap.put("burrow", new RabbitHole());
        return classReferenceMap;
    }

    static int[] parseLine(String line, int size, int[] addedObjects, World world) {
        // Error check
        if (addedObjects.length > 3)
            throw new IllegalArgumentException();

        // Method
        String[] splitLine = line.split("[\r\t\f -]");
        String typeOfCreature;
        int min = 0, max = 0, space = size * size;

        if (splitLine.length >= 1) {
            typeOfCreature = splitLine[0];
        } else {
            System.out.println("ERROR (main): emptystring given as argument");
            return addedObjects;
        }

        if (splitLine.length >= 2) {
            min = Integer.parseInt(splitLine[1]);
        } else {
            System.out.println(
                    "ERROR: NO AMOUNT SPECIFIER FOR OBJECT: \"" + typeOfCreature + "\" ENDING PLACEMENT OPERATIONS!");
            return addedObjects;
        }

        if (splitLine.length >= 3) {
            max = Integer.parseInt(splitLine[2]);
        }

        Random r = new Random();

        // Assert the amount of objects to put
        int diff = Math.abs(max - min); // find the difference between the highest and lowest
        int randAmt = r.nextInt(diff); // create a random number up until the difference
        int finalAmt = randAmt + min; // add the random amount to the minimum amount to find the final amount
        // System.out.println("d:"+diff+", r:"+randAmt+", f:"+finalAmt);

        LivingBeing sampleCreature = getClassReferenceMap().get(typeOfCreature);

        if (sampleCreature == null) {
            System.out.println("GIVEN OBJECT: \"" + typeOfCreature + "\" NOT RECOGNIZED ENDING PLACEMENT OPERATIONS!");
            return addedObjects;
        }

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
                        + "\" WITH: \"" + addedObjects[zPointer] + "/" + space
                        + "\" ADDED OBJECTS, ENDING PLACEMENT OPERATIONS!");
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

        return addedObjects;
    }

    /**
     * Fileloader takes one argument (String fileLocation) and returns a program
     * where in the world of the program, the given file is loaded and objects are
     * put in.
     * 
     * @throws FileNotFoundException and NullPointerException
     **/
    static Program fileLoader(String fileLocation) throws FileNotFoundException, NullPointerException {

        // Setup Scanner (ERROR LIKELY TO THROW HERE!)
        Scanner sc = new Scanner(new File(fileLocation));

        // Setup variables (ERROR LIKELY TO THROW HERE!)
        int size = Integer.parseInt(sc.nextLine()), delay = 50, display_size = 800;
        ;

        // Creating the new program and world
        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();
        getDisplayInformation(p);

        // how many objects we have added: 0 in the ground, 0 in the land, 0 in the sky,
        // for making sure we dont add more objects than there is space for
        int[] addedObjects = { 0, 0, 0 };

        // scan the file and add the object
        while (sc.hasNextLine()) {
            parseLine(sc.nextLine(), size, addedObjects, world);
        }

        // Return
        sc.close();
        return p;
    }

}