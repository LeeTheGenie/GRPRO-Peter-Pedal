package animal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstracts.Animal;
import abstracts.LivingBeing;
import abstracts.Plant;
import abstracts.Predator;
import executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import misc.WolfHole;
import misc.WolfPack;

public class Wolf extends Predator {

    private LivingBeing target;
    private WolfPack wolfPack;

    // Sleeping
    private int sleepyness, bedtime;

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.60d);
        this.target = null;
        this.wolfPack = null;
        growthStates = new String[][] { { "wolf-small", "wolf-small-sleeping" }, { "wolf", "wolf-sleeping" } };
        this.sleepyness = 0;
        this.bedtime = 60;
    }

    @Override public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = isMature() ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override public void act(World world) {
        //if(hasPack())
        //System.out.println("I "+this+" am in "+wolfPack+", size: "+wolfPack.getSize());
        if(!sleeping) {
            handlePack(world);
            handleMovement(world);
            handleSurroundings(world);
        }
        handleSleep(world);
        reproduce(world);

        super.act(world);
    }

    @Override
    public void die(World world) {
        super.die(world);
        if(hasPack())
            leavePack();
    }   

    @Override public boolean canEat(World world,LivingBeing livingBeing) {
        if(!validateLocationExistence(world))
            return false; 

        if(livingBeing instanceof Rabbit) {
            return true;
        }

        if(livingBeing instanceof Bear) {
            int wolfCount = 0;
            for(Object o :world.getSurroundingTiles(world.getLocation(this))) {
                if(o instanceof Wolf){
                    if(((Wolf)o).getPack().equals(this.getPack())){
                        wolfCount++;
                    }
                }
                if(wolfCount>=2) {
                    return true;
                }
            }
        }

        return false; 
    }

    /**
     * A function to join all movement for wolfs together.
     * 
     * @param world
     */
    public void handleMovement(World world) {
        if (!validateLocationExistence(world))
            return;
        if (wantToSleep()) { // if you want to sleep go to sleep
            if (hasPack()) {
                // if wolf has a pack
                WolfHole wolfHole = getPack().getWolfHole();
                if (wolfHole != null) {
                    // has a hole - go towards it
                    if (world.isOnTile(wolfHole)) {
                        if (world.getLocation(wolfHole).equals(world.getLocation(this))) {
                            enterHole(world);
                            setSleeping(true);
                            return;
                        }
                        move(world, toAndFrom(world, world.getLocation(wolfHole), world.getLocation(this)));
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

        if(hasPack()) {
            if(getPack().getHunting()) {
                if(!getPack().hasTarget())
                    getPack().setTarget(locateTarget(world, 8));
                if(getPack().getTarget()!=null) {
                    if(getPack().getTarget().validateLocationExistence(world)) {
                        Location oneCloser = toAndFrom(world, world.getLocation(getPack().getTarget()), world.getLocation(this));
                    
                        move(world,oneCloser);
                        
                        return;
                    }
                }
            } else if(isSmallHungry()) {
                if(target==null)
                    target = locateCarcass(world, 5);
                moveToTarget(world);
                return;
            }
        } else if(isHungry()) {
            if (target == null)
                target = locateCarcass(world, 5);
            if(target==null)
                target = locateTarget(world,6);

            moveToTarget(world);
            
            return;
        }

        move(world, null);
    }

    public void moveToTarget(World world){
        if(target==null) return; 
        if(!validateLocationExistence(world)) return;
        if(!target.validateLocationExistence(world)) return;
        Location oneCloser = toAndFrom(world, world.getLocation(target), world.getLocation(this));

        move(world,oneCloser);
    }

    /**
     * A funciton where a wolf handles its surrondings
     * 
     * @param world
     */
    public void handleSurroundings(World world) {
        killTarget(world);
        eatTarget(world);
        if(!validateExistence(world))
            target = null; 
        // kill animals
        // if target not exist set to null
    }

    /**
     * Overwritten method, kills nearby targets, and if is hunting increment kills
     */
    @Override public void killTarget(World world) {
        if(!validateLocationExistence(world)) return;
        for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
            Object target = world.getTile(l);
            if(target==null)
                continue;
            if(!((LivingBeing) target).validateExistence(world))
                continue;
            if(canEat(world, ((LivingBeing) target))) {
                ((LivingBeing) target).die(world);
                if(hasPack()) {
                    getPack().incrementKills();
                }
            }
        }
    } 

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world) {
        if (sleeping) {
            if (sleepyness < 10) {
                exitHole(world);
            }
            sleepyness -= 4;

        } else {
            sleepyness += 1;
            if (sleepyness >= 100) {
                die(world, "sleep-exhaustion");
            }
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
        this.resting = sleeping;
    }

    public void digHole(World world) {
        int holeDigCost = 8;
        if (!canAfford(holeDigCost))
            return;
        if (!world.isOnTile(this))
            return;

        Location wolfLocation = world.getLocation(this);

        try {
            if (world.getNonBlocking(wolfLocation) instanceof Plant) {
                ((Plant) world.getNonBlocking(wolfLocation)).die(world);
            }
        } catch (Exception e) {
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

    public boolean wantToSleep() {
        return sleepyness > bedtime;
    }

    public boolean isHungry() {
        return maxEnergy * hungerFactor < currentEnergy;
    }

    public boolean isSmallHungry() {
        return maxEnergy * 0.8d < currentEnergy;
    }

    /**
     * Tries to enter a hole underneath it.
     * Claims a hole if it encounters a empty hole
     * 
     * @param world
     */
    public void enterHole(World world) {
        if (!validateLocationExistence(world))
            return;
        if (!world.containsNonBlocking(world.getLocation(this)))
            return;

        Object objectUnderneath = world.getNonBlocking(world.getLocation(this));

        if (!(objectUnderneath instanceof WolfHole))
            return;
        // Sucess
        //WolfHole wolfHoleUnderneath = (WolfHole) objectUnderneath;

        if (objectUnderneath.equals(this.getPack().getWolfHole())) {
            world.remove(this);
            setSleeping(true);

            // System.out.println("Rabbit(s) resting: " + this.rabbits);
            // System.out.println(" -In " + world.getTile(world.getLocation(this)));
        }
    }

    public void reproduce(World world) {
        
        // Failstates
        if(!hasPack())
            return;
        if (!isMature())
            return;
        if (!canAfford(reproductionCost))
            return;
        if (this.validateLocationExistence(world))
            return;

        // Main
        if (this.wolfPack.hasSpace() && this.wolfPack.getSize() > 1) {
            // Check if more than one wolf is in hole
            int wolvesInHole = 0;
            for (Wolf w : this.wolfPack.getWolfList()) {
                if (!w.validateLocationExistence(world)) {
                    wolvesInHole = wolvesInHole + 1;
                }
            }
            if (this.wolfPack.inHeat()) {
                if (wolvesInHole >= 2) {
                    if (this.wolfPack.getSize() >= 4) {
                        //System.out.println("make 2 babe");
                        this.makeBaby(world);
                        this.makeBaby(world);
                        this.wolfPack.postNutClarity();
                    } else {
                        this.makeBaby(world);
                        this.wolfPack.postNutClarity();
                    }
                }
            }

            else {
                this.wolfPack.getHorny();
            }
        }
    }

    public void makeBaby(World world) {
        if(!canAfford(reproductionCost))
            return;
        //System.out.println("make baby");

        Wolf baby = new Wolf();
        baby.joinPack(getPack());
        world.add(baby);
        int tired = baby.bedtime;
        baby.sleepyness = baby.sleepyness + tired;
        baby.setSleeping(true);
        

        changeEnergy(reproductionCost, world);;
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
        if (!world.isOnTile(this.getPack().getWolfHole()))
            return;

        Location exitLocation = world.getLocation(this.getPack().getWolfHole());
        if (!world.isTileEmpty(exitLocation))
            return;

        // System.out.println("Go to tile:" + exitLocation);
        world.setTile(exitLocation, this);
        setSleeping(false);
    }
    /*
     * $$\
     * $$ |
     * $$$$$$\ $$$$$$\ $$$$$$$\ $$ | $$\
     * $$ __$$\ \____$$\ $$ _____|$$ | $$ |
     * $$ / $$ | $$$$$$$ |$$ / $$$$$$ /
     * $$ | $$ |$$ __$$ |$$ | $$ _$$<
     * $$$$$$$ |\$$$$$$$ |\$$$$$$$\ $$ | \$$\
     * $$ ____/ \_______| \_______|\__| \__|
     * $$ |
     * $$ |
     * \__|
     */

    /**
     * A function to join all wolfpack related acitons
     * 
     * @param world
     */
    public void handlePack(World world) {
        if (!validateLocationExistence(world))
            return;
        if (hasPack()) {
            if (getPack().getSize() <= 1) { // if you are in a 1 size pack just leave
                getPack().removeWolf(this);
                leavePack();
            }
            if(isHungry()&&!getPack().getHunting()) {
                // 
                getPack().startHunting();
            }
        }
        if (!hasPack()) { // if you dont have a pack find one
            searchForPack(world);
        }
    }

    /**
     * Searches nearby tiles for wolfs and creates a pack with them if the
     * conditions are right.
     * 
     * @param world
     */
    public void searchForPack(World world) {
        for (Location l : world.getSurroundingTiles()) {
            Object o = world.getTile(l);
            if (o instanceof Wolf) {
                if (((Wolf) o).hasPack()) {// if the target wolf has a pack
                    if (((Wolf) o).getPack().hasSpace()) // if there is space
                        joinPack(((Wolf) o).getPack());
                } else { // if the target wolf does not have a pack
                    createPack(world);
                    ((Wolf) o).joinPack(wolfPack);
                }
            }
        }
    }

    public void createPack(World world) {
        wolfPack = new WolfPack();
        joinPack(wolfPack);
    }

    public void joinPack(WolfPack wolfPack) {
        // System.out.println("I "+this+" joined "+wolfPack);
        this.wolfPack = wolfPack;
        wolfPack.addWolf(this);
    }

    public void leavePack() {
        // System.out.println("I "+this+" left "+wolfPack);
        wolfPack.removeWolf(this);
        this.wolfPack = null;
    }

    public WolfPack getPack() {
        return wolfPack;
    }

    public boolean hasPack() {
        return wolfPack != null;
    }

}
