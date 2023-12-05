package animal;

import java.util.Set;

import abstracts.Animal;
import abstracts.LivingBeing;
import abstracts.Predator;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Predator {

    LivingBeing target;

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.80d);
        target = null;
    }

    @Override
    public void act(World world) {

        if (target == null)
            target = locateTarget(world, 3);

        if (target == null) {
            move(world, null);
        } else if (!world.isOnTile(this) && !world.isOnTile(target)) {
            move(world, toAndFrom(world, world.getLocation(target), world.getLocation(this)));
        }

        if (lonely(world)) {
            moveCloser(world);
        } else {
            move(world, null);
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
