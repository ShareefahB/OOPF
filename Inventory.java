import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> items;

    public Inventory() {
        items = new ArrayList<>();
        // Add default items
        for (int i = 0; i < 5; i++) {
            addItem("PokeBall");
        }
        addItem("Boxing Gloves");
        addItem("Boots");
    }

    // Add a single item to the inventory
    public void addItem(String itemName) {
        items.add(itemName);
    }

    // Use/remove one item from inventory
    public boolean useItem(String itemName) {
        if (items.remove(itemName)) {
            System.out.println("You used a " + itemName + ".");
            return true;
        } else {
            System.out.println("You don't have any " + itemName + " left.");
            return false;
        }
    }


    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            List<String> displayedItems = new ArrayList<>();
            for (String item : items) {
                if (!displayedItems.contains(item)) {
                    int itemQuantity = 0;
                    for (String innerItem : items) {
                        if (item.equals(innerItem)) {
                            itemQuantity++;
                        }
                    }
                    System.out.println("-" + item + " (x" + itemQuantity + ")");
                    displayedItems.add(item);
                    
                }
            }
        }
    }

    public List<String> getItems() {
        return this.items;
    }
}
