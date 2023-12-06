package misc;

import java.awt.Color;

import abstracts.LivingBeing;
import executable.DisplayInformation;
import executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

public class Carcass extends LivingBeing implements DynamicDisplayInformationProvider {

    protected int decay;
    protected boolean infected;
    protected int fungusGrowth;
    
    public Carcass(int age, int maxAge, int decay) {        
        super(age, maxAge);
        this.decay=decay;
        this.infected=false;
        this.fungusGrowth=0;
    }

    @Override
    public void act(World world) {
        decay(world);
    }

    @Override
    public Carcass newInstance() {
        return new Carcass(age,maxAge, decay);
    }

    public void decay(World world){
        
        if(this.decay<=0){
            if (this.fungusGrowth>=2) {
                spawnFungus(world);
            }
            else
                world.delete(this);
        }
        else{
            if (infected) {
                this.decay = this.decay-2;
                this.fungusGrowth=this.fungusGrowth+1;
            }else
                this.decay = this.decay-1;
        }
    }

    public void takeBite(World world){
        this.decay=this.decay-20;
    }

    public void giveFungus(World world){
        System.out.println("infected");
        this.infected=true;
    }

    public void spawnFungus(World world){
        Location spawnLocation = world.getLocation(this);
        world.delete(this);
        world.setTile(spawnLocation, new Fungus());
        

    }


    @Override
    public DisplayInformation getInformation() {
        if(decay<50){
            return new DisplayInformation(Color.red,"carcass-small");
        }
        else
            return new DisplayInformation(Color.red,"carcass");
    }

}
