public class Pokeballs extends Item {
    @Override
    public String getName() {
        return "Poke Balls";
    }

    @Override
    public String getDescription() {
        return "Use these to catch wild Pokemon.";
    }

    @Override
    public void use(Pokemon target) {
        if (target != null) {
            System.out.println("You used a Poke Ball on " + target.getName() + "!");
        }
    }
}
