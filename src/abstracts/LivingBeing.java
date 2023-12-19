package abstracts;
import itumulator.simulator.Actor;
import itumulator.world.Location;
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
    /**
     * Returns the distance (in tiles) between this and the target as an int
     * @param world
     * @param target
     * @return
     */
    public int getDistance(World world, LivingBeing target) {
        if(!validateLocationExistence(world)||!target.validateExistence(world)) throw new Error("Target or this does not exist in the world.");

        int deltaX = Math.abs(world.getLocation(this).getX()-world.getLocation(target).getX()),
            deltaY = Math.abs(world.getLocation(this).getY()-world.getLocation(target).getY());

        return Math.max(deltaX,deltaY); 
    }

    /**
     * Returns a vector represented by Location. 
     * @param world
     * @param from
     * @param to
     * @return
     */
    public Location drawVector(World world, LivingBeing from,LivingBeing to) {
        if(!from.validateLocationExistence(world)||!to.validateExistence(world)) throw new Error("Target or this does not exist in the world.");

        int deltaX = world.getLocation(to).getX()-world.getLocation(from).getX(),
            deltaY = world.getLocation(to).getY()-world.getLocation(from).getY();

        Location vector = new Location(deltaX, deltaY);

        return vector; 
    }
    /**
     * Returns the sum of two vectors (Location) a and b
     * @param a
     * @param b
     * @return
     */
    public Location addVectors(Location a, Location b) {
        int sumX = a.getX()+b.getX(),
            sumY = a.getY()+b.getY();

        return new Location(sumX,sumY);
    }

    /**
     * Vector (Location) + this.getLocation
     * @param world
     * @param vector
     * @return
     */
    public Location getLocationFromVector(World world, Location vector) {
        return addVectors(world.getLocation(this), vector);
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