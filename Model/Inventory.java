package Model;

import Model.AbstractClasses.Item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> myInventory;

    public Inventory() {
        myInventory = new HashMap<>();
        myInventory.put("Health Potion", 0);
        myInventory.put("Vision Potion", 0);
    }

    @Override
    public String toString() {
        return "[ Health Potions: " + myInventory.get("Health Potion") + ", Vision Potions: " + myInventory.get("Vision Potion") + " ]";
    }

    public void addItem(final Item theItem) {
        myInventory.put(theItem.toString(), myInventory.get(theItem.toString()) + 1);
    }

    public void removeItem(final Item theItem) {
        if(myInventory.get(theItem.toString()) > 0) {
            myInventory.put(theItem.toString(), myInventory.get(theItem.toString()) - 1);
        }
    }
}