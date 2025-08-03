import java.util.*;

public class Game {
    private Player player;
    private Scanner scanner;
    private Random random;
    private List<String> usedBoosterItems;
    private boolean boosterItemUsedInBattle;
    private boolean running;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.usedBoosterItems = new ArrayList<>();
        this.boosterItemUsedInBattle = false;
    }

    public void start() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        this.player = new Player(name);
     
        printInstructions();
        
        // Add items using polymorphism
        player.getInventory().addItem(new BoxingGloves()); // polymorphic item
        player.getInventory().addItem(new Boots());        // polymorphic item


        this.running = true;
        while (running) {
            System.out.println("\n---------------------------");
            System.out.println("~Menu~");
            System.out.println("1. Show My PokÃ©mon");
            System.out.println("2. Battle");
            System.out.println("3. Exit");
            System.out.println("4. Show Inventory");  //added for inventory
            System.out.print("Choose: ");

            int choice = getValidatedNumberInput();

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
                    player.getInventory().showInventory();
                    break;
                default:
                    System.out.println("\n---------------------------");
                    System.out.println("Invalid choice.");
                    System.out.println("---------------------------");
            }
        }
    }

    private void chooseStarter() {
        List<Pokemon> starters = generateRandomPokemons(3);
        System.out.println("\n---------------------------");
        System.out.println("Choose your starter PokÃ©mon:");
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
            System.out.println("Invalid choice. Defaulting to first PokÃ©mon.");
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
            System.out.println("You only have one Pokemon. Generating a helper for second battle.");
            p2 = generateRandomPokemons(1).get(0); // helper if player has only one
        }
        System.out.println("\n--- Starting Battle 1 ---");
        battleRound(p1, wilds.get(0));
        
        if (!p1.isDefeated()) { 
             System.out.println("\n--- Starting Battle 2 ---");
             battleRound(p2, wilds.get(1));
        } else {
            System.out.println(p1.getName() + " was defeated. Cannot proceed to the second battle.");
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
                    System.out.print("Choose your action (1 or 2): ");
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
                                    System.out.println("---------------------------");
                                    System.out.println(playerPoke.getName() + "'s speed increased by " + boostAmount + " from " + playerPoke.getBaseSpeed() + " to " + playerPoke.getSpeed() + "!");
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
            player.increaseScore(10);
            attemptCatch(wildPoke);
        } else if (playerPoke.isDefeated()) {
            System.out.println("Your " + playerPoke.getName() + " was defeated.");
        }
        playerPoke.resetBoosts();
        endGame();
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
    
    
    // remove this one, usePokeball in Pokeballs.java
    private void attemptCatch(Pokemon wild) {
        System.out.println("\n---------------------------");
        System.out.println("Attempt to catch " + wild.getName() + "? (y/n): ");
        System.out.println("---------------------------");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
        	if (player.getInventory().useItem("PokeBall")) {               
                /*System.out.println("Select a Poké Ball type to use: \n1. Poké Ball\n2. Great Ball\n3. Ultra Ball \n4. Master Ball");
                int type = scanner.nextInt();*/
                
                //edited to access inventory
        	    int chance = random.nextInt(100);
        	    if (chance > 50) {
        	        player.addPokemon(wild);
        	    } else {
        	        System.out.println(wild.getName() + " escaped!");
                }
            }
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
        System.out.println("• If you have a second Pokémon, it will fight the second wild one.");
        System.out.println("• If not, a helper Pokémon will assist you.");
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
        System.out.println("• After winning a battle, you may try to catch the wild Pokémon.");
        System.out.println("• Poké Balls are required and have a 50% success rate.");
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
    	this.running = false;
    	System.out.println("\n---------------------------");
        System.out.printf("Final score: %d\n",player.getScore());
        System.out.println("Thanks for playing!");
        System.out.println("---------------------------");
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
