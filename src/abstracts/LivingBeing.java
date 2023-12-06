package abstracts;

import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import misc.SmallCarcass;

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
            die(world);
        }
    }

    public LivingBeing newInstance() {
        return null;// return new LivingBeing(0, maxAge);
    }

    public void die(World world) {
        onDeath(world);

        try {
            Location deathLocation = world.getLocation(this);
            boolean isAnimal = false;

            if (this instanceof Animal) {
                isAnimal = true;

            }

            world.delete(this);

            if (isAnimal) {
                world.setTile(deathLocation, new SmallCarcass());
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    public void die(World world, String reason) {
        die(world);
        if (!(this instanceof Plant))
            System.out.println("I \"" + this.getClass() + "\" died of " + reason + " at age: " + age);

    }

    public void onDeath(World world) {

    }
}