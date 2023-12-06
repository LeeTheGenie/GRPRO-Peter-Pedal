import java.awt.Color;

import itumulator.world.World;
import plants.BerryBush;
import plants.Bush;
import plants.Flower;
import plants.Grass;
import itumulator.world.Location;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.crypto.KEM;

import abstracts.LivingBeing;
import executable.DisplayInformation;
import executable.Program;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Program p = fileLoader("data/input-filer 2/t2-5a.txt");
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
        p.setDisplayInformation(RabbitHole.class, new DisplayInformation(Color.black, "hole", false));
        // Bear
        p.setDisplayInformation(Bear.class, new DisplayInformation(Color.blue, "bear", false));
        // SmallCarcass
        p.setDisplayInformation(SmallCarcass.class, new DisplayInformation(Color.black, "carcass-small", false));
        // BerryBush
        p.setDisplayInformation(BerryBush.class, new DisplayInformation(Color.green, "bush-berries", false));
        // Bush
        p.setDisplayInformation(Bush.class, new DisplayInformation(Color.green, "bush", false));
        // Wolf
        p.setDisplayInformation(Wolf.class, new DisplayInformation(Color.green, "wolf", false));
        // BerryBush/bush
        p.setDisplayInformation(BerryBush.class, new DisplayInformation(Color.green, "bush-berries", false));
        p.setDisplayInformation(Bush.class, new DisplayInformation(Color.green, "bush", false));
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
        return classReferenceMap;
    }

    static void printStringArray(String[] arr) {
        System.out.print("Length ("+arr.length+"): ");
            for(String s:arr) {
                System.out.print("["+s+"]");
            }
        System.out.print("\n");
    }

    static LivingBeing parseLivingBeing(String str) {
        return getClassReferenceMap().get(str);
    }

    static Location parseLocation(String str) {
        String[] locationString = str.split("[(),\r\t\f ]");
        //printStringArray(locationString);
        Location spawnLocation = null;
        Integer x=null, y=null;
        try {
            if(locationString.length >= 2)
                x = Integer.parseInt(locationString[1]);
            if(locationString.length >= 3)
                y = Integer.parseInt(locationString[2]);
            if(x!=null&&y!=null)
                spawnLocation = new Location(x-1, y-1);
        } catch(Exception e) {
            System.out.println("ERROR (main):"+e.getMessage());
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
        return new int[]{min,max};
    }

    static LineInformation parseLine(String line) {
        // Get the line
        String[] splitLine = line.split("[\r\t\f ]");
        // ex   [bear][1-2][(1,1)]
        //      [typeOfCreature][amount][location]
        //printStringArray(splitLine);

        // Assert the creature
        if (!(splitLine.length >= 1)) {
            System.out.println("ERROR (main): emptystring given as argument");
            return null;
        }
        LivingBeing typeOfCreature = parseLivingBeing(splitLine[0]);
        
        if(typeOfCreature==null)
            System.out.println("ERROR (main): LivingBeing not recognized \""+splitLine[0]+"\"");

        // Assert min max 
        if (!(splitLine.length >= 1)) {
            System.out.println("ERROR (main): no amount specifier for: "+splitLine[0]);
            return null;
        }
        int[] minMax = parseMinMax(splitLine[1]);

        // Assert location
        Location spawnLocation = null;
        if(splitLine.length >= 3) {
            spawnLocation = parseLocation(splitLine[2]);
        }

       return new LineInformation(typeOfCreature,minMax[0],minMax[1],spawnLocation); 
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

        // Creating the new program and world
        int world_size = Integer.parseInt(sc.nextLine());
        Program p = new Program(world_size, 800, 100);
        World world = p.getWorld();
        getDisplayInformation(p);

        SpaceManager spaceManager = new SpaceManager(world_size);

        // scan the file and add the object
        while (sc.hasNextLine()) {
            placeMultipleBeings(spaceManager, parseLine(sc.nextLine()),world,world_size);
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

    public static int assertAmount(int min,int max) {
        Random r = new Random();

        // Assert the amount of objects to put
        int diff = Math.abs(max - min); // find the difference between the highest and lowest
        int randAmt = r.nextInt(diff); // create a random number up until the difference
        int finalAmt = randAmt + min; // add the random amount to the minimum amount to find the final amount
        return finalAmt; 
    }

    public static void placeMultipleBeings(SpaceManager spaceManager, LineInformation lineInformation,World world,int world_size) {
        // Failstates
        if(lineInformation==null)
            return;

        // assert amount to be placed
        int amount = assertAmount(lineInformation.minAmount,lineInformation.maxAmount);

        // place that amount
        for (int i = 0; i < amount; i++) {
            boolean hasPlaced = false;
            //System.out.println("running program!"+i);

            while (!hasPlaced) {
                if(!spaceManager.isThereSpaceFor(lineInformation.livingBeing)){ // if there is no more space.
                    System.out.println("No more space for: "+lineInformation.livingBeing+" ending placement operations!");
                    break;
                }

                // Get a random location
                Location rl = getRandomLocation(world_size);
                int z = spaceManager.getZ(lineInformation.livingBeing);                
                
                // if nonblocking or empty tile
                if(
                    (z==0&&world.containsNonBlocking(rl)) || // if nonblocking          System.out.println("Condition 1: "+(z==0)+ " && "+world.containsNonBlocking(rl));
                    (z==1&&!world.isTileEmpty(rl))           // if there is a blocking  System.out.println("Condition 2: "+(z==1)+ " && "+world.isTileEmpty(rl));
                ) continue; // then stop

                // spawn being 
                LivingBeing spawn = lineInformation.livingBeing.newInstance();
                world.setTile(rl, spawn);
                spaceManager.incrementDimension(lineInformation.livingBeing);
                hasPlaced = true;

                // special if bear
                if((spawn instanceof Bear)&&lineInformation.spawnLocation!=null){
                    ((Bear) spawn).setSpawnLocation(lineInformation.spawnLocation);
                    ((Bear) spawn).setTerritory(world);
                }
            }
        }
    }
}