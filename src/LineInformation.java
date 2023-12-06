import abstracts.LivingBeing;
import itumulator.world.Location;

public class LineInformation {
    public LivingBeing livingBeing;
    public int minAmount, maxAmount;
    public Location spawnLocation;

    LineInformation(LivingBeing livingBeing,int minAmount,int maxAmount,Location spawnLocation) {
        this.livingBeing = livingBeing;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.spawnLocation = spawnLocation;
    }
}