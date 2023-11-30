package abstracts;
import itumulator.world.World;

public abstract class Animal extends LivingBeing {

    protected int currentEnergy;
    protected int maxEnergy;
    protected int trueMaxEnergy;

    protected Animal(int age, int maxAge, int maxEnergy) {
        super(age, maxAge);
        this.currentEnergy = maxEnergy;
        this.maxEnergy = maxEnergy;
        this.trueMaxEnergy = maxEnergy;
    }

    @Override
    public void act(World world) {
        if (currentEnergy == 0) {
            System.out.println("I \"" + this.getClass() + "\" died of energyloss at age: " + age);
            die(world);
        }
        super.act(world);
    }

    @Override
    public LivingBeing newInstance() {
        return null;//new Animal(0, maxAge, trueMaxEnergy);
    }
}