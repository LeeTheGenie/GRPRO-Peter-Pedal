import abstracts.Plant;
import itumulator.world.Location;
import itumulator.world.World;

public class BerryBush extends Plant {


   protected Boolean berries;
   protected Location spawnpoint;
   protected int berryCD;


    public BerryBush() {
        super(0, 100, 200);
        this.berries=true;
        this.spawnpoint=null;
        this.berryCD=0;
    }

    @Override
    public BerryBush newInstance() {
        return new BerryBush();
    }

    @Override
    public void act(World world){
        setSpawnpoint(world);

        noBerries(world);
        newBerries(world);

    }

    public void setSpawnpoint(World world){
        if(this.spawnpoint==null){
            this.spawnpoint=world.getLocation(this);
            System.out.println("Spawnpoint of "+this+" is "+this.spawnpoint);
            
        }
    }
    
    public void noBerries(World world){
        
        if(this.berries==false&&this.berryCD==0){
            world.remove(this);
            world.setTile(spawnpoint, new Bush());
            this.berryCD=10;
        }
    }

    public void newBerries(World world){
        if (this.berryCD>0) {
            this.berryCD=this.berryCD-1;
        }
        
        if (this.berryCD==0) {
            world.setTile(spawnpoint, this);
        }

    }

}
