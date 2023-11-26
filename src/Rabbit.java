import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class Rabbit extends Animal {

    private Location rabbithole;
    private boolean dig = false;

    @Override
    public Rabbit newInstance() {
        return new Rabbit();
    }

    public Rabbit() {
        super(0, 70, 30);
        this.rabbithole = null;

    }

    @Override
    public void act(World world) {
        maxEnergy = trueMaxEnergy - (age / 7); // update the max energy
        findHole(world);
        digHole(world);
        gotoHole(world);

        if (world.isDay()) {
            eat(world);
            reproduce(world);
            move(world);
        }

        super.act(world); // age up & check for if energy == 0

    }

    public void eat(World world) { // spiser på den tile den står på
        int energyIncrement = 8; // hvor meget energi man får
        if (currentEnergy == maxEnergy) // hvis du ikke gavner af at spise så lad vær
            return;
        try {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Plant) { // er det en plant
                world.delete(world.getNonBlocking(world.getLocation(this))); // slet den plant

                currentEnergy += energyIncrement;
                if (currentEnergy > maxEnergy) // hvis den er større end max, bare set den til max fordi det er max duh
                    currentEnergy = maxEnergy;
            }
        } catch (IllegalArgumentException e) { // if the current tile does not have a nonblocking it returns
                                               // IllegalArgumentException
            // System.out.println(e.getMessage());
        }
    }

    // Move to a random free location within radius of 1, costs 1 energy
    public void move(World world) {
        if (currentEnergy > 0) {
            try {
                Set<Location> neighbors = world.getEmptySurroundingTiles();
                List<Location> list = new ArrayList<>(neighbors);
                Random r = new Random();

                int randomLocation = r.nextInt(list.size());
                Location newLocation = list.get(randomLocation);

                world.move(this, newLocation);
            } catch (Exception e) {

            }
        }
        currentEnergy--;
    }

    // makes another rabbit if there is another rabbit within radius of 1 and energy
    // is greater than 7 and age greater than 8 costs 2 energy
    public void reproduce(World world) {

        if (currentEnergy > 20 && age > 18) {
            try {
                Set<Location> tiles = world.getSurroundingTiles(world.getLocation(this));
                for (Location l : tiles) {
                    if (world.getTile(l) instanceof Rabbit) {

                        // Get surrounding tiles
                        Set<Location> neighbors = world.getEmptySurroundingTiles();
                        List<Location> list = new ArrayList<>(neighbors);

                        // take one random surrounding tile
                        Random r = new Random();
                        int randomLocation = r.nextInt(list.size());
                        Location newLocation = list.get(randomLocation);

                        // create a new instance of Rabbit and put it on the world
                        world.setTile(newLocation, new Rabbit());
                        System.out.println("Baby");
                    }
                }
                currentEnergy -= 2;
            } catch (Exception e) {

            }
        }
    }

    public void digHole(World world) {
        try {
            Location rabbitLocation = world.getLocation(this);
            if (world.isNight() && rabbithole == null) {
                if (world.isNight() == true && rabbitLocation != null && this.dig == false) {
                    this.dig = true;
                    eat(world);
                    world.setTile(rabbitLocation, new RabbitHole());
                    rabbithole = rabbitLocation;
                }
            } else
                this.dig = false;
        } catch (Exception e) {
        }
    }

    public void findHole(World world) {
        try {
            if (rabbithole == null) {
                Set<Location> tiles = world.getSurroundingTiles(1);
                for (Location l : tiles) {
                    try {
                        if (world.getNonBlocking(l) instanceof RabbitHole) {
                            rabbithole = l;
                            this.dig = false;
                            break;
                        } else {
                            this.dig = true;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void gotoHole(World world) {
        if (rabbithole != null) {
            try {
                world.move(this, rabbithole);
            } catch (Exception e) {

            }
        }
    }

    // goes to assigned hole if rabbit doesnt have a assigned hole it checks if
    // there is one nearby if there is one it enters it if not it digs a new hole

    /*
     * public void holeMode(World world) {
     * 
     * if (rabbithole != null) { // hvis rabbit er assignet et rabbithole går den
     * hen til det.
     * try {
     * world.move(this, rabbithole);
     * world.remove(this);
     * } catch (Exception e) {
     * 
     * }
     * 
     * }
     * int radius = 2;
     * boolean holenearby = checkHolesNearby(world, radius);
     * 
     * if (rabbithole == null && holenearby == false) { // hvis rabbit ikke har hul
     * assignet og der ikke er et tæt på
     * // så lav nyt hul
     * System.out.println("laver hul fordi ingen tæt på");
     * try {
     * Location rabbitlocation = world.getLocation(this); // gemmer location af
     * rabbit
     * System.out.println("fået loaktion");
     * try {
     * if (world.getNonBlocking(rabbitlocation) instanceof NonBlocking) {
     * world.delete(world.getNonBlocking(rabbitlocation)); // deletes nonblocking
     * entity if
     * // there is one
     * }
     * } catch (Exception e) {
     * 
     * }
     * System.out.println("slettet noget");
     * world.setTile(rabbitlocation, new RabbitHole()); // laver rabbithul på rabbit
     * location
     * System.out.println("lavet hul");
     * world.remove(this); // fjerner rabbit
     * System.out.println("fjernet rabbit");
     * 
     * rabbithole = rabbitlocation; // assigner rabbit et rabbit hole
     * System.out.println("assignet hul");
     * 
     * } catch (Exception e) {
     * 
     * }
     * }
     * if (rabbithole == null && holenearby == true) // hvis rabbit ikke har hul
     * assignet men der er et hul tæt på så
     * // gå ind i det
     * try {
     * Set<Location> tiles = world.getSurroundingTiles(radius); // getter
     * surrounding
     * // tiles
     * for (Location l : tiles) {
     * try {
     * if (world.getNonBlocking(l) instanceof RabbitHole) { // tjekker om tile er
     * rabbithole
     * 
     * world.move(this, l); // hvis ja rykker derhen
     * world.remove(this); // fjerner rabbit
     * 
     * rabbithole = l; // assigner rabbit rabbitjole
     * 
     * break;
     * }
     * } catch (Exception e) {
     * // TODO: handle exception
     * }
     * }
     * } catch (Exception e) {
     * // TODO: handle exception
     * }
     * }
     * 
     * // virker ikke
     * // tjekker om der er et rabbithole tæt på inden for en given radius
     * private boolean checkHolesNearby(World world, int radius) {
     * try {
     * Set<Location> tiles = world.getSurroundingTiles(radius); // getter
     * surrounding
     * // tiles
     * for (Location l : tiles) {
     * try {
     * if (world.getNonBlocking(l) instanceof RabbitHole) { // tjekker om tile er
     * rabbithole
     * System.out.println("hole(s) nearby");
     * return true;
     * }
     * } catch (Exception e) {
     * // TODO: handle exception
     * }
     * }
     * } catch (Exception e) {
     * // TODO: handle exception
     * }
     * System.out.println("hole(s) not nearby");
     * return false;
     * }
     * 
     * private boolean onMap(World world) {
     * HashMap<Object, Location> thingsOnMap = new HashMap<>(world.getEntities());
     * for (Object o : thingsOnMap.keySet()) {
     * if (o instanceof Rabbit) {
     * if (thingsOnMap.get(o) == null) {
     * return false;
     * }
     * }
     * }
     * return true;
     * }
     */

}
