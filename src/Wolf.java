
import java.util.Set;

import abstracts.Animal;
import abstracts.LivingBeing;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal {

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 500, 0, 0, 0, 0, 0);
    }

    @Override
    public void act(World world) {

        if (lonely(world)) {
            System.out.println("1");
            moveCloser(world);
            System.out.println("2");
        } else {
            move(world, null);
            System.out.println("3");
        }

        // hunt animals hvis den er sulten

        // har wolfpack
        // størrelse 6?
        // hvis de møder en pakkeløs ulv adopterer de den hvis der er plads
        // former sig hver 3. dag?
        // formerer sig ikke hvis pakken er fuldt
        // har et ulvehul
        //

        /*
         * Skal søge aktivt mod andre dyr som de kan spise
         * Spiser animals for at få mad
         * Flokke
         * Har et ulvehul hvor de formerer sig
         * Hostile mod andre flokke
         * Undgår andre ulveflokke
         * Skal kunne spise en bjørn hvis conditions er right
         */

        super.act(world);
    }

    public void moveCloser(World world) {
        Set<Location> tiles = world.getSurroundingTiles(10);
        Location wolfLocation = world.getLocation(this);
        int x = wolfLocation.getX();
        int y = wolfLocation.getY();
        Location newLocation = null;
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                newLocation = toAndFrom(world, l, wolfLocation);
            }
        }
        world.move(this, newLocation);
    }

    public boolean lonely(World world) {
        Set<Location> tiles = world.getSurroundingTiles(1);
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                return false;
            }

        }
        return true;
    }

}
