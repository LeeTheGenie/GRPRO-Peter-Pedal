package animal;
import misc.WolfHole;
import java.util.HashSet;

public class WolfPack {

    private HashSet<Wolf> wolfList;
    private WolfHole wolfHole;
    private int heat;

    public WolfPack() {
        this.wolfList = new HashSet<Wolf>();
        this.heat=60;
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

    public void display() {
        for (Wolf w : wolfList) {
            System.out.println("- " + w);
        }
    }
}