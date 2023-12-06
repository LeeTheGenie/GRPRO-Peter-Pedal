package misc;

import java.awt.Color;

import abstracts.LivingBeing;
import executable.DisplayInformation;
import executable.DynamicDisplayInformationProvider;
import itumulator.world.World;

public class Carcass extends LivingBeing implements DynamicDisplayInformationProvider {

    protected int decay;
    protected boolean infected;
    
    public Carcass(int age, int maxAge, int decay) {        
        super(age, maxAge);
        this.decay=decay;
        this.infected=false;
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
            world.delete(this);
        }
        else{
            if (infected) {
                this.decay = this.decay-2;
            }else
                this.decay = this.decay-1;
        }
            
    }

    public void takeBite(World world){
        this.decay=this.decay-20;
        
    }

    public void fungusInfected(World world){
        this.infected=true;
    }


    @Override
    public DisplayInformation getInformation() {
        if(decay<50){
            return new DisplayInformation(Color.red,"carcass-small");
        }
        else
            return new DisplayInformation(Color.red,"carcass");
    }

    public void giveFungus() {
    }


}
