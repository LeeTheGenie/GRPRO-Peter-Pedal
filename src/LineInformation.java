import abstracts.LivingBeing;
import itumulator.world.Location;

public class LineInformation {
    public LivingBeing livingBeing;
    public int minAmount, maxAmount;
    public Location spawnLocation;
    public boolean fungi,cordyceps;

    LineInformation(LivingBeing livingBeing,int minAmount,int maxAmount,Location spawnLocation,boolean fungi,boolean cordyceps) {
        this.livingBeing = livingBeing;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.spawnLocation = spawnLocation;
        this.fungi = fungi; 
        this.cordyceps = cordyceps;
    }
}