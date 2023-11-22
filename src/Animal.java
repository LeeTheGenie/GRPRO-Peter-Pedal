import itumulator.world.World;

public class Animal extends LivingBeing {

    protected int currentEnergy;
    protected int maxEnergy;
    protected int trueMaxEnergy;

    Animal(int age, int maxAge,int maxEnergy) {
        super(age,maxAge);
        this.currentEnergy = maxEnergy;
        this.maxEnergy = maxEnergy;
        this.trueMaxEnergy = maxEnergy;
    }

    @Override public void act(World world) {
        if(currentEnergy==0)
            die(world);
        super.act(world);
    }

    public void checkEnergy() {

    };

}