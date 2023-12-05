package abstracts;

import itumulator.world.World;
import itumulator.world.Location;

public abstract class Predator extends Animal {

    /**
     * At what pct energy does the predator feel hungry at.
     * Ex: hunger = 0.80d
     */
    protected double hunger;

    protected Location targetLocation;

    protected Predator(int age, int maxAge, int maxEnergy, int matureAge, int movementCost,
            int reproductionCost, int inheritedEnergy, int metabloicRate, double hunger) {
        super(age, maxAge, maxEnergy, matureAge, movementCost, reproductionCost, inheritedEnergy, metabloicRate);
        this.hunger = hunger;
    }

    @Override
    public void act(World world) {
        super.act(world);
    }

    /**
     * 
     * @param livingBeing
     * @return
     * 
     */
    protected boolean canEat(LivingBeing b) {
        return false;
    }
}