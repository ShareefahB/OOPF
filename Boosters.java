public class Boosters {
   public static final String Boxing_Gloves = "Boxing Gloves";
   public static final String Boots = "Boots";

   public static int getAttackBoost(String boosterName) {
      return "Boxing Gloves".equals(boosterName) ? 10 : 0;
   }

   public static int getSpeedBoost(String boosterName) {
      return "Boots".equals(boosterName) ? 5 : 0;
   }

   public static String getDescription(String boosterName) {
      switch(boosterName.hashCode()) {
      case 64369569:
         if (boosterName.equals("Boots")) {
            return "Increases speed during battle.";
         }
         break;
      case 1849161027:
         if (boosterName.equals("Boxing Gloves")) {
            return "Increases attack power during battle.";
         }
      }

      return "Unknown booster.";
   }
}
