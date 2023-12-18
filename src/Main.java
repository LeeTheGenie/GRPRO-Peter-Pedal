import java.awt.Color;

import misc.RabbitHole;
import misc.Trap;
import misc.TrapActivated;
import misc.WolfHole;
import misc.WolfPack;
import misc.Carcass;
import misc.Fungus;

import plants.BerryBush;
import plants.Bush;
import plants.Flower;
import plants.Grass;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import abstracts.LivingBeing;

import animal.Bear;
import animal.Rabbit;
import animal.Wolf;
import animal.Monkey;

import executable.DisplayInformation;
import executable.Program;

import java.util.HashMap;

public class Main {
    static int displaySize = 800;
    static int delay = 100;

    public static void main(String[] args) {
        try {
            Program p = fileLoader("data/input-filer/test.txt", displaySize, delay);
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
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        // RabbitHole
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black, "hole-small", false));
        // Bear
        p.setDisplayInformation(Bear.class, new DisplayInformation(Color.blue, "bear", false));
        // SmallCarcass
        p.setDisplayInformation(Carcass.class, new DisplayInformation(Color.black, "carcass-small", false));
        // BerryBush
        p.setDisplayInformation(BerryBush.class, new DisplayInformation(Color.green, "bush-berries", false));
        // Bush
        p.setDisplayInformation(Bush.class, new DisplayInformation(Color.green, "bush", false));
        // Wolf
        p.setDisplayInformation(Wolf.class, new DisplayInformation(Color.green, "wolf", false));
        // BerryBush/bush
        p.setDisplayInformation(BerryBush.class, new DisplayInformation(Color.green, "bush-berries", false));
        p.setDisplayInformation(Bush.class, new DisplayInformation(Color.green, "bush", false));
        // wolf hole
        p.setDisplayInformation(WolfHole.class, new DisplayInformation(Color.green, "hole", false));
        // moneky
        p.setDisplayInformation(Monkey.class, new DisplayInformation(Color.green, "monkey", false));
        // trap
        p.setDisplayInformation(Trap.class, new DisplayInformation(Color.green, "trap", false));
        p.setDisplayInformation(TrapActivated.class, new DisplayInformation(Color.green, "trap-used", false));

    }

    public static HashMap<String, LivingBeing> classReferenceMap;

    // i want this public so we dont create a new instance every time
    static HashMap<String, LivingBeing> getClassReferenceMap() {
        // Create a hashmap of all the creatures that can be added to the world.
        // (String animalName)->(Instance of animal)
        // Then to create a new fresh animal - just do .newInstance();
        if (classReferenceMap != null)
            return classReferenceMap;

        classReferenceMap = new HashMap<String, LivingBeing>();
        classReferenceMap.put("grass", new Grass());
        classReferenceMap.put("rabbit", new Rabbit());
        classReferenceMap.put("burrow", new RabbitHole(null));
        classReferenceMap.put("bear", new Bear());
        classReferenceMap.put("wolf", new Wolf());
        classReferenceMap.put("berry", new BerryBush());
        classReferenceMap.put("fungus", new Fungus());
        classReferenceMap.put("carcass", new Carcass(0, 0, 0));
        classReferenceMap.put("monkey", new Monkey());
        classReferenceMap.put("trap", new Trap());
        return classReferenceMap;
    }

    static void printStringArray(String[] arr) {
        System.out.print("Length (" + arr.length + "): ");
        for (String s : arr) {
            System.out.print("[" + s + "]");
        }
        System.out.print("\n");
    }

    static LivingBeing parseLivingBeing(String str) {
        return getClassReferenceMap().get(str);
    }

    static Location parseLocation(String str) {
        String[] locationString = str.split("[(),\r\t\f ]");
        // printStringArray(locationString);
        Location spawnLocation = null;
        Integer x = null, y = null;
        try {
            if (locationString.length >= 2)
                x = Integer.parseInt(locationString[1]);
            if (locationString.length >= 3)
                y = Integer.parseInt(locationString[2]);
            if (x != null && y != null)
                spawnLocation = new Location(x - 1, y - 1);
        } catch (Exception e) {
            System.out.println("ERROR (main):" + e.getMessage());
        }
        return spawnLocation;
    }

    static int[] parseMinMax(String str) {
        Integer min = 0, max = 0;
        String[] minMax = str.split("-");

        if (minMax.length >= 1) {
            min = Integer.parseInt(minMax[0]);
        } else {
            System.out.println("ERROR: NO AMOUNT SPECIFIER FOR OBJECT: \"" + str + "\" ENDING PLACEMENT OPERATIONS!");
            return null;
        }
        if (minMax.length >= 2) {
            max = Integer.parseInt(minMax[1]);
        }
        return new int[] { min, max };
    }

    static LineInformation parseLine(String line) {
        // ex [bear][1-2][(1,1)]
        // ex [carcass][fungi][5-10]
        // ex [carcass][4]
        // ex [cordyceps][rabbit][1-2]
        // Generalized form:
        // [typeOfCreature/cordyceps][amount/fungi][location/amount][location] bro
        // :skull:

        // Get the line
        String[] splitLine = line.split("[\r\t\f ]");
        int aP = 0; // arrayPointer
        boolean cordyceps = false, fungus = true;
        // printStringArray(splitLine);

        // Assert the creature
        if (!(splitLine.length >= 1)) {
            System.out.println("ERROR (main): emptystring given as argument");
            return null;
        }
        if (splitLine[0].equals("cordyceps")) { // check for cordyceps
            cordyceps = true;
            aP++;
        }
        LivingBeing typeOfCreature = parseLivingBeing(splitLine[0 + aP]);

        if (typeOfCreature == null)
            System.out.println("ERROR (main): LivingBeing not recognized \"" + splitLine[0 + aP] + "\"");

        // Assert min max
        if (!(splitLine.length >= 1 + aP)) {
            System.out.println("ERROR (main): no amount specifier for: " + splitLine[0 + aP]);
            return null;
        }
        if (splitLine[1 + aP].equals("fungi") && typeOfCreature instanceof Carcass) { // check for fungus
            fungus = true;
            aP++;
        }

        int[] minMax = parseMinMax(splitLine[1 + aP]);

        // Assert location
        Location spawnLocation = null;
        if (splitLine.length >= 3 + aP) {
            spawnLocation = parseLocation(splitLine[2 + aP]);
        }

        return new LineInformation(typeOfCreature, minMax[0], minMax[1], spawnLocation, fungus, cordyceps);
    }

    /**
     * Fileloader takes one argument (String fileLocation) and returns a program
     * where in the world of the program, the given file is loaded and objects are
     * put in.
     * 
     * @throws FileNotFoundException and NullPointerException
     **/
    static Program fileLoader(String fileLocation, int displaySize, int delay)
            throws FileNotFoundException, NullPointerException {

        // Setup Scanner (ERROR LIKELY TO THROW HERE!)
        Scanner sc = new Scanner(new File(fileLocation));

        // Creating the new program and world
        int world_size = Integer.parseInt(sc.nextLine());
        Program p = new Program(world_size, displaySize, delay);
        World world = p.getWorld();
        getDisplayInformation(p);

        SpaceManager spaceManager = new SpaceManager(world_size);

        // scan the file and add the object
        while (sc.hasNextLine()) {
            placeMultipleBeings(spaceManager, parseLine(sc.nextLine()), world, world_size);
        }

        // Return
        sc.close();
        return p;
    }

    public static Location getRandomLocation(int size) {
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        return new Location(x, y);
    }

    public static int assertAmount(int min, int max) {
        Random r = new Random();

        // Assert the amount of objects to put
        int diff = Math.min(Math.abs(max - min), 0); // find the difference between the highest and lowest

        int randAmt;
        if (diff > 0) {
            randAmt = r.nextInt(diff); // create a random number up until the difference
        } else {
            randAmt = 0;
        }

        int finalAmt = randAmt + min; // add the random amount to the minimum amount to find the final amount
        return finalAmt;
    }

    public static void placeMultipleBeings(SpaceManager spaceManager, LineInformation lineInformation, World world,
            int world_size) {
        // Failstates
        if (lineInformation == null)
            return;
        WolfPack wolfPack = null; // wolfpack

        // assert amount to be placed
        int amount = assertAmount(lineInformation.minAmount, lineInformation.maxAmount);

        // place that amount
        for (int i = 0; i < amount; i++) {
            boolean hasPlaced = false;
            // System.out.println("running program!"+i);

            while (!hasPlaced) {
                if (!spaceManager.isThereSpaceFor(lineInformation.livingBeing)) { // if there is no more space.
                    System.out.println(
                            "No more space for: " + lineInformation.livingBeing + " ending placement operations!");
                    break;
                }

                // Get a random location
                Location rl = getRandomLocation(world_size);
                int z = spaceManager.getZ(lineInformation.livingBeing);

                // if nonblocking or empty tile
                /*
                 * boolean c11 = (z==0), c12 = world.containsNonBlocking(rl), c1 = c11&&c12,
                 * c21 = (z==1), c22 = !world.isTileEmpty(rl), c2 = c21&&c22, j = c1||c2;
                 * 
                 * System.out.println("Deriving judgement for: "+lineInformation.livingBeing +
                 * " on plane: "+z);
                 * System.out.println("Condition 1: ("+c1+"): "+c11+ " && "+c12);
                 * System.out.println("Condition 2: ("+c2+"): "+c21+ " && "+c22);
                 * System.out.println("Judgement ("+j+"): "+c1+ " || "+c2);
                 */

                if ((z == 0 && world.containsNonBlocking(rl)) || // if nonblocking
                        (z == 1 && !(world.isTileEmpty(rl))) // if there is a blocking */
                ) {
                    continue; // then stop
                }

                // spawn being
                LivingBeing spawn = lineInformation.livingBeing.newInstance();
                world.setTile(rl, spawn);
                spaceManager.incrementDimension(lineInformation.livingBeing);
                hasPlaced = true;

                if (lineInformation.fungi && spawn instanceof Carcass) { // special if fungis
                    ((Carcass) spawn).secureFungus();
                } else if (spawn instanceof Wolf) { // special if wolf
                    if (wolfPack == null) {
                        ((Wolf) spawn).createPack(world);
                        wolfPack = ((Wolf) spawn).getPack();
                    } else {
                        ((Wolf) spawn).joinPack(wolfPack);
                    }

                } else if ((spawn instanceof Bear) && lineInformation.spawnLocation != null) { // special if bear

                    ((Bear) spawn).setSpawnLocation(lineInformation.spawnLocation);
                    ((Bear) spawn).setTerritory(world);
                }
            }
        }
    }
}