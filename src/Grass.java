public class Grass extends Plant {
    public Grass() {
        super(0,30,10);
    }
    @Override public Plant CreateNew(){
        return new Grass();
    }
}