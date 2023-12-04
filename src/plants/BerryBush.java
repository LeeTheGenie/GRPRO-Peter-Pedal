package plants;
import abstracts.Plant;
import itumulator.world.Location;
import itumulator.world.World;

public class BerryBush extends Plant {
   protected Boolean berries;
   protected Location spawnpoint;

    public BerryBush() {
        super(0, 100, 200);
        this.berries=true;
        this.spawnpoint=null;
    }

    @Override public BerryBush newInstance() {
        return new BerryBush();
    }

    @Override public void act(World world){
        setSpawnpoint(world);
        if (world.isNight()) {
            this.berries=false;
        }
        noBerries(world);

    }

    public void setSpawnpoint(World world){
        if(this.spawnpoint==null){
            this.spawnpoint=world.getLocation(this);
            System.out.println("Spawnpoint of "+this+" is "+this.spawnpoint);
            
        }
    }
    
    public void noBerries(World world){ //migrate noBerries to animal class and rename to pickBerries
        if(this.berries==false){
            world.delete(this);
            world.setTile(spawnpoint, new Bush());
        }
    }
}
