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

    /**
     * Attacks a prey and kills it, and spawns a carcass on the same tile. If there
     * is already a non-blocking object it just replaces it.
     * 
     * @param world
     */
    public void attackPrey(World world) {
        ((LivingBeing) world.getTile(targetLocation)).die(world, "killed by bear");

        if (world.containsNonBlocking(targetLocation)) {
            world.delete(world.getNonBlocking(targetLocation));
        }
        world.setTile(targetLocation, new SmallCarcass());

        currentEnergy -= 3;
        System.out.println("dræbt");
    }

    /**
     * Moves the object to a prey then kills it and eats it.
     * 
     * @param world
     */
    public void hunt(World world) {

        world.move(this, toAndFrom(world, world.getLocation(this), targetLocation)); // crasher fordi bear
                                                                                     // prøver at
        // stille sig på samme tile som
        // rabbit, men der kan kun være en
        // blocking, toAndFrom skal gøre
        // så objekt stiller sig ved
        // siden af og ikke ovenpå
        attackPrey(world);
        eatPrey(world);
    }

    /**
     * Eats the prey and increases currentenergy. But if energy is already max then
     * the method return and does noting.
     * 
     * @param world
     */
    public void eatPrey(World world) {
        int energyIncrement = 5;
        if (currentEnergy == maxEnergy) // hvis du ikke gavner af at spise så lad vær
            return;
        currentEnergy += energyIncrement;
        if (currentEnergy > maxEnergy) // hvis den er større end max, bare set den til max fordi det er max duh
            currentEnergy = maxEnergy;
        world.delete(world.getTile(targetLocation));
    }
}