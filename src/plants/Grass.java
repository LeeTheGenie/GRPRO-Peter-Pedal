package plants;
import abstracts.Plant;
import itumulator.world.NonBlocking;

public class Grass extends Plant implements NonBlocking {
    public Grass() {
        super(0, 30, 10);
    }

    @Override public Grass newInstance() {
        return new Grass();
    }
}