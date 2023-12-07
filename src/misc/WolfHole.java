package misc;

import itumulator.world.NonBlocking;
import itumulator.world.World;

import abstracts.LivingBeing;
import animal.WolfPack;

public class WolfHole extends LivingBeing implements NonBlocking {

    WolfPack owner;

    public WolfHole() {
        super(0, 0);
        this.owner = null; 
    }

    public boolean isClaimed() {
        if (this.owner == null)
            return false;
        return true;
    }

    public void setOwner(WolfPack owner) {
        // System.out.println();
        if (!isClaimed()) {
            this.owner = owner;
            this.age = 0;
        } else {
            throw new Error("Cannot asign a new owner to a hole with an already existing owner.");
        }
    }

    public void clearOwner() {
        this.owner = null;
    }

    public WolfPack getOwner() {
        return owner;
    }

    @Override
    public WolfHole newInstance() {
        return new WolfHole();
    }

    @Override
    public void act(World world) {
        if (!isClaimed())
            super.act(world);
    }
}