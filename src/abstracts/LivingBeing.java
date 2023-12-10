package abstracts;

import java.lang.reflect.Type;

import animal.Bear;
import animal.Rabbit;
import animal.Wolf;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import misc.Carcass;

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
        onDeath(world);
        if(world.isOnTile(this)){
            dropCarcass(world);
        }
            
        else{
            world.delete(this);
        }
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

    public void onDeath(World world) {

    }

    public void dropCarcass(World world){
            Location deathLocation = world.getLocation(this); 
            boolean isRabbit=false;
            boolean isWolf=false;
            boolean isBear=false;

            if(this instanceof Rabbit){
                 isRabbit=true;
            }
            if(this instanceof Wolf){
                 isWolf=true;
            }
            if(this instanceof Bear){
                isBear=true;
            }  

            world.delete(this);

            if(isRabbit){
                world.setTile(deathLocation, new Carcass(0, 0, 50));}

            if(isBear){
                world.setTile(deathLocation, new Carcass(0, 0, 200));}
            
            if(isWolf){
                world.setTile(deathLocation, new Carcass(0, 0, 120));}            
    }

}