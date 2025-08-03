public class BoxingGloves extends Item {
    @Override
    public String getName() {
        return "Boxing Gloves";
    }

    @Override
    public String getDescription() {
        return "Boosts your Pokemon's attack for one turn.";
    }

    @Override
    public void use(Pokemon target) {
        // If target is null (used outside battle), ignore
        if (target != null) {
            int boost = 10;
            target.applyAttackBoost(boost);
            System.out.println(target.getName() + " gained +10 Attack from Boxing Gloves!");
        }
    }
}

