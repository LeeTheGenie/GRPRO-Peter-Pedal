public class Grass extends Plant {
    public Grass() {
        super(0,31,10);
    }
    @Override public Plant CreateNew(){
        return new Grass();
    }
}