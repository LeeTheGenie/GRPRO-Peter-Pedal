import abstracts.Plant;
import itumulator.world.Location;
import itumulator.world.World;

public class Bush extends Plant {

    protected int spawnBerry;
    protected Location spawnpoint;

    public Bush() {
        super(0, 100, 200);
        this.spawnBerry=0;
        this.spawnpoint=null;
    }

    @Override
    public void act(World world){
        setSpawnpoint(world);
        newBerries(world);

    }


    public void setSpawnpoint(World world){
        if(this.spawnpoint==null){
            this.spawnpoint=world.getLocation(this);
            System.out.println("Spawnpoint of "+this+" is "+this.spawnpoint);
            
        }
    }

    public void newBerries(World world){
        if (this.spawnBerry==10) {
            world.delete(this);
            world.setTile(spawnpoint, new BerryBush());
        }
        else{
            this.spawnBerry=this.spawnBerry+1;

        }

    }
}
