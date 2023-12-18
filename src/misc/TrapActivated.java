package misc;

import itumulator.world.World;

public class TrapActivated extends Trap {
    @Override
    public TrapActivated newInstance() {
        return new TrapActivated();
    }

    public TrapActivated() {
        super();
    }

    public void claim(World world){
        world.delete(this);
    }
}
