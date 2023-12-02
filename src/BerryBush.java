import abstracts.Plant;

public class BerryBush extends Plant {
    public BerryBush() {
        super(0, 50, 20);
    }

    @Override
    public BerryBush newInstance() {
        return new BerryBush();
    }
}