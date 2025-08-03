public class Pokeballs extends Item {
    @Override
    public String getName() {
        return "Poké Balls";
    }

    @Override
    public String getDescription() {
        return "Use these to catch wild Pokémon.";
    }

    @Override
    public void use(Pokemon target) {
        if (target != null) {
            System.out.println("You used a Poké Ball on " + target.getName() + "!");
        }
    }
}