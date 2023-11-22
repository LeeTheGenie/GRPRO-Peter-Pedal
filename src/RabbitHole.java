import itumulator.world.NonBlocking;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class RabbitHole implements NonBlocking, Actor {
    private ArrayList<Rabbit> rabbits;
    private ArrayList<RabbitHole> exits;
    
    public RabbitHole(){
        this.rabbits = new ArrayList<Rabbit>(); //Number of rabbits in RabbitHole/tunnel

        this.exits = new ArrayList<RabbitHole>();
        this.exits.add(this);
    }
    
    public void act(World world) {
        System.out.println("bøh");
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
