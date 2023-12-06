package misc;

import java.awt.Color;
import java.util.Set;

import abstracts.Animal;
import abstracts.LivingBeing;
import executable.DisplayInformation;
import executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import plants.Grass;

public class Fungus extends LivingBeing implements DynamicDisplayInformationProvider{

    int energy;
    String[] growthState;

    public Fungus() {
        super(0,0);
        this.energy = 100;
        growthState = new String[]{"fungi-small","fungi"};
    }

    @Override public DisplayInformation getInformation() {    
        int growthPointer = (energy>=100)?1:0;
        return new DisplayInformation(Color.red,growthState[growthPointer]);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override public LivingBeing newInstance() {
        return new Fungus();
    }
    

    @Override public void act(World world){
        spreadSpores(world);
        spawnGrass(world);
        changeEnergy(-2,world);
    }

    public boolean canAfford(int cost) {
        if (energy - cost != 0)
            return true;
        return false;
    }

    public void changeEnergy(int change, World world) {
        energy += change;

        if (energy <= 0) { // grace period when below mature age
            die(world, "energyloss");
        }
    }

    public void spawnGrass(World world) {
        if (!world.isOnTile(this))
            return;

        if(world.containsNonBlocking(world.getLocation(this)))
            return;

        Grass grass = new Grass();
        world.setTile(world.getLocation(this),grass);
    }

    public void spreadSpores(World world){
        if (!world.isOnTile(this))
            return;

        Location currentLocation = world.getLocation(this);

        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation, 3);
        // Find animal wit

        for (Location l : surroundingTiles) {
            Object target = world.getTile(l);
            if (target instanceof Carcass)
                ((Carcass) target).fungusInfected();
        }
        
    }
}
