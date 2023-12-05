package misc;

import abstracts.LivingBeing;
import itumulator.world.World;

public class SmallCarcass extends LivingBeing {


    public int decay = 50;



    @Override
    public void act(World world) {
        decay = decay - 1;
        decayed();
    }

    @Override
    public SmallCarcass newInstance() {
        return new SmallCarcass();
    }

    public SmallCarcass() {
        super(0, 0);
    }

    public void decayed(World world){
        if(decay<=0){
            world.delete(this),
        }

    }
}
