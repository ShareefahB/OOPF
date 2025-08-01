public class Pokemon {
	//attributes
    private String name;
    private int hp;
    private int attackPower;
    private String moveType;
    private String defenderType;
    private int speed;
    private int increaseAttack = 0; // boosted attack power
    private int increaseSpeed = 0; // boosted speed 
    private int baseSpeed; // original speed 
    private int baseAttackPower; // original attack power
    

    // Constructor
    public Pokemon(String name, int hp, int attackPower,int speed, String moveType, String defenderType) {
        this.name = name;
        this.hp = hp;
        this.attackPower = attackPower;
        this.moveType = moveType;
        this.defenderType = defenderType;
        this.speed = speed;
        this.baseSpeed = speed;
        this.baseAttackPower = attackPower;
        }

    // Setters Getters
    
    //get and set user name
    public String getName() {
    	return name;
    }
    public void setName(String name) { 
    	this.name = name; 
    }
    
    //get and set hp
    public int getHp() { 
    	return hp; 
    }
    
    public void setHp(int hp) {
    	this.hp = hp;
    }
    
    public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	//get set attack power
    public int getAttackPower() { 
    	return attackPower; 
    }
    
    public void setAttackPower(int attackPower) { 
    	this.attackPower = attackPower;
    }
    
    //get set move type
    public String getMoveType() { 
    	return moveType;
    }
    public void setMoveType(String moveType) { 
    	this.moveType = moveType; 
    }
    
    //get set defender type
    public String getDefenderType() { 
    	return defenderType; 
    }

    public void setDefenderType(String defenderType) { 
    	this.defenderType = defenderType;
    }
    
    public int getBaseSpeed() {
		return baseSpeed;
	}

	public int getBaseAttackPower() {
		return baseAttackPower;
	}

	public void setBaseAttackPower(int baseAttackPower) {
		this.baseAttackPower = baseAttackPower;
	}

	// Methods
    public void reduceHp(int damage) {
        this.hp = Math.max(0, this.hp - damage);
        //example = when HP is 10 and damage is 15, (0, 10 - 15) = 0
    }

    public boolean isDefeated() { //checks if pokemon is dead 
        return this.hp <= 0;
        //if pokemon's hp = 0, return true. otherwise, bro's still alive
    }

    public void attack(Pokemon target) {
        double effectiveness = TypeEffectiveness.getEffectiveness(this.moveType, target.getDefenderType());
        int damage = (int)((double)this.attackPower * effectiveness);
        
        // Apply   the damage to the target's HP
        target.reduceHp(damage); 
        
        System.out.println(this.name + " attacks " + target.getName() + " for " + damage + " damage!");
        System.out.println(target.getName() + " now has " + target.getHp() + " HP.");
    }
   
    public void applyAttackBoost(int boostAmount) {
    	this.increaseAttack += boostAmount; // Add to the total boost amount
        this.attackPower = this.baseAttackPower + this.increaseAttack;
    }
    
    public void applySpeedBoost(int boostAmount) {
    	this.increaseSpeed += boostAmount; // Add to the total boost amount
        this.speed = this.baseSpeed + this.increaseSpeed; // Update current speed
    }
    
    public void removeSpeedBoost(int boostAmount) {
    	this.increaseSpeed -= boostAmount; // Subtract from the total boost amount
        // Ensure the increase doesn't go negative
    	if (this.increaseSpeed < 0) {
    		this.increaseSpeed = 0;
    	}
        this.speed = this.baseSpeed + this.increaseSpeed; // Update current speed
    } 
    
    public void removeAttackBoost(int boostAmount) {
    	this.increaseAttack -= boostAmount; // Subtract from the total boost amount
        // Ensure the increase doesn't go negative
    	if (this.increaseAttack < 0) {
    		this.increaseAttack = 0;
    	}
        this.attackPower = this.baseAttackPower + this.increaseAttack; // Update current attackPower
    }
    public void resetBoosts() {
        this.increaseAttack = 0;
        this.attackPower = this.baseAttackPower; // Reset current attack to base
        this.increaseSpeed = 0;
        this.speed = this.baseSpeed;             // Reset current speed to base
    }
    			
    //toString
    public String toString() {
        return "Name: " + name +
               "\nHP: " + hp +
               "\nAttack Power: " + attackPower +
               "\nMove Type: " + moveType +
               "\nDefender Type: " + defenderType +
               "\nBase Speed: " + baseSpeed +
               "\nBase Attack Power: " + baseAttackPower;
}
}
