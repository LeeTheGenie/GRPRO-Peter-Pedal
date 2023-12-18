package misc;

import java.util.HashSet;
import java.util.Set;

import animal.Monkey;

public class MonkeyFamily {

    private HashSet<Monkey> family;
    private int heat;

    public MonkeyFamily() {
        this.family = new HashSet<>();
        this.heat = 60;
    }

    public void addMonkey(Monkey monkey) {
        family.add(monkey);
    }

    public void removeMonkey(Monkey monkey) {
        family.add(monkey);
    }

    public Set<Monkey> getFamily() {
        return family;
    }

    public int getSize() {
        return family.size();
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
        for (Monkey m : family) {
            System.out.println("- " + m);
        }
    }
}