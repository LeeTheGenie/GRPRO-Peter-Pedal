package abstracts;
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




    @Override
    public LivingBeing newInstance() {
        return null;//new Animal(0, maxAge, trueMaxEnergy);
    }
}