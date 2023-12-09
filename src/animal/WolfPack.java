package animal;

import java.util.List;

import misc.WolfHole;

import java.util.ArrayList;

public class WolfPack {

    private List<Wolf> wolfpack;
    private WolfHole wolfHole;

    public WolfPack() {
        this.wolfpack = new ArrayList<>();
    }

    public void addWolf(Wolf wolf) {
        wolfpack.add(wolf);
    }

    public WolfHole getWolfHole() {
        return this.wolfHole;
    }

    public void setWolfHole(WolfHole wolfHole) {
        this.wolfHole = wolfHole;
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