package misc;

import abstracts.LivingBeing;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class SmallCarcass extends LivingBeing implements NonBlocking {

    @Override
    public void act(World world) {
    }

    @Override
    public SmallCarcass newInstance() {
        return new SmallCarcass();
    }

    public SmallCarcass() {
        super(0, 0);
    }
}
