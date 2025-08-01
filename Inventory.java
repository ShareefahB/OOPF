import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> items;

    public Inventory() {
        items = new ArrayList<>();
    }
    //add item into inventory
    public void addItem(String itemName) {
        items.add(itemName);
        System.out.println(itemName + " added to inventory.");
    }

    //use/remove an item from inventory
    public boolean useItem(String itemName) {
        if (items.contains(itemName)) {
            items.remove(itemName);
            System.out.println("You used a " + itemName + ".");
            return true;
        } else {
            System.out.println("You don't have any " + itemName + " left.");
            return false;
        }
    }

    //display all items in inventory
    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (String item : items) {
                System.out.println("- " + item);
            }
        }
    }
}
