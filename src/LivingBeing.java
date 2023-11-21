import itumulator.simulator.Actor;
import itumulator.world.World;
import java.awt.Color;

public class LivingBeing implements Actor {

    int age;        // The age a of a being
    int maxAge;     // The max Age of a being

    @Override public void act(World world) {
        // This is the ultimate being in the world of ideas - it does not act
    }

    LivingBeing(int age, int maxAge) {
        this.age = age;
        this.maxAge = maxAge;
        
    }

    public void ageUp(World world) {
        age++;
        if(age>=maxAge) {
            die(world);
        }
    }

    public void die(World world) {
        onDeath();
        world.delete(this);
    }

    public void onDeath() {
        // If a being does something when they die
        // Perhaps create a corpse on the tile they stand?
    }
}