
import itumulator.world.NonBlocking;
import itumulator.world.World;

import abstracts.LivingBeing;

public class RabbitHole extends LivingBeing implements NonBlocking {

    Rabbit owner;

    RabbitHole(Rabbit owner) {
        super(0, 10);
        this.owner = owner;
    }

    public boolean isClaimed() {
        if(this.owner==null)
            return false; 
        return true;
    }

    public void setOwner(Rabbit owner) {
       // System.out.println();
        if(!isClaimed()) {
            this.owner = owner;
            this.age = 0;
        } else {
            throw new Error("Cannot asign a new owner to a hole with an already existing owner."); 
        }
    }

    public void clearOwner(){
        this.owner = null;
    }

    public Rabbit getOwner() {
        return owner;
    }

    @Override public RabbitHole newInstance() {
        return new RabbitHole(null);
    }

    @Override
    public void act(World world) {
        if(!isClaimed())
            super.act(world);

        //keepRabbit(world);
        //exitRabbit(world);
    }

   
    /*
    

    public void exitRabbit(World world) {

        if (world.isDay() == true) {
            try {
                if (this.rabbits.size() > 0) {
                    for (Object rabbit : rabbits) {

                        Set<Location> neighbors = world.getEmptySurroundingTiles();
                        List<Location> list = new ArrayList<>(neighbors);
                        Random r = new Random();
                        int randomLocation = r.nextInt(list.size());

                        System.out.println(rabbit + " left " + this + "\n -Located at: " + world.getCurrentLocation());
                        this.rabbits.remove(rabbit);

                        Location newLocation = list.get(randomLocation);

                        world.setCurrentLocation(newLocation);
                        System.out.println(" -Go to tile:" + world.getCurrentLocation());
                        world.setTile(newLocation, rabbit);

                    }

                }
            } catch (java.util.ConcurrentModificationException e) {

            }
        }
    }
    */
}
