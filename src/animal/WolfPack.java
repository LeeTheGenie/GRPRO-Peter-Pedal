package animal;

import java.util.List;

import misc.WolfHole;

import java.util.ArrayList;

public class WolfPack {

    private List<Wolf> wolfpack;
    private String name;
    private WolfHole wolfHole;

    public WolfPack(String name) {
        this.wolfpack = new ArrayList<>();
        this.name = name;
    }

    public void addWolf(Wolf wolf) {
        wolfpack.add(wolf);
    }

    public WolfHole getWolfHole() {
        return this.wolfHole;
    }

    public void setWolfHole(WolfHole wolfHole){
        this.wolfHole = wolfHole;
    }

    public String getName() {
        return name;
    }

    public List<Wolf> getWolfPack() {
        return wolfpack;
    }

    public void display() {
        for (Wolf w : wolfpack) {
            System.out.println("- " + w);
        }
    }
}