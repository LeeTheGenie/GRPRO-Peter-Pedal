package animal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Plant;
import abstracts.Predator;
import executable.DisplayInformation;
import abstracts.Animal;
import itumulator.world.Location;
import itumulator.world.World;
import misc.RabbitHole;
import misc.WolfHole;

public class Wolf extends Predator {

    private LivingBeing target;
    private WolfPack pack;

    // Sleeping
    private int sleepyness;


    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.80d);
        this.target = null;
        this.pack = null;
        growthStates = new String[][]{{"wolf-small","wolf-small-sleeping"},{"wolf","wolf-sleeping"}};
        sleepyness = 0; 
    }

    @Override public DisplayInformation getInformation() {
        int sleepPointer = (sleeping)?1:0;
        int growthPointer = isMature()?1:0;
        
        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {     
        if(!sleeping) {
            handlePack(world);
            handleMovement(world);
        }   
        handleSleep(world);
        super.act(world);
    }

    /**
     * A function to join all wolfpack related acitons
     * @param world
     */
    public void handlePack(World world) {
        // join packs
        if (pack == null) {
            if (wolfNearby(world, 3)) {
                moveCloser(world);
                if (!hasPack(world, getNearbyWolfs(world, 1))) {
                    createPack(world);
                    System.out.println("created pack");
                } else if (this.getPack().getWolfPack().size() < 6) { // virker nok ikke
                    joinPack();
                }

                pack.display();
            }
        }
    }

    /**
     * A function to join all movement for wolfs together.
     * @param world
     */
    public void handleMovement(World world){
        if (!world.isOnTile(this))
            return;

        if(wantToSleep()) {     // if you want to sleep go to sleep
            if(hasPack()) { 
                // if wolf has a pack
                WolfHole wolfHole = getPack().getWolfHole();
                if(wolfHole!=null) { 
                    // has a hole - go towards it
                    if(!world.isOnTile(wolfHole)) {
                        if(world.getLocation(wolfHole).equals(world.getLocation(this))) {
                            enterHole(world);
                            return;
                        }
                        move(world,toAndFrom(world, world.getLocation(wolfHole), world.getLocation(this)));
                        return;
                    }
                } else {
                    // does nst have a hole - dig a hole.
                    digHole(world);
                    return;
                }
            } else { 
                // if wolf does not have a pack just sleep where you are
                setSleeping(true);
                return;
            }
        }
        if(isHungry()) { // if you are hungry go eat
            if (target == null) {
                // no target? go find one.
                target = locateTarget(world, 3);
            } else if (world.isOnTile(this) && world.isOnTile(target)) {
                move(world, toAndFrom(world, world.getLocation(target),world.getLocation(this)));
                killTarget(world);
                //eatTarget(world, null);
                return;
            } else {
                target = null;
            }
        }
        if(pack != null)  {          // if you are lonely go find friends
            if(!wolfNearby(world, 1) && wolfNearby(world, 3)) {
                moveCloser(world);
                // er not sure på det her.

            }            
        }               
        // if none go wander around
        move(world, null);
    }

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world)  {
        if(sleeping) {
            if(sleepyness<10) {
                setSleeping(false);
            }
            sleepyness-=10; 
        } else {
            sleepyness+=1; 
            if(sleepyness<=0) {
                die(world,"sleep-exhaustion");
            }
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public void digHole(World world) {
        int holeDigCost = 8;
        if (!canAfford(holeDigCost))
            return;
        if (!world.isOnTile(this))
            return;

        Location wolfLocation = world.getLocation(this);

        try {
            if(world.getNonBlocking(wolfLocation) instanceof Plant) {
                ((Plant) world.getNonBlocking(wolfLocation)).die(world);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if (world.containsNonBlocking(wolfLocation))
            return;
        
        WolfHole wolfHole = new WolfHole();
        wolfHole.setOwner(this.getPack());
        this.getPack().setWolfHole(wolfHole);
        changeEnergy(holeDigCost, world);
        world.setTile(wolfLocation, wolfHole);
    }


    /**
     * nicky please explain what this does
     * @param world
     */
    public void moveCloser(World world) {
        Set<Location> tiles = world.getSurroundingTiles(3);
        Location wolfLocation = world.getLocation(this);
        Location newLocation = null;
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                newLocation = toAndFrom(world, l, wolfLocation);
            }
        }
        move(world,newLocation);
    }

    public boolean wolfNearby(World world, int radius) {
        Set<Location> tiles = world.getSurroundingTiles(radius);
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    public List<Location> getNearbyWolfs(World world, int radius) {
        Set<Location> tiles = world.getSurroundingTiles(radius);
        List<Location> wolfsNearby = new ArrayList<>();
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                wolfsNearby.add(l);
            }
        }
        return wolfsNearby;
    }

    public void killTarget(World world) {
        //((LivingBeing) world.getTile(world.getLocation(target))).die(world);
    }

    public void eatTarget(World world, Animal target) {
        // aaffa
    }

    public void createPack(World world) {
        pack = new WolfPack(null);
        List<Location> wolfsNearby = getNearbyWolfs(world, 3);
        for (Location l : wolfsNearby) {
            pack.addWolf((Wolf) world.getTile(l));
        }
    }

    public void joinPack() {
        // ahdas da
    }

    public void setPack(String name) {
        this.pack = new WolfPack(name);
    }

    public WolfPack getPack() {
        return pack;
    }

    public boolean hasPack(World world, List<Location> wolfs) {
        for (Location l : wolfs) {
            Wolf wolf = (Wolf) world.getTile(l);
            if (wolf.getPack() != null) {
                return true;
            }
        }
        return false;
    }
    public boolean hasPack(){
        return pack!=null;
    }
    public boolean wantToSleep(){
        return sleepyness>60;
    }
    public boolean isHungry() {
        return maxEnergy*hungerFactor<currentEnergy;
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

        if (!(objectUnderneath instanceof WolfHole))
            return;
        // Sucess
        WolfHole wolfHoleUnderneath = (WolfHole) objectUnderneath;

        if (objectUnderneath.equals(this.getPack().getWolfHole())) {
            world.remove(this);
            setSleeping(true);

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
        if (this.getPack().getWolfHole() == null)
            return;
        if (!world.isOnTile(this.getPack().getWolfHole() ))
            return;

        Location exitLocation = world.getLocation(this.getPack().getWolfHole() );
        if (!world.isTileEmpty(exitLocation))
            return;

        // System.out.println("Go to tile:" + exitLocation);
        world.setTile(exitLocation, this);
        setSleeping(false);
    }

}
