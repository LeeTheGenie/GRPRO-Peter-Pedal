package abstracts;

import itumulator.world.World;
import itumulator.world.Location;

public class Animal extends LivingBeing {

    protected int currentEnergy;
    protected int maxEnergy;
    protected int trueMaxEnergy;

    protected Animal(int age, int maxAge, int maxEnergy) {
        super(age, maxAge);
        this.currentEnergy = maxEnergy;
        this.maxEnergy = maxEnergy;
        this.trueMaxEnergy = maxEnergy;
    }

    @Override
    public void act(World world) {
        if (currentEnergy == 0) {
            System.out.println("I \"" + this.getClass() + "\" died of energyloss at age: " + age);
            die(world);
        }
        super.act(world);
    }

    @Override
    public LivingBeing newInstance() {
        return new Animal(0, maxAge, trueMaxEnergy);
    }

    /**
     * Moves the object one tile per step.
     * 
     * @param world
     * @param to
     *              The Location the object is going towards.
     * @param from
     *              The current location of the object. "world.getLocation(this)"
     */
    public void toAndFrom(World world, Location to, Location from) {
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
        System.out.println("going to " + newLocation);

        world.move(this, newLocation);

    }
}