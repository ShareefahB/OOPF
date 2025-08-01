import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;

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
      String name = this.scanner.nextLine();
      this.player = new Player(name);
      System.out.println("Welcome, " + name + "! Let's catch your first PokÃ©mon!");
      System.out.println("---------------------------");
      this.chooseStarter();
      boolean running = true;

      while(running) {
         System.out.println("\n---------------------------");
         System.out.println("~Menu~");
         System.out.println("1. Show My Pokemon");
         System.out.println("2. Battle");
         System.out.println("3. Exit");
         System.out.println("4. Show Inventory");
         System.out.print("Choose: ");
         int choice = this.scanner.nextInt();
         this.scanner.nextLine();
         switch(choice) {
         case 1:
            System.out.println("\n---------------------------");
            this.player.showTeam();
            break;
         case 2:
            this.battle();
            break;
         case 3:
            running = false;
            System.out.println("\n---------------------------");
            System.out.println("Thanks for playing!");
            System.out.println("---------------------------");
            break;
         case 4:
            this.player.getInventory().showInventory();
            break;
         default:
            System.out.println("\n---------------------------");
            System.out.println("Invalid choice.");
            System.out.println("---------------------------");
         }
      }

   }

   private void chooseStarter() {
      List<Pokemon> starters = this.generateRandomPokemons(3);
      System.out.println("\n---------------------------");
      System.out.println("Choose your starter Pokemon:");
      System.out.println("---------------------------");

      int choice;
      for(choice = 0; choice < starters.size(); ++choice) {
         System.out.println("[" + choice + "]\n" + String.valueOf(starters.get(choice)) + "\n");
      }

      System.out.print("Enter your choice (0-2): ");
      choice = this.scanner.nextInt();
      this.scanner.nextLine();
      if (choice >= 0 && choice < starters.size()) {
         this.player.addPokemon((Pokemon)starters.get(choice));
      } else {
         System.out.println("Invalid choice. Defaulting to first Pokemon.");
         this.player.addPokemon((Pokemon)starters.get(0));
      }
        player.addItemToInventory("Poke Ball");
        player.addItemToInventory("Poke Ball");
        player.addItemToInventory("Poke Ball");
   }
   private void battle() {
      List<Pokemon> wilds = this.generateRandomPokemons(2);
      System.out.println("\n---------------------------");
      System.out.println("Two wild Pokemon appeared!");
      System.out.println("---------------------------");
      Iterator var3 = wilds.iterator();

      Pokemon p1;
      while(var3.hasNext()) {
         p1 = (Pokemon)var3.next();
         System.out.println(String.valueOf(p1) + "\n");
      }

      if (this.player.getTeam().isEmpty()) {
         System.out.println("You have no Pokemon to battle.");
      } else {
         p1 = (Pokemon)this.player.getTeam().get(0);
         Pokemon p2;
         if (this.player.getTeam().size() > 1) {
            p2 = (Pokemon)this.player.getTeam().get(1);
         } else {
            System.out.println("You only have one Pokemon. Generating a helper for second battle.");
            p2 = (Pokemon)this.generateRandomPokemons(1).get(0);
         }

         System.out.println("\n--- Starting Battle 1 ---");
         this.battleRound(p1, (Pokemon)wilds.get(0));
         if (!p1.isDefeated()) {
            System.out.println("\n--- Starting Battle 2 ---");
            this.battleRound(p2, (Pokemon)wilds.get(1));
         } else {
            System.out.println(p1.getName() + " was defeated. Cannot proceed to the second battle.");
         }

      }
   }

   private void battleRound(Pokemon playerPoke, Pokemon wildPoke) {
      System.out.println("\n---------------------------");
      PrintStream var10000 = System.out;
      String var10001 = playerPoke.getName();
      var10000.println("Battle Start: " + var10001 + " vs " + wildPoke.getName());
      System.out.println("---------------------------");
      String firstAttackerMessage = "";
      Pokemon firstRound_firstAttacker;
      Pokemon firstRound_secondAttacker;
      if (playerPoke.getSpeed() > wildPoke.getSpeed()) {
         firstRound_firstAttacker = playerPoke;
         firstRound_secondAttacker = wildPoke;
         firstAttackerMessage = playerPoke.getName() + " is faster and attacks first!";
         System.out.println("---------------------------");
      } else if (wildPoke.getSpeed() > playerPoke.getSpeed()) {
         firstRound_firstAttacker = wildPoke;
         firstRound_secondAttacker = playerPoke;
         firstAttackerMessage = wildPoke.getName() + " is faster and attacks first!";
         System.out.println("---------------------------");
      } else {
         if (this.random.nextBoolean()) {
            firstRound_firstAttacker = playerPoke;
            firstRound_secondAttacker = wildPoke;
            System.out.println("Both pokemon speeds TIED! " + wildPoke.getName() + " attacks first by random generator!");
         } else {
            firstRound_firstAttacker = wildPoke;
            firstRound_secondAttacker = playerPoke;
            System.out.println("Both pokemon speeds TIED! " + playerPoke.getName() + " attacks first by random generator!");
         }

         System.out.println("---------------------------");
      }

      System.out.println("1. Attack");
      boolean playerHasItem = this.playerHasItem();
      if (playerHasItem) {
         System.out.println("2. Use Booster Item");
      }

      System.out.println("Choose your action: ");
      int userChoice = this.scanner.nextInt();
      this.scanner.nextLine();
      boolean firstToAttackInBattle = true;
      switch(userChoice) {
      case 1:
         break;
      case 2:
         if (playerHasItem) {
            this.player.getInventory().showInventory();
            System.out.print("Enter item name to use: ");
            String itemName = this.scanner.nextLine();
            int boostAmount;
            if ("Boxing Gloves".equalsIgnoreCase(itemName)) {
               if (this.player.getInventory().useItem(itemName)) {
                  boostAmount = Boosters.getAttackBoost("Boxing Gloves");
                  playerPoke.applyAttackBoost(boostAmount);
                  var10000 = System.out;
                  var10001 = playerPoke.getName();
                  var10000.println(var10001 + "'s attack power increased by " + boostAmount + "!");
               } else {
                  System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
               }
            } else if ("Boots".equalsIgnoreCase(itemName)) {
               if (this.player.getInventory().useItem(itemName)) {
                  boostAmount = Boosters.getSpeedBoost("Boots");
                  playerPoke.applySpeedBoost(boostAmount);
                  var10000 = System.out;
                  var10001 = playerPoke.getName();
                  var10000.println(var10001 + "'s speed increased by " + boostAmount + "!");
               } else {
                  System.out.println("Could not use " + itemName + ". You might not have it or it's depleted.");
               }
            } else {
               System.out.println("Item not found! Please enter a valid item. ");
            }
         } else {
            System.out.println("Unable to use item: Item Insuffficient");
         }

         if (!wildPoke.isDefeated() && !playerPoke.isDefeated()) {
            wildPoke.attack(playerPoke);
            System.out.println("---------------------------");
            if (playerPoke.isDefeated()) {
               System.out.println("Your " + playerPoke.getName() + " was defeated.");
               playerPoke.resetBoosts();
               return;
            }
         }

         firstToAttackInBattle = false;
         break;
      default:
         System.out.println("Invalid action. Your turn is skipped lol.");
         if (!wildPoke.isDefeated() && !playerPoke.isDefeated()) {
            wildPoke.attack(playerPoke);
            System.out.println("---------------------------");
            if (playerPoke.isDefeated()) {
               System.out.println("Your " + playerPoke.getName() + " was defeated.");
               playerPoke.resetBoosts();
               return;
            }
         }
      }

      while(!playerPoke.isDefeated() && !wildPoke.isDefeated()) {
         System.out.println("\n" + firstAttackerMessage);
         System.out.println("\n---------------------------");
         this.executeSingleAttack(firstRound_firstAttacker, firstRound_secondAttacker);
         if (firstRound_firstAttacker.isDefeated()) {
            break;
         }

         this.executeSingleAttack(firstRound_firstAttacker, firstRound_secondAttacker);
         if (firstRound_secondAttacker.isDefeated()) {
            break;
         }

         Pokemon temp = firstRound_firstAttacker;
         firstRound_firstAttacker = firstRound_secondAttacker;
         firstRound_secondAttacker = temp;
      }

      if (wildPoke.isDefeated()) {
         var10000 = System.out;
         var10001 = playerPoke.getName();
         var10000.println("Your " + var10001 + " defeated " + wildPoke.getName() + "!");
         this.player.increaseScore(10);
         this.attemptCatch(wildPoke);
      } else if (playerPoke.isDefeated()) {
         System.out.println("Your " + playerPoke.getName() + " was defeated.");
      }

      playerPoke.resetBoosts();
   }

   private boolean playerHasItem() {
      Iterator var2 = this.player.getInventory().getItems().entrySet().iterator();

      Entry entry;
      do {
         do {
            if (!var2.hasNext()) {
               return false;
            }

            entry = (Entry)var2.next();
         } while(!"Boxing Gloves".equalsIgnoreCase((String)entry.getKey()) && !"Boots".equalsIgnoreCase((String)entry.getKey()));
      } while((Integer)entry.getValue() <= 0);

      return true;
   }

   private void executeSingleAttack(Pokemon attacker, Pokemon defender) {
      if (!attacker.isDefeated() && !defender.isDefeated()) {
         attacker.attack(defender);
         System.out.println("---------------------------");
      }
   }

   private void attemptCatch(Pokemon wild) {
      System.out.println("\n---------------------------");
      System.out.println("Attempt to catch " + wild.getName() + "? (y/n): ");
      System.out.println("---------------------------");
      String input = this.scanner.nextLine();
      if (input.equalsIgnoreCase("y") && this.player.getInventory().useItem("Poke Ball")) {
         int chance = this.random.nextInt(100);
         if (chance < 50) {
            this.player.addPokemon(wild);
         } else {
            System.out.println(wild.getName() + " escaped!");
         }
      }

   }

   private List<Pokemon> generateRandomPokemons(int count) {
      String[] names = new String[]{"Charmander", "Squirtle", "Bulbasaur", "Pikachu", "Eevee", "Growlithe", "Tsareena"};
      String[] types = new String[]{"Fire", "Water", "Grass"};
      List<Pokemon> list = new ArrayList();

      for(int i = 0; i < count; ++i) {
         String name = names[this.random.nextInt(names.length)];
         int hp = 50 + this.random.nextInt(51);
         int attack = 10 + this.random.nextInt(11);
         int speed = 10 + this.random.nextInt(11);
         String moveType = types[this.random.nextInt(types.length)];
         String defenderType = types[this.random.nextInt(types.length)];
         list.add(new Pokemon(name, hp, attack, speed, moveType, defenderType));
      }

      return list;
   }

   public static void main(String[] args) {
      Game game = new Game();
      game.start();
   }
}
