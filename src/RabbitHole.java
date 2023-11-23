import itumulator.world.NonBlocking;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class RabbitHole extends LivingBeing implements NonBlocking {
    
    ArrayList<Object> rabbits;

    @Override public void act(World world) {
        keepRabbit(world);
        exitRabbit(world);
    }

    RabbitHole(){
    super(0,0);
    this.rabbits = new ArrayList<>();
    }

    public void keepRabbit(World world){
        if(world.isNight()==true){
            if(world.getTile(world.getLocation(this)) instanceof Rabbit){
                Object rabbit = world.getTile(world.getLocation(this));
                this.rabbits.add(rabbit);
                world.remove(rabbit);

                System.out.println("Rabbits resting: "+this.rabbits);
                System.out.println("In this rabbithole: "+world.getTile(world.getLocation(this)));
            }
        }
    }

    public void exitRabbit(World world){
        try{
        if(world.isDay()==true){
            for(Object rabbit:rabbits){
                
                
                
                System.out.println(rabbit+" left "+this);
                this.rabbits.remove(rabbit);
            }
        }
        }catch(Exception e){

        }
    }
    

}
