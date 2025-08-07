
import java.util.*;

public class Game { //attributes
    private Player player;
    private Scanner scanner;
    private Random random;
    private List<String> usedBoosterItems;
    private boolean boosterItemUsedInBattle;
    private boolean running;
    private boolean gameEnded = false;
    private ScoreManager scoreManager;

    public Game() { //constructors
        this.scanner = new Scanner(System.in); //initiate the scanner
        this.random = new Random(); //random number generator
        this.usedBoosterItems = new ArrayList<>();
        this.boosterItemUsedInBattle = false;
        this.scoreManager = new ScoreManager();
    }

    public void start() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        this.player = new Player(name);
     
        printInstructions();
        
        // Add items using polymorphism
        player.getInventory().addItem(new BoxingGloves()); // polymorphic item
        player.getInventory().addItem(new Boots());        // polymorphic item
        player.getInventory().addItem(new Pokeballs());    // polymorphic item

        this.running = true;
        while (running) {
            System.out.println("\n---------------------------");
            System.out.println("~Menu~");
            System.out.println("1. Show My Pokemon");
            System.out.println("2. Battle");
            System.out.println("3. Exit"); 
            System.out.println("4. Show Inventory");  
            System.out.print("Choose: ");

            int choice = getValidatedNumberInput(); //check if input is valid

            switch (choice) {
                case 1:
                    System.out.println("\n---------------------------");
                    player.showTeam();
                    break;
                case 2:
                    battle();
                    break;
                case 3:
                    running = false;
                    endGame();
                    break;
                case 4:
                    System.out.println();
                    player.getInventory().showInventory();
                    break;
                default:
                    System.out.println("\n---------------------------");
                    System.out.println("Invalid choice.");
                    System.out.println("---------------------------");
                    // In the main menu switch statement:
                case 5:
                    System.out.println("\n--- TOP 5 SCORES ---");
                    List<PlayerScore> topScores = scoreManager.getTopScores();
                    if (topScores.isEmpty()) {
                        System.out.println("No scores recorded yet.");
                    } else {
                        for (int i = 0; i < topScores.size(); i++) {
                            PlayerScore score = topScores.get(i);
                            System.out.printf("%d. %s - %d\n", i+1, score.getName(), score.getScore());
                        }
    }
                    break;
            }
        }
    }

    private void chooseStarter() {
        List<Pokemon> starters = generateRandomPokemons(3);
        System.out.println("\n---------------------------");
        System.out.println("Choose your starter Pokémon:");
        System.out.println("---------------------------");

        for (int i = 0; i < starters.size(); i++) {
            System.out.println("[" + i + "]\n" + starters.get(i) + "\n");
        }

        System.out.print("Enter your choice (0-2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice >= 0 && choice < starters.size()) {
            player.addPokemon(starters.get(choice));
        } else {
            System.out.println("Invalid choice. Defaulting to first Pokémon.");
            player.addPokemon(starters.get(0));
        }
    }

    private void battle() {
        List<Pokemon> wilds = generateRandomPokemons(2);
        this.boosterItemUsedInBattle = false;

        System.out.println("\n---------------------------");
        System.out.println("Two wild Pokemon appeared!");
        System.out.println("---------------------------");
        for (Pokemon p : wilds) {
            System.out.println(p + "\n");
        }

        if (player.getTeam().isEmpty()) {
            System.out.println("You have no Pokemon to battle.");
            return;
        }

        Pokemon p1 = player.getTeam().get(0); // starter always goes first
        Pokemon p2;

        if (player.getTeam().size() > 1) {
            p2 = player.getTeam().get(1); // second caught PokÃ©mon
        } else {
            System.out.println("... Generating a helper for second battle...");
            p2 = generateRandomPokemons(1).get(0); // helper if player has only one
        }
        System.out.println("\n--- Starting Battle 1 ---");
        battleRound(p1, wilds.get(0));
        
        if (!p1.isDefeated()) { 
             System.out.println("\n--- Starting Battle 2 ---");
             battleRound(p2, wilds.get(1));
        } else {
            System.out.println(p1.getName() + " was defeated. Cannot proceed to the second battle.");
        }
        
     // After both battle rounds (or skip) are done
        if (player.getTeam().stream().allMatch(Pokemon::isDefeated)) {
            System.out.println("\nAll your Pokemon have been defeated.");
            running = false;
            endGame();
        }

    }
    private void battleRound(Pokemon playerPoke, Pokemon wildPoke) {
        System.out.println("\n---------------------------");
        System.out.println("Battle Start: " + playerPoke.getName() + " vs " + wildPoke.getName());
        System.out.println("---------------------------");

        // This is the battle loop. It will continue until one Pokémon is defeated.
        while (!playerPoke.isDefeated() && !wildPoke.isDefeated()) {
        		//PLAYER'S TURN
                System.out.println("\nIt's your turn, " + playerPoke.getName() + "!");
                System.out.println("1. Attack");
                if (playerHasItem()) {
                    System.out.println("2. Use Booster Item");
                }
                int userChoice = -1;
                boolean validInput = false;
               
                while (!validInput) {                   	
                    if (boosterItemUsedInBattle) {
                    	System.out.print("Choose your action: ");                    	
                    	boosterItemUsedInBattle = true;
                    }
                    else {
                        System.out.print("Choose your action (1 or 2): ");               	
                    }
                    
                    String input = scanner.nextLine();
                    
                try {
                    userChoice = Integer.parseInt(input);
                    if (userChoice == 1 || (userChoice == 2 && playerHasItem())) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid number. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input (letters not allowed). Try again.");
                }
                }

             
                boolean playerUsedBoots = false;
                boolean playerUsedBoxingGloves = false;
                boolean playerAttacked = false;
                switch (userChoice) {
                    case 1:
                       this.executeSingleAttack(playerPoke, wildPoke);                      
                       playerAttacked = true;
                        break;
                    case 2:
                        if (playerHasItem()) {
                            List<String> usableItems = getUsableItemsFromInventory();

                            if (usableItems.isEmpty()) {
                                System.out.println("You have no usable battle items.");
                                break;
                            }

                            System.out.println("\nChoose an item to use:");
                            for (int i = 0; i < usableItems.size(); i++) {
                                System.out.println(i + ": " + usableItems.get(i));
                            }

                            System.out.print("Enter the number of the item: ");
                            int choice = -1;
                            String input = scanner.nextLine();
                            try {
                                choice = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input '" + input + "'. Turn skipped.");
                                break;
                            }


                            if (choice < 0 || choice >= usableItems.size()) {
                                System.out.println("Invalid choice. Turn skipped.");
                                break;
                            }

                            String itemName = usableItems.get(choice);

                            if (Boosters.Boxing_Gloves.equalsIgnoreCase(itemName)) {
                                if (player.getInventory().useItem(itemName)) {
                                    this.boosterItemUsedInBattle = true;
                                    int boostAmount = Boosters.getAttackBoost(Boosters.Boxing_Gloves);
                                    playerPoke.applyAttackBoost(boostAmount);
                                    System.out.println(playerPoke.getName() + "'s attack power increased by " + boostAmount + " from " + playerPoke.getBaseAttackPower() + " to " + playerPoke.getAttackPower() + "!");
                                    System.out.println("---------------------------");
                                    this.executeSingleAttack(playerPoke, wildPoke);
                                    playerPoke.resetBoosts();
                                    playerUsedBoxingGloves = true;
                                } else {
                                    System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
                                }

                            } else if (Boosters.Boots.equalsIgnoreCase(itemName)) {
                                if (player.getInventory().useItem(itemName)) {
                                    this.boosterItemUsedInBattle = true;
                                    int boostAmount = Boosters.getSpeedBoost(Boosters.Boots);
                                    playerPoke.applySpeedBoost(boostAmount);
                                    System.out.println(playerPoke.getName() + " used Boots and now attacks first in this round!");                                                                       
                                    System.out.println(playerPoke.getName() + "'s speed increased by " + boostAmount + " from " + playerPoke.getBaseSpeed() + " to " + playerPoke.getSpeed() + "!");
                                    System.out.println("---------------------------");
                                    this.executeSingleAttack(playerPoke, wildPoke);
                                    playerPoke.resetBoosts();
                                    playerUsedBoots = true;
                                    this.executeSingleAttack(wildPoke, playerPoke);
                                } else {
                                    System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
                                }

                            } else {
                                System.out.println("Item cannot be used in this battle!");
                            }

                        } else {
                            System.out.println("Invalid action choice, your turn is skipped!");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice, turn skipped.");
                        break;
            }
          
            
            if (!playerUsedBoots) {
            	this.executeSingleAttack(wildPoke, playerPoke);
            	}
        
            // Check if the battle is over 
            if (playerPoke.isDefeated() || wildPoke.isDefeated()) {
            	break;
            }
            }

        // Battle outcome and cleanup
        if (wildPoke.isDefeated()) {
            System.out.println("Your " + playerPoke.getName() + " defeated " + wildPoke.getName() + "!");
            player.increaseScore(100);
            attemptCatch(wildPoke);
        } else if (playerPoke.isDefeated()) {
            System.out.println("Your " + playerPoke.getName() + " was defeated.");
        }
        playerPoke.resetBoosts();
        }
    

    private void executeSingleAttack(Pokemon attacker, Pokemon defender) {
        if (attacker.isDefeated() || defender.isDefeated()) {
            return;
        }
        attacker.attack(defender);
        System.out.println("---------------------------");
    }
    
    private boolean playerHasItem() {
    	if (boosterItemUsedInBattle) {
    		return false;
    	}
        for (String item : player.getInventory().getItems()) {
            if ("Boxing Gloves".equalsIgnoreCase(item) || "Boots".equalsIgnoreCase(item)
            	&&  !this.usedBoosterItems.contains(item)){
                return true;
            }
        }
        return false;
    }
    
    
    private void attemptCatch(Pokemon wild) {
        System.out.println("\n---------------------------");
        String input;
        do {
        	System.out.print("Attempt to catch " + wild.getName() + "? (y/n): ");
        	input = scanner.nextLine().toLowerCase();
        	System.out.println();
        	if (!input.equals("y") && !input.equals("n")) {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
        while (!input.equals("y") && !input.equals("n"));
        	System.out.println();
            

        if (input.equalsIgnoreCase("y")) {       
            System.out.println("Choose a Poké Ball to use: \n1. Poké Ball - 40% success rate \n2. Great Ball - 60% success rate \n3. Ultra Ball - 80% success rate \n4. Master Ball - 100% success rate");
            System.out.print("Choose: ");
            int type = getValidatedNumberInput(); 

            int catchRate = 0;
            switch(type) {
                case 1:     
                    System.out.println("\nYou used a Poke Ball!");
                    catchRate = 10;
                    break;
                case 2:     
                    System.out.println("\nYou used a Great Ball!");
                    catchRate = 25;
                    break;
                case 3:     
                    System.out.println("\nYou used a Ultra Ball!");
                    catchRate = 35;
                    break;
                case 4:     
                    System.out.println("\nYou used a Master Ball!");
                    catchRate = 51; 
                    break;
                default:
                    System.out.println("Invalid choice. You used a random Poke Ball!");
                    catchRate = random.nextInt(25);
                    break;
                    // In the main menu switch statement:
            }
                
        	int chance = random.nextInt(50);
        	if ((chance + catchRate) > 50) {
                System.out.println("You successfully caught " + wild.getName() + "!");
    	        player.addPokemon(wild);
                player.increaseScore(100);
    	    } else {
        	    System.out.println(wild.getName() + " escaped!\n");
            }
        } else if (input.equalsIgnoreCase("n")) {
            System.out.println("You chose not to catch " + wild.getName() + ".");
        } else {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            attemptCatch(wild);
        }
    }

    private List<Pokemon> generateRandomPokemons(int count) {
        String[] names = { "Charmander", "Squirtle", "Bulbasaur", "Pikachu", "Eevee", "Growlithe", "Tsareena" };
        String[] types = { "Fire", "Water", "Grass" };

        List<Pokemon> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = names[random.nextInt(names.length)];
            int hp = 50 + random.nextInt(51);
            int attack = 10 + random.nextInt(11);
            int speed = 10 + random.nextInt(11);
            String moveType = types[random.nextInt(types.length)];
            String defenderType = types[random.nextInt(types.length)];

            list.add(new Pokemon(name, hp, attack, speed, moveType, defenderType));
        }

        return list;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
    
    private void printInstructions() {
        System.out.println("===============================================");
        System.out.printf("   TRAINER %s, WELCOME TO THE WORLD OF POKÉMON!\n", player.getName());
        System.out.println("===============================================");
        System.out.println("Your journey begins now. Prepare to battle wild Pokémon and build your team!");
        System.out.println();
        System.out.println("🕹️ GAME INSTRUCTIONS:");
        System.out.println("• You will encounter 2 wild Pokémon in every battle.");
        System.out.println("• Your first Pokémon will fight the first wild one.");
        System.out.println("• If you have a second Pokemon, it will fight the second wild one.");
        System.out.println("• If not, a helper Pokemon will assist you.");
        System.out.println();
        System.out.println("⚔️ DURING BATTLE:");
        System.out.println("1. Choose to attack the wild Pokémon.");
        System.out.println("2. If you have booster items, you can choose to:");
        System.out.println("   - Use 'Boxing Gloves' to boost attack.");
        System.out.println("   - Use 'Boots' to boost speed.");
        System.out.println("3. Speed determines who attacks first.");
        System.out.println();
        System.out.println("🎁 ITEMS:");
        System.out.println("• Use items during your turn by entering their name exactly.");
        System.out.println("• Items can only be used once per turn and must be in your inventory.");
        System.out.println();
        System.out.println("🎯 CATCHING POKÉMON:");
        System.out.println("• After winning a battle, you may try to catch the wild Pokemon using Poké Balls.");
        System.out.println("• Different Poke Balls have different success rates so choose carefully.");
        System.out.println("===============================================\n");
        
        System.out.print("\nPress SPACE then ENTER to continue...");
        String input = scanner.nextLine();
        while (!input.equals(" ")) {
            System.out.print("Please press only SPACE then ENTER to proceed: ");
            input = scanner.nextLine();
        }
        chooseStarter();
    }

    
    private void endGame() {
        if (gameEnded) return;
        gameEnded = true;
        System.out.println("\n---------------------------");
        System.out.printf("Final score: %d\n", player.getScore());
    
    // Add the player's score to the leaderboard
        scoreManager.addScore(player.getName(), player.getScore());
    
    // Show top scores
        System.out.println("\n--- TOP 5 SCORES ---");
        List<ScoreManager.PlayerScore> topScores = scoreManager.getTopScores();
        if (topScores.isEmpty()) {
            System.out.println("No scores recorded yet.");
        } else {
            for (int i = 0; i < topScores.size(); i++) {
                ScoreManager.PlayerScore score = topScores.get(i);
                System.out.printf("%d. %s - %d\n", i+1, score.getName(), score.getScore());
            }
        }
        System.out.println("---------------------------");
        System.out.println("Thanks for playing!");
        System.out.println("---------------------------");
        
        System.out.print("Would you like to return to the main menu? (y/n): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            this.running = true;
            this.gameEnded = false;
        } else {
            this.running = false;
            System.exit(0);
        }
    }
    private List<String> getUsableItemsFromInventory() {
        List<String> uniqueItems = new ArrayList<>();
        for (String item : player.getInventory().getItems()) {
            if (!uniqueItems.contains(item) &&
               (item.equalsIgnoreCase(Boosters.Boxing_Gloves) || item.equalsIgnoreCase(Boosters.Boots))) {
                uniqueItems.add(item);
            }
        }
        return uniqueItems;
    }
    
    private int getValidatedNumberInput() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input (numbers only). Try again: ");
            }
        }
    }
    

}
