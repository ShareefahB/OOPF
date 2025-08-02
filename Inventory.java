import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Your inventory:");
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.println((i + 1) + ". " + item.getName() + " - " + item.getDescription());
            }
        }
    }

    public void useItem(int index, Pokemon p) {
        if (index >= 0 && index < items.size()) {
            Item item = items.get(index);
            item.use(p);
            items.remove(index);
        } else {
            System.out.println("Invalid item index.");
        }
    }

    // Use item by name (used in Game.java)
    public boolean useItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(name)) {
                items.get(i).use(null); // If the item affects the player instead of a specific Pokémon
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    // Get a list of item names (used in Game.java)
    public List<String> getItems() {
        List<String> names = new ArrayList<>();
        for (Item item : items) {
            names.add(item.getName());
        }
        return names;
    }

    // Full access to Item objects (if needed elsewhere)
    public List<Item> getItemObjects() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void showInventory() {
        displayItems();
    }
}
