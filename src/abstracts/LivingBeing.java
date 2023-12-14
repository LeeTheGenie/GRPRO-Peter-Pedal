package abstracts;
import itumulator.simulator.Actor;
import itumulator.world.World;

public abstract class LivingBeing implements Actor {

    protected int age; // The age a of a being
    protected int maxAge; // The max Age of a being

    @Override public void act(World world) {
        ageUp(world);
    }

    protected LivingBeing(int age, int maxAge) {
        this.age = age;
        this.maxAge = maxAge;
    }

    public void ageUp(World world) {
        age++;
        if (age >= maxAge) {
            die(world);
        }
    }

    public LivingBeing newInstance() {
        return null;// return new LivingBeing(0, maxAge);
    }

    public void die(World world) {
        world.delete(this);
    }

    public boolean validateExistence(World world) {
        return world.contains(this);
    }

    public boolean validateLocationExistence(World world) {
        if(validateExistence(world)) {
            return world.isOnTile(this);
        }
        return false;
    }

    public void die(World world, String reason) {
        die(world);
        if (!(this instanceof Plant))
            System.out.println("I \"" + this.getClass() + "\" died of " + reason + " at age: " + age);

    }    

}