package animal;

import java.awt.Color;

import abstracts.Predator;
import executable.DisplayInformation;

public class Monkey extends Predator {

    public Monkey() {
        super(0, 100, 300, 18, 1, 10, 0, 2,
                0.80d);
        growthStates = new String[][] { { "monkey-small", "monkey-small-sleeping" }, { "monkey", "monkey-sleeping" } };
    }

    @Override
    public Monkey newInstance() {
        return new Monkey();
    }

    @Override
    public DisplayInformation getInformation() {
        int sleepPointer = (sleeping) ? 1 : 0;
        int growthPointer = (matureAge <= age) ? 1 : 0;

        return new DisplayInformation(Color.red, growthStates[growthPointer][sleepPointer]);
    }
    // pack
    // pick berries
    // destroy bushes for sticks
    // without stick can only pick berries
    // with stick can kill rabbit
}
