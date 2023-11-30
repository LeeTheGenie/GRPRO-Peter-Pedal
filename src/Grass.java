import abstracts.Plant;

public class Grass extends Plant {
    public Grass() {
        super(0, 30, 10);
    }

    @Override
    public Grass newInstance() {
        return new Grass();
    }
}