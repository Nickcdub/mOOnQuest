package Model;

import Model.AbstractClasses.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
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

    public void addItem(Item theItem) {
        inventory.put(theItem.toString(), inventory.get(theItem.toString()) + 1);
    }

    public void removeItem(Item theItem) {
        if(inventory.get(theItem.toString()) > 0) {
            inventory.put(theItem.toString(), inventory.get(theItem.toString()) - 1);
        }
    }
}