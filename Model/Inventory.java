package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    //public List<String> inventory;
    public Map<String, Integer> inventory;
    public Inventory() {
        inventory = new HashMap<String, Integer>();
        inventory.put("Health Potion", 0);
        inventory.put("Vision Potion", 0);
    }

    @Override
    public String toString() {
        return "[ Health Potions: " + inventory.get("Health Potion") + ", Vision Potions: " + inventory.get("Vision Potion") + " ]";
    }

    public void addItem(String item) {
        inventory.put(item, inventory.get(item) + 1);
    }

    public void removeItem(String item) {
        if(inventory.get(item) > 0) {
            inventory.put(item, inventory.get(item) - 1);
        }
    }
}