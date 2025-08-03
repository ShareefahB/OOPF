public class Pokeballs extends Item {

    @Override
    public String getName() {
        return "Pokeballs";
    }

    @Override
    public String getDescription() {
        return "Used to catch wild Pokémon.";
    }

    public void usePokeball(int type) {
        int catchRate = 0;

        switch(type) {
            case 1:     // Poké Ball
                System.out.println("You used a Poké Ball!");
                catchRate = 20;
                break;

            case 2:     // Great Ball
                System.out.println("You used a Great Ball!");
                catchRate = 30;
                break;

            case 3:     // Ultra Ball
                System.out.println("You used a Ultra Ball!");
                catchRate = 35;
                break;

            case 4:     // Master Ball
                System.out.println("You used a Master Ball!");
                catchRate = 51; 
                break;
        }

        //return catchRate;
    }


    /*  auto generated code
    @Override
    public void use(Pokemon target) {
        if (target != null) {
            boolean caught = target.attemptCapture();
            if (caught) {
                System.out.println("You successfully caught " + target.getName() + "!");
            } else {
                System.out.println("Failed to catch " + target.getName() + ".");
            }
        }
    }*/
}
