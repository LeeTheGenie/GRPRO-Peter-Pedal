
import abstracts.LivingBeing;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class BerryBush extends LivingBeing implements NonBlocking {

    @Override
    public void act(World world) {
    }

    @Override
    public BerryBush newInstance() {
        return new BerryBush();
    }

    public BerryBush() {
        super(0, 0);
    }
}
