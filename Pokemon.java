public class Pokemon {
   private String name;
   private int hp;
   private int attackPower;
   private String moveType;
   private String defenderType;
   private int speed;
   private int increaseAttack = 0;
   private int increaseSpeed = 0;
   private int baseSpeed;
   private int baseAttackPower;

   public Pokemon(String name, int hp, int attackPower, int speed, String moveType, String defenderType) {
      this.name = name;
      this.hp = hp;
      this.attackPower = attackPower;
      this.moveType = moveType;
      this.defenderType = defenderType;
      this.speed = speed;
      this.baseSpeed = speed;
      this.baseAttackPower = attackPower;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getHp() {
      return this.hp;
   }

   public void setHp(int hp) {
      this.hp = hp;
   }

   public int getSpeed() {
      return this.speed;
   }

   public void setSpeed(int speed) {
      this.speed = speed;
   }

   public int getAttackPower() {
      return this.attackPower;
   }

   public void setAttackPower(int attackPower) {
      this.attackPower = attackPower;
   }

   public String getMoveType() {
      return this.moveType;
   }

   public void setMoveType(String moveType) {
      this.moveType = moveType;
   }

   public String getDefenderType() {
      return this.defenderType;
   }

   public void setDefenderType(String defenderType) {
      this.defenderType = defenderType;
   }

   public int getBaseSpeed() {
      return this.baseSpeed;
   }

   public int getBaseAttackPower() {
      return this.baseAttackPower;
   }

   public void setBaseAttackPower(int baseAttackPower) {
      this.baseAttackPower = baseAttackPower;
   }

   public void reduceHp(int damage) {
      this.hp = Math.max(0, this.hp - damage);
   }

   public boolean isDefeated() {
      return this.hp <= 0;
   }

   public int attack(Pokemon target) {
      double effectiveness = TypeEffectiveness.getEffectiveness(this.moveType, target.getDefenderType());
      int damage = (int)((double)this.attackPower * effectiveness);
      String var10001 = this.name;
      System.out.println(var10001 + " attacks " + target.getName() + " for " + damage + " damage!");
      return damage;
   }

   public void applyAttackBoost(int boostAmount) {
      this.increaseAttack += boostAmount;
      this.attackPower = this.baseAttackPower + this.increaseAttack;
   }

   public void applySpeedBoost(int boostAmount) {
      this.increaseSpeed += boostAmount;
      this.speed = this.baseSpeed + this.increaseSpeed;
   }

   public void removeSpeedBoost(int boostAmount) {
      this.increaseSpeed -= boostAmount;
      if (this.increaseSpeed < 0) {
         this.increaseSpeed = 0;
      }

      this.speed = this.baseSpeed + this.increaseSpeed;
   }

   public void removeAttackBoost(int boostAmount) {
      this.increaseAttack -= boostAmount;
      if (this.increaseAttack < 0) {
         this.increaseAttack = 0;
      }

      this.attackPower = this.baseAttackPower + this.increaseAttack;
   }

   public void resetBoosts() {
      this.increaseAttack = 0;
      this.attackPower = this.baseAttackPower;
      this.increaseSpeed = 0;
      this.speed = this.baseSpeed;
   }

   public String toString() {
      return "Name: " + this.name + "\nHP: " + this.hp + "\nAttack Power: " + this.attackPower + "\nMove Type: " + this.moveType + "\nDefender Type: " + this.defenderType + "\nBase Speed: " + this.baseSpeed + "\nBase Attack Power: " + this.baseAttackPower;
   }
}
