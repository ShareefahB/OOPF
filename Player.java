
import java.util.ArrayList;
import java.util.List;

//attributes
public class Player {
    private String name;
    private List<Pokemon> team;
    private int score;
    private Inventory inventory;
    // Constructor
    public Player(String name) {
        this.name = name;
        this.team = new ArrayList<>();
        this.score = 0;
        this.inventory = new Inventory(); //added for inventory
    }

    // setters getters 
    
    //name
    public String getName() { 
    	return name; 
    }
    public void setName(String name) { 
    	this.name = name; 
    }
    
    //team
    public List<Pokemon> getTeam() { 
    	return team; 
    }
    
    //score
    public int getScore() { 
    	return score;
    }    
    public void setScore(int score) { 
    	this.score = score; 
    }

    // Methods
    public void addPokemon(Pokemon pokemon) {
        if (team.size() < 6) {
            team.add(pokemon);
            System.out.println(pokemon.getName() + " has been added to your team!");
        } else {
            System.out.println("Your team is full. Cannot add more PokÃ©mon.");
        }
    }

    public Pokemon choosePokemon(int index) {
        if (index >= 0 && index < team.size()) {
            return team.get(index);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    public void increaseScore(int amount) {
        this.score += amount;
    }

    public void showTeam() {
        if (team.isEmpty()) {
            System.out.println("You don't have any PokÃ©mon yet.");
        } else {
            System.out.println("Your PokÃ©mon:");
            for (int i = 0; i < team.size(); i++) {
                System.out.println("[" + i + "]\n" + team.get(i).toString() + "\n");
            }
        }
    }
    
    public Inventory getInventory() {  //added for inventory
        return inventory;
    }

    @Override
    public String toString() {
        return "Player Name: " + name + "\nScore: " + score + "\nTeam Size: " + team.size();
    }
}
