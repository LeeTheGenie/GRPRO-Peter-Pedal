package misc;

import abstracts.LivingBeing;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class TrapActivated extends LivingBeing implements NonBlocking{
    private int supply;

    @Override
    public TrapActivated newInstance() {
        return new TrapActivated(supply);
    }

    public TrapActivated(int supply) {
        super(0,0);
        this.supply=supply;
    }

    @Override
    public void act(World world){


    }

    public void claim(World world){
        world.delete(this);
        //create a carcass with size dependent on this.supply 
        
    }
}
