public class TypeEffectiveness {

    private TypeEffectiveness() {}

   //Effectiveness multiplier (e.g., 2.0, 1.0, 0.5)
    
    //fire beats grass
    // water beats fire
    public static double getEffectiveness(String moveType, String defenderType) {
        switch (moveType) {
            case "Fire":
                if (defenderType.equals("Grass")) return 2.0;
                if (defenderType.equals("Water")) return 0.5;
                break;
            case "Water":
                if (defenderType.equals("Fire")) return 2.0;
                if (defenderType.equals("Grass")) return 0.5;
                break;
            case "Grass":
                if (defenderType.equals("Water")) return 2.0;
                if (defenderType.equals("Fire")) return 0.5;
                break;
        }
        return 1.0; // Neutral effectiveness
    }
}
