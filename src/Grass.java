import itumulator.world.NonBlocking;

public class Grass implements NonBlocking {
    private int lifeThreshold;
    private int lifeCount;
    private int spread;

    public Grass() {
        this.lifeThreshold = 30;
        this.lifeCount = 0;
        this.spread = 10;
    }
}
