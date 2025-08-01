import java.util.*;

public class Game {
    private Player player;
    private Scanner scanner;
    private Random random;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void start() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        this.player = new Player(name);
     
        System.out.println("Welcome, " + name + "! Let's catch your first Pokémon!");
        System.out.println("---------------------------");

        chooseStarter();

        boolean running = true;
        while (running) {
            System.out.println("\n---------------------------");
            System.out.println("~Menu~");
            System.out.println("1. Show My Pokémon");
            System.out.println("2. Battle");
            System.out.println("3. Exit");
            System.out.println("4. Show Inventory");  //added for inventory
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

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
                    System.out.println("\n---------------------------");
                    System.out.println("Thanks for playing!");
                    System.out.println("---------------------------");
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

        System.out.println("\n---------------------------");
        System.out.println("Two wild Pokémon appeared!");
        System.out.println("---------------------------");
        for (Pokemon p : wilds) {
            System.out.println(p + "\n");
        }

        if (player.getTeam().isEmpty()) {
            System.out.println("You have no Pokémon to battle.");
            return;
        }

        Pokemon p1 = player.getTeam().get(0); // starter always goes first
        Pokemon p2;

        if (player.getTeam().size() > 1) {
            p2 = player.getTeam().get(1); // second caught Pokémon
        } else {
            System.out.println("You only have one Pokémon. Generating a helper for second battle.");
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
    }
    private void battleRound(Pokemon playerPoke, Pokemon wildPoke) {
        System.out.println("\n---------------------------");
        System.out.println("Battle Start: " + playerPoke.getName() + " vs " + wildPoke.getName());
        System.out.println("---------------------------");

        // Determine which pokemon is the first to attack
        Pokemon firstRound_firstAttacker;
        Pokemon firstRound_secondAttacker;
        
        if (playerPoke.getSpeed() > wildPoke.getSpeed()) {
            firstRound_firstAttacker = playerPoke;
            firstRound_secondAttacker = wildPoke;
            System.out.println(playerPoke.getName() + " is faster and attacks first!");
        } else if (wildPoke.getSpeed() > playerPoke.getSpeed()) {
            firstRound_firstAttacker = wildPoke;
            firstRound_secondAttacker = playerPoke;
            System.out.println(wildPoke.getName() + " is faster and attacks first!");
        } else {
            if (random.nextBoolean()) {
                firstRound_firstAttacker = playerPoke;
                firstRound_secondAttacker = wildPoke;
                System.out.println("Both Pokémon speeds TIED! " + playerPoke.getName() + " attacks first by random generator!");
            } else {
                firstRound_firstAttacker = wildPoke;
                firstRound_secondAttacker = playerPoke;
                System.out.println("Both Pokémon speeds TIED! " + wildPoke.getName() + " attacks first by random generator!");
            }
        }
        System.out.println("---------------------------");

        // This is the battle loop. It will continue until one Pokémon is defeated.
        while (!playerPoke.isDefeated() && !wildPoke.isDefeated()) {

            // --- FIRST ATTACKER'S TURN ---
            // Only allow player action if their Pokémon is the one attacking
            if (firstRound_firstAttacker == playerPoke) {
                System.out.println("\nIt's your turn, " + playerPoke.getName() + "!");
                System.out.println("1. Attack");
                if (playerHasItem()) {
                    System.out.println("2. Use Booster Item");
                }
                System.out.print("Choose your action: ");
                int userChoice = scanner.nextInt();
                scanner.nextLine();

                switch (userChoice) {
                    case 1:
                       this.executeSingleAttack(firstRound_firstAttacker, firstRound_secondAttacker);
                        break;
                    case 2:
                    	if (playerHasItem()) {
                            player.getInventory().showInventory();
                            System.out.print("Enter item name to use: ");
                            String itemName = scanner.nextLine();
                            
                            if (Boosters.Boxing_Gloves.equalsIgnoreCase(itemName)) {
                                if (player.getInventory().useItem(itemName)) {
                                    int boostAmount = Boosters.getAttackBoost(Boosters.Boxing_Gloves);
                                    playerPoke.applyAttackBoost(boostAmount);
                                    System.out.println(playerPoke.getName() + "'s attack power increased by " + boostAmount + "!");
                                } else {
                                    System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
                                }
                            } else if (Boosters.Boots.equalsIgnoreCase(itemName)) {
                                if (player.getInventory().useItem(itemName)) {
                                    int boostAmount = Boosters.getSpeedBoost(Boosters.Boots);
                                    playerPoke.applySpeedBoost(boostAmount);
                                    System.out.println(playerPoke.getName() + "'s speed increased by " + boostAmount + "!");
                                } else {
                                    System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
                                }
                            } else {
                                System.out.println("Item not found! Please enter a valid item. Your turn is skipped.");
                            }
                        } else {
                            System.out.println("Unable to use item: Item Insufficient. Your turn is skipped.");
                        }
                        break;
                        
                    default:
                        System.out.println("Invalid choice, turn skipped.");
                        break;
                }
            } else {
                // Wild Pokémon's turn
                System.out.println("\n" + wildPoke.getName() + "'s turn to attack!");
                executeSingleAttack(firstRound_firstAttacker, firstRound_secondAttacker);
            }

            // Check if the battle is over after the first attack
            if (playerPoke.isDefeated() || wildPoke.isDefeated()) {
                break;
            }

            // --- SECOND ATTACKER'S TURN ---
            // Only allow player action if their Pokémon is the one attacking
            if (firstRound_secondAttacker == playerPoke) {
                System.out.println("\nIt's your turn, " + playerPoke.getName() + "!");
                System.out.println("1. Attack");
                if (playerHasItem()) {
                    System.out.println("2. Use Booster Item");
                }
                System.out.print("Choose your action: ");
                int userChoice = scanner.nextInt();
                scanner.nextLine();

                switch (userChoice) {
                    case 1:
                        executeSingleAttack(firstRound_secondAttacker, firstRound_firstAttacker);
                        break;
                    case 2:
                        // ... (rest of your booster item logic here) ...
                        break;
                    default:
                        System.out.println("Invalid choice, turn skipped.");
                        break;
                }
            } else {
                // Wild Pokémon's turn
                System.out.println("\n" + wildPoke.getName() + "'s turn to attack!");
                executeSingleAttack(firstRound_secondAttacker, firstRound_firstAttacker);
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
    }

    private void executeSingleAttack(Pokemon attacker, Pokemon defender) {
        if (attacker.isDefeated() || defender.isDefeated()) {
            return;
        }
        attacker.attack(defender);
        System.out.println("---------------------------");
    }
    
    private boolean playerHasItem() {
        for (String item : player.getInventory().getItems()) {
            if ("Boxing Gloves".equalsIgnoreCase(item) || "Boots".equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }
  
    private void attemptCatch(Pokemon wild) {
        System.out.println("\n---------------------------");
        System.out.println("Attempt to catch " + wild.getName() + "? (y/n): ");
        System.out.println("---------------------------");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
        	if (player.getInventory().useItem("Poké Ball")) {               //edited to access inventory
        	    int chance = random.nextInt(100);
        	    if (chance < 50) {
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
}
