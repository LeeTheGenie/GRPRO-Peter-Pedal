import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.Random;

public class RabbitHole extends LivingBeing implements NonBlocking {
    private ArrayList<Rabbit> rabbits;
    private ArrayList<RabbitHole> exits;
    
    public RabbitHole(){
        super(0,0);
        this.rabbits = new ArrayList<Rabbit>(); //Number of rabbits in RabbitHole/tunnel

        this.exits = new ArrayList<RabbitHole>();
        this.exits.add(this);
    }

    @Override public RabbitHole newInstance() {
        return new RabbitHole();
    }

    @Override public void act(World world) {
        //System.out.println("bøh");
    }

    public void rabbitStay(World world){
        Random rand = new Random();

        if(world.isNight()==true){/*
            if(){

            }*/
        }
        else{
            if(rand.nextInt(1)==0){
                world.getTiles();
                    System.out.println("bruh");
            }

        }
    }

}
