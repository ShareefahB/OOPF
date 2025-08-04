
public class Boosters {
	    public static final String Boxing_Gloves = "Boxing Gloves";
	    public static final String Boots = "Boots";

	    public static int getAttackBoost(String boosterName) {
	        if (Boxing_Gloves.equals(boosterName)) {
	            return 10; // Example: Increase attack by 10
	        }
	        return 0;
	    }

	    public static int getSpeedBoost(String boosterName) {
	        if (Boots.equals(boosterName)) {
	            return 10; // Example: Increase speed by 5 (you might need to add a speed attribute to Pokemon)
	        }
	        return 0;
	    }

	    // You might want to add a method to get a description of the booster
	    public static String getDescription(String boosterName) {
	        switch (boosterName) {
	            case Boxing_Gloves:
	                return "Increases attack power during battle.";
	            case Boots:
	                return "Increases speed during battle.";
	            default:
	                return "Unknown booster.";
	        }
	    }
	}

