package animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import abstracts.LivingBeing;
import abstracts.Predator;
import abstracts.Animal;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Predator {

    private LivingBeing target;
    private WolfPack pack;

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 500, 400, 20, 3, 20, 30, 3, 0.80d);
        this.target = null;
        this.pack = null;
    }

    @Override
    public void act(World world) {
        // System.out.println("wolf pack: " + pack);
        if (pack == null) {
            if (wolfNearby(world, 3)) {
                moveCloser(world);
                if (!hasPack(world, getNearbyWolfs(world, 1))) {
                    createPack(world);
                    // System.out.println("created pack");
                    pack.display();
                    // System.out.println(pack.getWolfPackSize());
                } else if (!packFull(world)) {
                    joinPack();
                }
            } else {
                move(world, null);
            }
        }
        if (pack != null) {
            if (!wolfNearby(world, 1) && wolfNearby(world, 3)) {
                moveCloser(world);
            } /*
               * else if () {
               * 
               * }
               */else {
                move(world, null);
            }
        }

        /*
         * if (target == null)
         * target = locateTarget(world, 3);
         * 
         * if (target == null) {
         * move(world, null);
         * } else if (world.isOnTile(this) && world.isOnTile(target)) {
         * move(world, toAndFrom(world, world.getLocation(target),
         * world.getLocation(this)));
         * killTarget(world);
         * }
         *
         * if (isHungry()) {
         * 
         * }
         */

        // hunt animals hvis den er sulten

        // har pack yes
        // størrelse 6? yes
        // hvis de møder en pakkeløs ulv adopterer de den hvis der er plads yes
        // former sig hver 3. dag?
        // formerer sig ikke hvis pakken er fuldt
        // har et ulvehul
        //

        /*
         * Skal søge aktivt mod andre dyr som de kan spise
         * Spiser animals for at få mad
         * Flokke yes
         * Har et ulvehul hvor de formerer sig
         * Hostile mod andre flokke
         * Undgår andre ulveflokke
         * Skal kunne spise en bjørn hvis conditions er right
         */

        super.act(world);
    }

    public void moveCloser(World world) {
        Set<Location> tiles = world.getSurroundingTiles(3);
        Location wolfLocation = world.getLocation(this);
        Location newLocation = null;
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                newLocation = toAndFrom(world, l, wolfLocation);
            }
        }
        world.move(this, newLocation);
    }

    public void killTarget(World world) {
        ((LivingBeing) world.getTile(world.getLocation(target))).die(world);
    }

    public void eatTarget(World world, Animal target) {
        // aaffa
    }

    public void createPack(World world) {
        pack = new WolfPack(null);
        List<Location> wolfsNearby = getNearbyWolfs(world, 3);
        for (Location l : wolfsNearby) {
            pack.addWolf((Wolf) world.getTile(l));
        }
    }

    public void joinPack() {
        System.out.println("joined pack");
    }

    public List<Location> getNearbyWolfs(World world, int radius) {
        Set<Location> tiles = world.getSurroundingTiles(radius);
        List<Location> wolfsNearby = new ArrayList<>();
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                wolfsNearby.add(l);
            }
        }
        return wolfsNearby;
    }

    public WolfPack getPack() {
        return pack;
    }

    public boolean hasPack(World world, List<Location> wolfs) {
        for (Location l : wolfs) {
            Wolf wolf = (Wolf) world.getTile(l);
            if (wolf.getPack() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean packFull(World world) {
        List<Location> wolfs = getNearbyWolfs(world, 1);
        for (Location l : wolfs) {
            Wolf wolf = (Wolf) world.getTile(l);
            if (wolf.getPack().getWolfPackSize() < 6) {
                return false;
            }
        }
        return true;
    }

    public boolean wolfNearby(World world, int radius) {
        Set<Location> tiles = world.getSurroundingTiles(radius);
        for (Location l : tiles) {
            if (world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }

    public boolean targetIearby(World world) {

        for (Location l : world.getSurroundingTiles(3)) {
            if (world.getTile(l) instanceof Rabbit || world.getTile(l) instanceof Wolf) {
                return true;
            }
        }
        return false;
    }
}
