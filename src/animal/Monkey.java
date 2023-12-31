package animal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Predator;

import plants.BerryBush;
import plants.Bush;
import executable.DisplayInformation;

import itumulator.world.World;

import misc.Carcass;
import misc.MonkeyFamily;
import misc.Trap;
import misc.TrapActivated;

import itumulator.world.Location;

public class Monkey extends Predator {

    private Location foodLocation;
    private boolean hasSticks;
    private boolean hasBerries;
    private Set<Location> traps;
    private Location trapLocation;
    private MonkeyFamily family;
    private int children;
    private int sleepyness;
    private int bedtime;
    private boolean parent;
    private int gender;

    public Monkey() {
        super(0, 100, 300, 1, 1, 10, 0, 2, 1.2d);
        growthStates = new String[][] { { "monkey-small", "monkey-small-sleeping" }, { "monkey", "monkey-sleeping" } };
        this.foodLocation = null;
        this.trapLocation = null;
        this.traps = new HashSet<>();
        this.hasSticks = false;
        this.hasBerries = false;
        this.family = null;
        this.children = 0;
        this.sleepyness = 0;
        this.bedtime = 30;
        this.gender = 0;
    }

    @Override
    public Monkey newInstance() {
        return new Monkey();
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = (matureAge <= age) ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }

    @Override
    public void act(World world) {

        if (!sleeping) {
            mature();
            handleFamily(world);
            reproduce(world);
            move(world, null);

            if (!isAdult()) {
                followAdult(world);
            }

            if (isHungry() && isAdult()) {
                handleHunger(world);
            }
        }
        
        handleSleep(world);
        super.act(world);


        // TODO: reproduce, dynamicdisplayinfo med stick, sleep handling,
        // can not reproduce
        // does not change display with stick
        // can not sleep properly
        // can move while sleeping

    }

    @Override
    public void die(World world) {
        super.die(world);
        leaveFamily();
    }

    /**
     * Makes the monkey reproduce with another monkey. If the monkey does not have a
     * family it will search for one, if it does it will simply search for a mate in
     * the family.
     * 
     * @param world
     */
    public void reproduce(World world) {
        if (!isAdult()) { // ikke for børn 🫡
            return;
        }
        if (!hasFamily()) {
            return;
        }
        if (!canAfford(reproductionCost)) {
            return;
        }
        if (!validateLocationExistence(world)) {
            return;
        }
        MonkeyFamily family =this.getFamily();
        if(family.getSize()>=2){
            int parents = 0;
            for(Monkey m : this.family.getFamily()){
                if(m.parent)
                    parents=parents+1;
            }
            if (parents==2&&family.hasSpace()&&family.inHeat()) {
                makeBaby(world);
                family.postNutClarity();
            }
            else
                family.getHorny();

        }
    }



    private void makeBaby(World world) {
        if(!canAfford(reproductionCost))
            return;
        //System.out.println("make baby");

        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (list.size() == 0)
            return;
        Location newLocation = list.get(new Random().nextInt(list.size()));


        Monkey baby = new Monkey();
        baby.joinFamily(this.getFamily());
        world.setTile(newLocation,baby);
        
        changeEnergy(reproductionCost, world);;
    }

    /**
     * Checks if the monkey is an adult.
     * 
     * @return true is adult, false if not adult.
     */
    public boolean isAdult() {
        return (age >= matureAge);
    }

    /**
     * Follows adult monkey.
     * 
     * @param world
     */
    public void followAdult(World world) {
        if (!hasFamily()) {
            return;
        }
        MonkeyFamily family = getFamily();
        for (Monkey m : family.getFamily()) {
            if (m.isAdult()) {
                move(world, toAndFrom(world, world.getLocation(m), world.getLocation(this)));
                break;
            }
        }
    }

    /**
     * Checks the the monkey trap for contents if there is any it eats it.
     * 
     * @param world
     */
    public void checkTraps(World world) {
        for (Location l : traps) {
            if (world.getNonBlocking(l) instanceof TrapActivated) {
                trapLocation = l;
                //System.out.println("set traplocation");
                move(world, toAndFrom(world, trapLocation, world.getLocation(this)));
                //System.out.println("moved to trap");
                claimTrap(world);
                break;
            } else {
                continue;
            }

        }
    }

    /**
     * Handles the hunger of the monkey.
     * 
     * @param world
     */
    public void handleHunger(World world) {
        if (hasTrapActivated(world)) {
            checkTraps(world);
        } else {
            if (hasSticks && hasBerries && traps.size() < 2) {
                buildTrap(world);
            } else {
                findAndEatFood(world);
            }
        }

    }

    public boolean hasTrapActivated(World world) {
        for (Location l : traps) {
            if (world.getNonBlocking(l) instanceof TrapActivated) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes the monkey sleep.
     * 
     * @param sleeping
     */
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
        this.resting = sleeping;
    }

    /**
     * Function to gather all sleep related acts
     */
    public void handleSleep(World world) {
        if (!world.isOnTile(this))
            return;
        if (sleeping) {
            if (sleepyness < 10) {
                setSleeping(false);
            }
            sleepyness -= 10;

        } else {
            sleepyness += 1;
            if (sleepyness >= 100) {
                die(world, "sleep-exhaustion");
            }
        }

        if (wantToSleep()) {
            setSleeping(true);
        }
    }

    /**
     * Checks if the monkey wants to sleep.
     * 
     * @return
     */
    public boolean wantToSleep() {
        return sleepyness > bedtime;
    }

    /**
     * Checks if the monkey has a family.
     * 
     * @return
     */
    public boolean hasFamily() {
        return (family != null);
    }

    /**
     * A function to join all monkeyfamily related acitons
     * 
     * @param world
     */
    public void handleFamily(World world) {
        if (!validateLocationExistence(world))
            return;

        
        if (!hasFamily()) {
            if (isAdult()&&!this.parent) {
                if(this.gender==1){
                    this.parent=true;
                    createFamily(world);
                    joinFamily(family);
                }else{
                    this.parent=true;
                }
            }else
                if (isAdult()&&!(this.gender==1)) {
                    searchForMate(world);
                }
                
        }
        if (isAdult()&&!this.parent) {
            leaveFamily();
        }
    }
    
    public void mature(){
        if (this.age==matureAge) {
            Random random = new Random();
            this.gender= random.nextInt(2);
        }

    }
    

    /**
     * Searches nearby tiles for monkeys and creates a family with them if the
     * conditions are right.
     * 
     * Only small monkeys can search for families
     * @param world
     */
    public void searchForMate(World world) {
        for (Location l : world.getSurroundingTiles()) {
            Object o = world.getTile(l);

            if (o instanceof Monkey) {
                if (((Monkey) o).hasFamily()&&((Monkey) o).getFamily().getSize()==1) {
                        joinFamily(((Monkey) o).getFamily());
                    
                } 
            }
        }
    }

    /**
     * Creates a family and joins it.
     * 
     * @param world
     */
    public void createFamily(World world) {
        family = new MonkeyFamily();
        joinFamily(family);
    }

    /**
     * Join a family.
     * 
     * @param family
     */
    public void joinFamily(MonkeyFamily family) {
        this.family = family;
        family.addMonkey(this);
    }

    /**
     * Leaves the family the monkey is in.
     */
    public void leaveFamily() {
        if (family == null) {
            return;
        }
        family.removeMonkey(this);
        this.family = null;
    }

    /**
     * Eats the contents of a trap if there is any.
     * 
     * @param world
     * @param trapLocation the location of the trap
     */
    public void claimTrap(World world) {
        TrapActivated trap = ((TrapActivated) world.getNonBlocking(trapLocation));
        trap.claim(world);
        changeEnergy(10*trap.supply, world);
        traps.remove(trapLocation);
        trapLocation = null;
    }

    /**
     * Returns the family of the monkey.
     * 
     * @return MonkeyFamily, null if the monkey does not have a family.
     */
    public MonkeyFamily getFamily() {
        return family;
    }

    /**
     * Builds a trap for rabbits to fall into.
     * 
     * @param world
     */
    public void buildTrap(World world) {
        if (world.containsNonBlocking(world.getLocation(this))) {
            return;
        }
        Trap trap = new Trap();
        world.setTile(world.getLocation(this), trap);
        hasSticks = false;
        hasBerries = false;
        traps.add(world.getLocation(trap));
    }

    /**
     * Finds and eats food.
     * 
     * @param world
     */
    public void findAndEatFood(World world) {
        if (foodLocation == null) {
            findFood(world);
            return;
        }
        if (world.getTile(foodLocation) instanceof Bush) {
            foodLocation = null;
            return;
        }
        move(world, toAndFrom(world, foodLocation, world.getLocation(this)));
        if (world.getTile(foodLocation) instanceof BerryBush) {
            BerryBush BerryBush = (BerryBush) world.getTile(foodLocation);
            BerryBush.setNoBerries(world);
            changeEnergy(4, world);
            hasSticks = true;
            hasBerries = true;
        } else if (world.getTile(foodLocation) instanceof Carcass) {
            Carcass carcass = (Carcass) world.getTile(foodLocation);
            carcass.takeBite();
            changeEnergy(10, world);
        }
    }

    /**
     * Finds the location of food in a three tile radius.
     * 
     * @param world
     */
    public void findFood(World world) {
        for (Location l : world.getSurroundingTiles(3)) {
            if (world.getTile(l) instanceof Carcass || world.getTile(l) instanceof BerryBush) {
                foodLocation = l;
            }
        }
    }

    /**
     * Function meant to be run on a livingBeing,
     * Checks if the livingBeing is eatable
     * 
     * @param livingBeing
     * @return true/false
     * 
     */
    public boolean canEat(World world, LivingBeing livingBeing) {
        if (livingBeing instanceof BerryBush) {
            return true;
        }
        if (livingBeing instanceof Carcass) {
            return true;
        }
        return false;
    }
}
