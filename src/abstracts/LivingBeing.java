package abstracts;

import itumulator.simulator.Actor;
import itumulator.world.World;

public abstract class LivingBeing implements Actor {

    protected int age; // The age a of a being
    protected int maxAge; // The max Age of a being

    @Override
    public void act(World world) {
        ageUp(world);
    }

    protected LivingBeing(int age, int maxAge) {
        this.age = age;
        this.maxAge = maxAge;

    }

    public void ageUp(World world) {
        age++;
        if (age >= maxAge) {
            if (!(this instanceof Plant))
                System.out.println("I \"" + this.getClass() + "\" died of old age at age: " + age);
            die(world);
        }
    }

    public LivingBeing newInstance() {
        return null;// return new LivingBeing(0, maxAge);
    }

    public void die(World world) {
        onDeath();
        try {
            world.delete(this);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onDeath() {
        // If a being does something when they die
        // Perhaps create a corpse on the tile they stand?
    }
}