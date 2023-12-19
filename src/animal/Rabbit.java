package animal;

import itumulator.world.World;
import misc.RabbitHole;
import misc.Trap;
import itumulator.world.Location;

import java.util.Set;

import abstracts.Animal;
import abstracts.LivingBeing;
import abstracts.Plant;
import abstracts.Predator;
import executable.DisplayInformation;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Rabbit extends Animal {

    private RabbitHole rabbithole;
    private int holeDigCost;
    private int reproductiveCooldown;
    private Rabbit mate;

    private Location yummyBerries;

    // TODO: reproductive cooldown
    // TODO: Run from predators
    public Rabbit() {
        super(0, 140, 120, 10, 1, 15, 0, 2);
        this.rabbithole = null;
        sleeping = false;
        holeDigCost = 10;
        this.yummyBerries = null;
        growthStates = new String[][] { { "rabbit-small", "rabbit-small-sleeping" },
                { "rabbit-large", "rabbit-sleeping" } };
        reproductiveCooldown = 0; 
        mate = null;
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = (matureAge <= age) ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public Rabbit newInstance() {
        return new Rabbit();
    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy
        reproductiveCooldown--;

        if (world.isNight()) {
            sleeping = true;
            if (rabbithole == null) {
                digHole(world);
            } else {
                locateHole(world);
            }
            if (!resting)
                enterHole(world);

        } else {
            sleeping = false;
            // exit hole
            findYummyBerries(world);
            eat(world);

            if (this.yummyBerries == null) {
                handleMovement(world);
            } else
                goToBerries(world);
            trapped(world);
            reproduce(world);
            if (resting)
                exitHole(world);
        }

        super.act(world);
    }



    /**
     * Locates rabbits that want to mate within a radius of 3
     * @param world
     * @return
     */
    public Rabbit locateMate(World world) {
        if (!validateLocationExistence(world))
            return null;

        Location currentLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, 5);
        for (Location l : surroundingTiles) {
            Object target = world.getTile(l);
            if (target instanceof Rabbit) {
                if(((Rabbit) target).wantToReproduce()&&!((Rabbit) target).hasMate())
                    return (Rabbit) target;
            }
        }
        return null;
    }

    /**
     * Method to handle all daytime movements for rabit
     * @param world
     */
    public void handleMovement(World world) {
        if(!validateLocationExistence(world))
            return;

        if(alerted) {
            try {
                alertedMovement(world);
                return;
            } catch (Exception e) {
                
            }
        }

        if(wantToReproduce()) {
            // find a mate
            if(hasMate()) {
                // has already found a mate
                if(mate.validateLocationExistence(world)){
                    Location location_new = toAndFrom(world, world.getLocation(mate), world.getLocation(this));
                    move(world,location_new);
                    return;
                }
            } else {
                // has NOT found a mate
                mate = locateMate(world);
                if(hasMate()){
                    mate.setMate(this);
                    if(mate.validateLocationExistence(world)){
                        Location location_new = toAndFrom(world, world.getLocation(mate), world.getLocation(this));
                        move(world,location_new);
                        return;
                    }
                }

            }
        }

        move(world, null);
    }


    /**
     * A method to handle alerted movement, throws Exception if it is no longer in danger and doesn't need to move.
     * This is done so if no movement is made the "parent" metod doesn't need to return. 
     * @param world
     * @throws Exception
     */
    public void alertedMovement(World world) throws Exception {
        Set<Predator> dangers = new HashSet<Predator>();

        // search for dangers
        for(Location l:  world.getSurroundingTiles(world.getLocation(this),4)) {
            Object target = world.getTile(l);
            if(target instanceof Predator) {
                if(((Predator)target).canEat(world,this)) {
                    dangers.add(((Predator)target));
                }
            }
        } 

        if(dangers.size()<=0){ // fail condition
            this.setAlert(false);
            throw new Exception("No dangers");
        }

        // sucess - we need to move out of the way
        Location sumofVectors = new Location(0,0);
        //System.out.println("I am: "+this+" location: "+world.getLocation(this));
        for(Predator p:dangers) {
            if(p.validateLocationExistence(world)){
                Location l = drawVector(world,p,this);
                //System.out.println(world.getLocation(p)+" -> "+l);
                sumofVectors = addVectors(sumofVectors, l);
            }
        }

        Location newLocation = getLocationFromVector(world, sumofVectors);
        //System.out.println("danger vector:"+sumofVectors+" new:"+newLocation);

        move(world,toAndFrom(world, newLocation, world.getLocation(this)));

    }

    /**
     * Returns true if rabbit is ready reproduce, false otherwise
     * @return true/false
     */
    public boolean wantToReproduce() {
        if(isMature())
            return (reproductiveCooldown<=0);
        return false; 
    }

    public void setMate(Rabbit rabbit) {
        this.mate = rabbit; 
    }

    /**
     * Returns true if has a found a mate
     * @return
     */
    public boolean hasMate() {
        return mate!=null;
    }

    /**
     * Eats plants underneath it
     * 
     * @param world
     */
    public void eat(World world) {
        int energyIncrement = 8; // hvor meget energi man får

        // Failstates
        if (currentEnergy == maxEnergy)
            return;
        if (!world.isOnTile(this))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        if (world.getNonBlocking(world.getLocation(this)) instanceof Plant) { // er det en plant
            world.delete(world.getNonBlocking(world.getLocation(this))); // slet den plant
            changeEnergy(energyIncrement, world);
            System.out.println("plant");
        }
    }

    /**
     * locateHole() moves 1 step to the rabbits hole.
     * 
     * @param world
     */
    public void locateHole(World world) {
        if (rabbithole == null)
            return;
        if (!world.isOnTile(this))
            return;

        Location rabbitLocation = world.getLocation(this);
        Location holeLocation = world.getLocation(rabbithole);

        move(world, toAndFrom(world, holeLocation, rabbitLocation));
    }

    /**
     * Births a new rabbit if 
     * 
     * @param world
     */
    public void reproduce(World world) {
        //System.out.println(this);
            // Failstates
        if(!wantToReproduce())
            return;
        if (!canAfford(reproductionCost))
            return;
        if (!validateLocationExistence(world))
            return;
        if(!hasMate())
            return;
        if(!mate.validateLocationExistence(world))
            return;
        if(!(getDistance(world,mate)<=1))
            return; 

        // Mating sucess
        
        // Get surrounding tiles
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (list.size() == 0)
            return;
        Location newLocation = list.get(new Random().nextInt(list.size()));

        // create a new instance of Rabbit and put it on the world
        Rabbit baby = new Rabbit();
        baby.setBaby();
        world.setTile(newLocation, baby);

        // reproductive cooldown
        mate.invokeReproductiveCooldown();
        invokeReproductiveCooldown();

        changeEnergy(reproductionCost, world);;
    }

    public void invokeReproductiveCooldown() {
        reproductiveCooldown = 20;
        this.mate = null;
    }

    /**
     * Digs a hole at current location
     * 
     * @param world
     */
    public void digHole(World world) {
        if (!canAfford(holeDigCost))
            return;
        if (!world.isOnTile(this))
            return;

        Location rabbitLocation = world.getLocation(this);

        eat(world);
        if (world.containsNonBlocking(rabbitLocation))
            return;
        rabbithole = new RabbitHole(this);
        changeEnergy(holeDigCost, world);

        world.setTile(rabbitLocation, rabbithole);
    }

    /**
     * Tries to enter a hole underneath it.
     * Claims a hole if it encounters a empty hole
     * 
     * @param world
     */
    public void enterHole(World world) {
        if (!world.isOnTile(this))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        Object objectUnderneath = world.getNonBlocking(world.getLocation(this));

        if (!(objectUnderneath instanceof RabbitHole))
            return;
        // Sucess
        RabbitHole rabbitHoleUnderneath = (RabbitHole) objectUnderneath;

        if (!rabbitHoleUnderneath.isClaimed()) {
            // System.out.println(this+" trying to claim: "+rabbitHoleUnderneath+" with
            // owner: "+rabbitHoleUnderneath.getOwner());
            try {
                rabbitHoleUnderneath.setOwner(this);
                rabbithole = rabbitHoleUnderneath;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (objectUnderneath.equals(rabbithole)) {
            world.remove(this);
            resting = true;

            // System.out.println("Rabbit(s) resting: " + this.rabbits);
            // System.out.println(" -In " + world.getTile(world.getLocation(this)));
        }
    }

    /**
     * Atempts to exit the hole the rabbit is in;
     * 
     * @param world
     * 
     */
    public void exitHole(World world) {
        if (world.isOnTile(this))
            return;
        if (rabbithole == null)
            return;
        if (!world.isOnTile(rabbithole))
            return;

        Location exitLocation = world.getLocation(rabbithole);
        if (!world.isTileEmpty(exitLocation))
            return;

        // System.out.println("Go to tile:" + exitLocation);
        world.setTile(exitLocation, this);
        resting = false;
    }

    public void findYummyBerries(World world) {
        if (validateLocationExistence(world)) {
            Location currentLocation = world.getLocation(this);

            Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, 3);

            for (Location l : surroundingTiles) {
                Object trap = world.getTile(l);
                if (trap instanceof Trap) {
                    this.yummyBerries = world.getLocation(trap);
                    break;
                } else
                    this.yummyBerries = null;

            }
        }

    }

    public void goToBerries(World world) {

        if (validateLocationExistence(world)) {
            if (!(yummyBerries == null)) {
                move(world, toAndFrom(world, yummyBerries, world.getLocation(this)));
            }

        }

    }

    public void trapped(World world) {
        if (!world.isOnTile(this))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        Object objectUnderneath = world.getNonBlocking(world.getLocation(this));

        if ((objectUnderneath instanceof Trap)) {
            ((Trap) objectUnderneath).trapped();
            world.delete(this);
        }
    }

    @Override public void die(World world) {
        super.die(world);
        if(rabbithole!=null)
            rabbithole.clearOwner();
    }
}
