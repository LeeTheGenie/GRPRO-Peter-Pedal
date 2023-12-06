package animal;

import java.util.List;
import java.util.ArrayList;

public class WolfPack {

    private List<Wolf> wolfpack;
    private String name;

    public WolfPack(String name) {
        this.wolfpack = new ArrayList<>();
        this.name = name;
    }

    public void addWolf(Wolf wolf) {
        wolfpack.add(wolf);
    }

    public String getName() {
        return name;
    }

    public List<Wolf> getWolfPack() {
        return wolfpack;
    }

    public int getWolfPackSize() {
        return wolfpack.size();
    }

    public void display() {
        for (Wolf w : wolfpack) {
            System.out.println("- " + w);
        }
    }
}