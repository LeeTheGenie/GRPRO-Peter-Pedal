public class Flower extends Plant {
    public Flower() {
        super(0,50,20);
    }
    
    @Override public Flower newInstance() {
        return new Flower();
    }
}