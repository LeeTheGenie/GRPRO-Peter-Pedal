package misc;

import itumulator.world.NonBlocking;
import itumulator.world.World;

import abstracts.LivingBeing;
import animal.Rabbit;

public class RabbitHole extends LivingBeing implements NonBlocking {

    Rabbit owner;

    public RabbitHole(Rabbit owner) {
        super(0, 10);
        this.owner = owner;
    }

    public boolean isClaimed() {
        if (this.owner == null)
            return false;
        return true;
    }

    public void setOwner(Rabbit owner) {
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

    public Rabbit getOwner() {
        return owner;
    }

    @Override
    public RabbitHole newInstance() {
        return new RabbitHole(null);
    }

    @Override
    public void act(World world) {
        if (!isClaimed())
            super.act(world);
    }
}