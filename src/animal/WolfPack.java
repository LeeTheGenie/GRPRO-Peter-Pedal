package animal;
import misc.WolfHole;
import java.util.ArrayList;

public class WolfPack {

    private ArrayList<Wolf> wolfList;
    private WolfHole wolfHole;

    public WolfPack() {
        this.wolfList = new ArrayList<>();
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

    public ArrayList<Wolf> getWolfList() {
        return wolfList;
    }

    public int getSize() {
        return wolfList.size();
    }

    public boolean hasSpace() {
        return (getSize()<6);
    }

    public void display() {
        for (Wolf w : wolfList) {
            System.out.println("- " + w);
        }
    }
}