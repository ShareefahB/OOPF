public class Boots extends Item {
    @Override
    public String getName() {
        return "Boots";
    }

    @Override
    public String getDescription() {
        return "Boosts your Pokémon's speed for one turn.";
    }

    @Override
    public void use(Pokemon target) {
        if (target != null) {
            int boost = 10;
            target.applySpeedBoost(boost);
            System.out.println(target.getName() + " gained +10 Speed from Boots!");
        }
    }
}
