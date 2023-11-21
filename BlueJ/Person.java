import itumulator.world.*;
//import itumulator.executable.*;
import itumulator.simulator.*;
import java.util.*;
//import java.util.ArrayList;
import java.util.Random;
public class Person implements Actor {
    
 @Override public void act(World world) {
    try {
        System.out.println("I ain't doin’ nothin’!");
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        
        int randomTarget = (int) Math.floor(Math.random()*neighbours.size());
        
        Location l = list.get(randomTarget); // Linje 2 og 3 kan erstattes af neighbours.toArray()[0]
        world.move(this,l);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
     
 }

}