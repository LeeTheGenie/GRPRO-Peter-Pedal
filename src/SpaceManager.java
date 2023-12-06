import abstracts.LivingBeing;
import itumulator.world.NonBlocking;
public class SpaceManager {
    
    int space; 
    int[] addedObjects;

    SpaceManager(int space) {
        this.space = space*space;
        addedObjects = new int[]{0,0,0};
    }

    public boolean isThereSpaceFor(LivingBeing livingBeing) {
        int zPointer = getZ(livingBeing);
        if (addedObjects[zPointer] >= space) {
            return false;
        } else {
            return true;
        }
    }

    public void incrementDimension(LivingBeing livingBeing) {
        int zPointer = getZ(livingBeing);
        addedObjects[zPointer]++;
    }

    public int getZ(LivingBeing livingBeing) {
        int zPointer = 1;
        if (livingBeing instanceof NonBlocking)
            zPointer = 0;
        return zPointer;
    }

    public int getSpace() {
        return this.space;
    }

    public int getEmptySpace(int zPointer) {
        return space-addedObjects[zPointer];
    }


}