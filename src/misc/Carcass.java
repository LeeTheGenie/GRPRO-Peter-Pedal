package misc;

import java.awt.Color;

import abstracts.LivingBeing;
import executable.DisplayInformation;
import executable.DynamicDisplayInformationProvider;
import itumulator.world.World;

public class Carcass extends LivingBeing implements DynamicDisplayInformationProvider {

    protected int decay;
    
    public Carcass(int age, int maxAge, int decay) {
        super(age, maxAge);
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
            this.decay = this.decay-1;
            System.out.println("decaying...");
        }
            

    }

    public void takeBite(){
        
    }

    @Override
    public DisplayInformation getInformation() {
        if(decay>50){
            return new DisplayInformation(Color.red,"carcass-small");

        }
        else
            return new DisplayInformation(Color.red,"carcass");
    }
}
