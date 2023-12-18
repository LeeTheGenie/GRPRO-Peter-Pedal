package abstracts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import misc.Carcass;

public abstract class Animal extends LivingBeing implements DynamicDisplayInformationProvider  {

    // Energy 
    protected int currentEnergy, maxEnergy, trueMaxEnergy, metabloicRate;
    protected boolean resting, sleeping;

    // Movement
    protected int movementCost;

    // Reproduction
    protected int reproductionCost, matureAge, inheritedEnergy;

    // displayinformation [adult/notadult][sleeping/notsleeping]
    public String[][] growthStates;


    protected Animal(int age, int maxAge, int maxEnergy, int matureAge, int movementCost, int reproductionCost,
            int inheritedEnergy, int metabloicRate) {
        super(age, maxAge);
        this.currentEnergy = maxEnergy;
        this.maxEnergy = maxEnergy;
        this.trueMaxEnergy = maxEnergy;
        this.matureAge = matureAge;
        this.movementCost = movementCost;
        this.reproductionCost = reproductionCost;
        this.inheritedEnergy = inheritedEnergy;
        resting = false;
        this.movementCost = movementCost;
        this.sleeping = false;
    }

    @Override public void act(World world) {
        if (!resting)
            changeEnergy(-metabloicRate, world);

        super.act(world);
    }

    @Override public LivingBeing newInstance() {
        return null;// new Animal(0, maxAge, trueMaxEnergy);
    }

    public boolean canAfford(int cost) {
        return (currentEnergy - cost > 0);
    }

    public boolean isMature(){
        return (age>=matureAge);
    }

    public void changeEnergy(int change, World world) {
        currentEnergy += change;

        if (currentEnergy > maxEnergy) { // hvis den er større end max, bare set den til max fordi det er max duh
            currentEnergy = maxEnergy;
            return;
        }

        if (currentEnergy <= 0 && matureAge < age) { // grace period when below mature age
            die(world, "energyloss");
        }
    }

    public void setBaby() {
        currentEnergy = inheritedEnergy;
    }

    @Override public void die(World world) {
        if(!validateExistence(world))
            return;   
         
        if(validateLocationExistence(world)) {
            Location deathLocation = world.getLocation(this);
            world.delete(this);
            dropCarcass(world,deathLocation);
        } else {
            world.delete(this);
        }
    }

    public void dropCarcass(World world,Location location){
        int energyToDrop = (int) ((int) currentEnergy + Math.floor((maxEnergy)*0.1d));
        world.setTile(location, new Carcass(0, 0,energyToDrop));
    }

    /*                                                                             $$\     
                                                                                $$ |    
    $$$$$$\$$$$\   $$$$$$\ $$\    $$\  $$$$$$\  $$$$$$\$$$$\   $$$$$$\  $$$$$$$\ $$$$$$\   
    $$  _$$  _$$\ $$  __$$\\$$\  $$  |$$  __$$\ $$  _$$  _$$\ $$  __$$\ $$  __$$\\_$$  _|  
    $$ / $$ / $$ |$$ /  $$ |\$$\$$  / $$$$$$$$ |$$ / $$ / $$ |$$$$$$$$ |$$ |  $$ | $$ |    
    $$ | $$ | $$ |$$ |  $$ | \$$$  /  $$   ____|$$ | $$ | $$ |$$   ____|$$ |  $$ | $$ |$$\ 
    $$ | $$ | $$ |\$$$$$$  |  \$  /   \$$$$$$$\ $$ | $$ | $$ |\$$$$$$$\ $$ |  $$ | \$$$$  |
    \__| \__| \__| \______/    \_/     \_______|\__| \__| \__| \_______|\__|  \__|  \____/                                                                                   
    */
    
    /**
     * a free move
     * @param world
     * @param target
     */
    private void freeMove(World world, Location target) {
        if (target == null) { // random
            Set<Location> neighbors = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbors);
            if (list.size() == 0)
                return;
            target = list.get(new Random().nextInt(list.size()));
        }
        if (!world.isTileEmpty(target))
            return;

        world.move(this, target);
    }

    /**
     * Moves to a certain location, if target is null move to a random location
     */
    public void move(World world, Location target) {
        if (!canAfford(movementCost)) return;
        if (!world.isOnTile(this)) return;
        freeMove(world,target);  
        changeEnergy(-movementCost, world);
    }

    public void push(World world) {
        freeMove(world, null);
    }

    /**
     * Returns a location one step closer to the 'to' location.
     * 
     * @param world
     * @param to
     *              The Location the object is going towards.
     * @param from
     *              The current location of the object. "world.getLocation(this)"
     */
    public Location toAndFrom(World world, Location to, Location from) {

        int x = from.getX();
        int y = from.getY();

        if (to.getX() != from.getX()) {
            if (to.getX() > from.getX()) {
                x = from.getX() + 1;
            }

            if (to.getX() < from.getX()) {
                x = from.getX() - 1;
            }
        }

        if (to.getY() != from.getY()) {
            if (to.getY() > from.getY()) {
                y = from.getY() + 1;
            }

            if (to.getY() < from.getY()) {
                y = from.getY() - 1;
            }
        }

        Location newLocation = new Location(x, y);
        // System.out.println("going to "+newLocation);

        //world.move(this, newLocation);
        return newLocation;
    }

    /**
     * PLEASE PROVIDE JAVADOC
     * @param world
     * @param to
     * @param from
     */
    public void toAndFromBesides(World world, Location to, Location from) {

        int x = from.getX();
        int y = from.getY();

        if (to.getX() != from.getX()) {
            if (to.getX() > from.getX()) {
                x = from.getX() + 1;
            }

            if (to.getX() < from.getX()) {
                x = from.getX() - 1;
            }
        }

        if (to.getY() != from.getY()) {
            if (to.getY() > from.getY()) {
                y = from.getY() + 1;
            }

            if (to.getY() < from.getY()) {
                y = from.getY() - 1;
            }
        }

        Location newLocation = new Location(x, y);
        // System.out.println("going to "+newLocation);

        world.move(this, newLocation);

    }

   
        /*                                                             
    $$$$$$$\  $$$$$$$\ $$$$$$\  $$$$$$$\  $$$$$$$\   $$$$$$\   $$$$$$\  
    $$  _____|$$  _____|\____$$\ $$  __$$\ $$  __$$\ $$  __$$\ $$  __$$\ 
    \$$$$$$\  $$ /      $$$$$$$ |$$ |  $$ |$$ |  $$ |$$$$$$$$ |$$ |  \__|
    \____$$\ $$ |     $$  __$$ |$$ |  $$ |$$ |  $$ |$$   ____|$$ |      
    $$$$$$$  |\$$$$$$$\\$$$$$$$ |$$ |  $$ |$$ |  $$ |\$$$$$$$\ $$ |      
    \_______/  \_______|\_______|\__|  \__|\__|  \__| \_______|\__|                                                                   
        */

    /*public ArrayList<Object> locateTarget(World world, int range,Class<LivingBeing> searchObject) {
        if (!validateExistence(world)) return null;

        Location currentLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, range);
        ArrayList<Object> returningObjects = new ArrayList<Object>();

        for (Location l : surroundingTiles) {
            Object target = world.getTile(l);
            if(target instanceof searchObject) {

            }

        }
        return returningObjects;
    }*/

    public Integer getDistance(World world, LivingBeing o) {
        if(!validateLocationExistence(world)||!o.validateExistence(world)) return null;

        int deltaX = Math.abs(world.getLocation(this).getX()-world.getLocation(o).getX()),
            deltaY = Math.abs(world.getLocation(this).getY()-world.getLocation(o).getY());

        return Math.min(deltaX,deltaY); 
    }
}