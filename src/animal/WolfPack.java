package animal;
import misc.WolfHole;
import java.util.HashSet;

import abstracts.LivingBeing;

public class WolfPack {

    private HashSet<Wolf> wolfList;
    private WolfHole wolfHole;
    private int heat, kills;
    LivingBeing target;
    Boolean hunting;

    public WolfPack() {
        this.wolfList = new HashSet<Wolf>();
        this.heat=60;
        this.hunting = false;
        this.target = null; 
        this.kills = 0; 
    }

    public void addWolf(Wolf wolf) {
        wolfList.add(wolf);
    }
    public void removeWolf(Wolf wolf) {
        wolfList.add(wolf);
    }

    public WolfHole getWolfHole() {
        return this.wolfHole;
    }

    public void setWolfHole(WolfHole wolfHole){
        this.wolfHole = wolfHole;
    }

    public HashSet<Wolf> getWolfList() {
        return wolfList;
    }

    public int getSize() {
        return wolfList.size();
    }

    public boolean hasSpace() {
        return (getSize()<6);
    }

    public boolean inHeat(){
        if (this.heat>=60) {
            return true;
        }
        return false;
    }

    public void getHorny(){
        this.heat=this.heat+1;
    }

    public void postNutClarity(){
        this.heat=0;
    }

    public boolean getHunting() {
        return this.hunting;
    }

    public void startHunting() {
        System.out.println(this+" WE ARE HUNTING!");
        this.hunting = true; 
    }

    public void endHunting() {
        System.out.println(this+" we stopped hunting!");
        this.hunting = false; 
    }

    public boolean hasTarget() {
        return target!=null;
    }

    public LivingBeing getTarget(){
        return this.target;
    }

    public void incrementKills() {
        setKills(kills+1);
        if(kills>=(getSize()/2)) {
            setKills(0);
            endHunting();
            target = null; 
        }
    }

    public void setTarget(LivingBeing target) {
        this.target = target;
    }

    public void setKills(int i) {
        this.kills = i;
    }

    public int getKills() {
        return this.kills;
    }

    public void display() {
        for (Wolf w : wolfList) {
            System.out.println("- " + w);
        }
    }
}