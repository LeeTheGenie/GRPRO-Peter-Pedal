package abstracts;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal extends LivingBeing {

    // Energy
    protected int currentEnergy, maxEnergy, trueMaxEnergy, metabloicRate;
    protected boolean resting;

    // Movement 
    protected int movementCost;
    
    // Reproduction
    protected int reproductionCost, matureAge, inheritedEnergy;

    protected Animal(int age, int maxAge, int maxEnergy,int matureAge,int movementCost,int reproductionCost,int inheritedEnergy,int metabloicRate) {
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
    }

    @Override public void act(World world) {
        if(!resting)
            changeEnergy(-metabloicRate,world);
        
        super.act(world);
    }

    

    /**
     * Returns true if (currentEnergy - cost != 0)
     */
    public boolean canAfford(int cost) {
        if(currentEnergy-cost!=0)
            return true; 
        return false;
    }

    public void changeEnergy(int change,World world) {
        currentEnergy += change;

        if (currentEnergy > maxEnergy) { // hvis den er større end max, bare set den til max fordi det er max duh
            currentEnergy = maxEnergy;
            return; 
        }

        if (currentEnergy == 0 &&matureAge<age) {  // grace period when below mature age
            System.out.println("I \"" + this.getClass() + "\" died of energyloss at age: " + age);
            die(world);
        }
    }

    public void setBaby() {
        currentEnergy = inheritedEnergy;
    }

    /** 
     * Moves to a certain location, if target is null move to a random location
    */

    public void move(World world, Location target) {
        if(!canAfford(movementCost))
            return;
        if(!world.isOnTile(this))
            return;

        if(target==null) { // random
            Set<Location> neighbors = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbors);
            if(list.size()==0)
                return;
            target = list.get(new Random().nextInt(list.size()));
        }
        if(!world.isTileEmpty(target))
            return; 

        world.move(this, target);
        changeEnergy(-movementCost, world);
    }

    public Location toAndFrom(World world, Location to, Location from){
        int x=from.getX(); 
        int y=from.getY(); 

        if(to.getX()!=from.getX()){
            if(to.getX()>from.getX()){
                        x=from.getX()+1;
                    }

            if(to.getX()<from.getX()){
                        x=from.getX()-1;
                    }
        }
        
        if(to.getY()!=from.getY()){
            if(to.getY()>from.getY()){
                        y=from.getY()+1;
                    }

            if(to.getY()<from.getY()){
                        y=from.getY()-1;
                    }
        }

        Location newLocation = new Location(x, y);
        //System.out.println("going to "+newLocation);

        //world.move(this, newLocation);
        return newLocation;
    }




    @Override
    public LivingBeing newInstance() {
        return null;//new Animal(0, maxAge, trueMaxEnergy);
    }
}