package animal;

import java.util.HashSet;

public class MonkeyPack {

    private HashSet<Monkey> monkeyList;
    private HashSet<Trap> trapList;
    private int heat;

    public MonkeyPack() {
        this.monkeyList = new HashSet<>();
        this.trapList = new HashSet<>();
        this.heat = 60;
    }

    public void addMonkey(Monkey monkey) {
        monkeyList.add(monkey);
    }

    public void removeMonkey(Monkey monkey) {
        monkeyList.add(monkey);
    }

    public Set<Trap> getTraps() {
        return this.trapList;
    }

    public void addTrap(Trap trap) {
        trapList.add(trap);
    }

    public void removeTrap(Trap trap) {
        trapList.remove(trap);
    }

    public boolean hasTrap() {
        return (trapList.size() > 0);
    }

    public HashSet<Monkey> getMonkeyList() {
        return monkeyList;
    }

    public int getSize() {
        return monkeyList.size();
    }

    public boolean hasSpace() {
        return (getSize() < 6);
    }

    public boolean inHeat() {
        if (this.heat >= 60) {
            return true;
        }
        return false;
    }

    public void getHorny() {
        this.heat = this.heat + 1;
    }

    public void postNutClarity() {
        this.heat = 0;
    }

    public void display() {
        for (Monkey m : monkeyList) {
            System.out.println("- " + m);
        }
    }
}