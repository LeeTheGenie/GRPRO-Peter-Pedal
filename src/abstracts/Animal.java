package abstracts;
import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal extends LivingBeing {

    // Energy
    protected int currentEnergy, maxEnergy, trueMaxEnergy;

    // Movement 
    protected int movementCost;
    
    // Reproduction
    protected int reproductionCost, matureAge, inheritedEnergy;

    protected Animal(int age, int maxAge, int maxEnergy,int matureAge,int movementCost,int reproductionCost,int inheritedEnergy) {
        super(age, maxAge);
        this.currentEnergy = maxEnergy;
        this.maxEnergy = maxEnergy;
        this.trueMaxEnergy = maxEnergy;
        this.matureAge = matureAge;
        this.movementCost = movementCost;
        this.reproductionCost = reproductionCost;
        this.inheritedEnergy = inheritedEnergy;
    }

    @Override public void act(World world) {
        if (currentEnergy == 0) {
            System.out.println("I \"" + this.getClass() + "\" died of energyloss at age: " + age);
            die(world);
        }
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

    /**
     * Moves the object one tile per step. 
     * @param world
     * @param to
     * The Location the object is going towards.
     * @param from
     * The current location of the object. "world.getLocation(this)"
     */
    public void toAndFrom(World world, Location to, Location from){
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
        System.out.println("going to "+newLocation);

        world.move(this, newLocation);
        
    }




    @Override
    public LivingBeing newInstance() {
        return null;//new Animal(0, maxAge, trueMaxEnergy);
    }
}