package misc;

import abstracts.LivingBeing;
import itumulator.world.World;

public class SmallCarcass extends LivingBeing {


    public int decay = 50;



    @Override
    public void act(World world) {
        decay(world);

    }

    @Override
    public SmallCarcass newInstance() {
        return new SmallCarcass();
    }

    public SmallCarcass() {
        super(0, 0);
    }

    public void decay(World world){
        
        if(this.decay<=0){
            world.delete(this);
        }
        else
            this.decay = this.decay-1;

    }
}
