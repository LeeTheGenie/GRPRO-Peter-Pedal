import abstracts.Animal;

public class Wolf extends Animal {

    @Override
    public Wolf newInstance() {
        return new Wolf();
    }

    public Wolf() {
        super(0, 0, 0);
    }
}
