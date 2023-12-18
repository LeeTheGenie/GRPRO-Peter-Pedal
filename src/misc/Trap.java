package misc;

import abstracts.LivingBeing;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Trap extends LivingBeing implements NonBlocking {
    protected Boolean caught;
    protected Location spawnpoint;
    protected int supply;

    @Override
    public Trap newInstance() {
        return new Trap();
    }

    public Trap() {
        super(0, 0);
        this.caught = false;
        this.spawnpoint = null;
    }
    
    public void act(World world){
        setSpawnpoint(world);
        rabbitCaught(world);

    }

    public void setSpawnpoint(World world) {
        if (this.spawnpoint == null) {
            this.spawnpoint = world.getLocation(this);
            // System.out.println("Spawnpoint of "+this+" is "+this.spawnpoint);

        }
    }

    public void rabbitCaught(World world) { 
        if (this.caught) {
            world.delete(this);
            world.setTile(spawnpoint, new TrapActivated(supply));
        }
    }

    public void trapped(){
        System.out.println("trapped");
        this.supply=this.supply+1;
        this.caught=true;
    }

    public boolean used(){
        return this.caught;
    }

}
