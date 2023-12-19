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

    /**
     * Adds monkey to family.
     * 
     * @param monkey to add.
     */
    public void addMonkey(Monkey monkey) {
        family.add(monkey);
    }

    /**
     * Removes monkey from family.
     * 
     * @param monkey to remove.
     */
    public void removeMonkey(Monkey monkey) {
        family.add(monkey);
    }

    /**
     * Returns the family.
     * 
     * @return set of monkeys in the family.
     */
    public Set<Monkey> getFamily() {
        return family;
    }

    /**
     * Returns the size of the family.
     * 
     * @return size of family.
     */
    public int getSize() {
        return family.size();
    }

    /**
     * Checks if there is space in the family.
     * 
     * @return true if there is space in the family.
     */
    public boolean hasSpace() {
        return (getSize() < 6);
    }

    /**
     * idk what this does.
     * 
     * @return
     */
    public boolean inHeat() {
        if (this.heat >= 60) {
            return true;
        }
        return false;
    }

    /**
     * idk what this does.
     */
    public void getHorny() {
        this.heat = this.heat + 1;
    }

    /**
     * idk what this does.
     * 
     */
    public void postNutClarity() {
        this.heat = 0;
    }

    /**
     * Display all monkeys in a family.
     */
    public void display() {
        for (Monkey m : family) {
            System.out.println("- " + m);
        }
    }
}