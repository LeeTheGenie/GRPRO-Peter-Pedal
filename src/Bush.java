import abstracts.Plant;

public class Bush extends Plant {
    public Bush() {
        super(0, 50, 20);
    }

    @Override
    public Bush newInstance() {
        return new Bush();
    }
}