package animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import abstracts.LivingBeing;

import itumulator.world.World;
import misc.WolfHole;
import itumulator.world.Location;

public class WolfPack extends LivingBeing {

    private List<Wolf> wolfpack;
    private Wolf wolf1;
    private Wolf wolf2;
    private int maxPackSize;
    private WolfHole wolfHole;

    public WolfPack() {
        super(0, 0);
        this.wolfpack = new ArrayList<>();
        this.maxPackSize = 6;
    }

    // Skal acte i baggrunden??? how???
    @Override
    public void act(World world) {
        // check if two wolves are standing next to each other
        if (twoWolvesNextToEachOther(world)) {
            // if two wolves are next to each other, check if they are in a wolfpack

            // if both dont have a pack, create a new pack
            if (!wolf1.hasPack() && !wolf2.hasPack()) {
                // create a new pack with wolf1 and wolf2 in it
                createPack();

                // if one of them has a pack, add the other to it
            } else if (wolf1.hasPack() && !wolf2.hasPack()) {
                // if there is room in the pack
                if (wolf1.getPack().getPackSize() < maxPackSize) {
                    // get wolf1 pack and add wolf2 to it
                    addWolftoAnotherPack(wolf1, wolf2);
                }

                // if one of them has a pack, add the other to it
            } else if (!wolf1.hasPack() && wolf2.hasPack()) {
                // if there is room in the pack
                if (wolf2.getPack().getPackSize() < maxPackSize) {
                    // get wolf2 pack and add wolf1 to it
                    addWolftoAnotherPack(wolf2, wolf1);
                }

            }
        }
        super.act(world);
    }

    /**
     * Checks if two wolves are standing next to each other
     * 
     * @param world
     * @return true if two wolves are standing next to each other
     */
    public boolean twoWolvesNextToEachOther(World world) {
        Map<Object, Location> tiles = world.getEntities();
        for (Object o : tiles.keySet()) {
            if (o instanceof Wolf) {
                if (world.isOnTile(o)) {
                    Wolf wolf = (Wolf) o;
                    if (wolf.wolfNearby(world, 1)) {
                        this.wolf1 = wolf;
                        this.wolf2 = (Wolf) wolf.locateTarget(world, 1);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds a wolf to the wolfpack
     * 
     * @param wolf to be added
     */
    public void addWolf(Wolf wolf) {
        wolfpack.add(wolf);
    }

    /**
     * Creates a new wolfpack with wolf1 and wolf2 in it
     */
    public void createPack() {
        WolfPack pack = new WolfPack();
        pack.addWolf(wolf1);
        pack.addWolf(wolf2);
        wolf1.setPack(pack);
        wolf2.setPack(pack);
        System.out.println("Pack created");
        display();
    }

    /**
     * Adds a wolf to another pack
     * 
     * @param recieverPack Pack to add wolf to
     * @param wolf         to be added
     */
    public void addWolftoAnotherPack(Wolf recieverPack, Wolf wolf) {
        WolfPack pack = recieverPack.getPack();
        pack.addWolf(wolf);
        wolf.setPack(pack);
    }

    /**
     * Returns the size of the wolfpack
     * 
     * @return size of wolfpack
     */
    public int getPackSize() {
        return wolfpack.size();
    }

    /**
     * Returns the packs wolfhole
     * 
     * @return wolfhole
     */
    public WolfHole getWolfHole() {
        return wolfHole;
    }

    /**
     * Sets the packs wolfhole
     * 
     * @param wolfHole to be set
     */
    public void setWolfHole(WolfHole wolfHole) {
        this.wolfHole = wolfHole;
    }

    /**
     * Displays the wolfpack
     */
    public void display() {
        for (Wolf w : wolfpack) {
            System.out.println("WolfPack:");
            System.out.println("- " + w);
        }
    }
}
