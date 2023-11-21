public class Flower extends Plant {
    public Flower() {
        super(0,50,20);
    }
    @Override public Plant CreateNew(){
        return new Flower();
    }
}