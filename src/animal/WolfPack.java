package animal;

import java.util.List;
import java.util.ArrayList;

public class WolfPack {

    private List<Wolf> wolfpack;

    public WolfPack() {
        this.wolfpack = new ArrayList<>();
    }

    public void addWolf(Wolf wolf) {
        wolfpack.add(wolf);
    }

    public List<Wolf> getWolfPack() {
        return wolfpack;
    }
}