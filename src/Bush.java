
import abstracts.LivingBeing;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Bush extends LivingBeing implements NonBlocking {

    @Override
    public void act(World world) {
    }

    @Override
    public Bush newInstance() {
        return new Bush();
    }

    public Bush() {
        super(0, 0);
    }
}
