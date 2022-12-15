package Model;

import Model.AbstractClasses.Item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> INVENTORY;

    public Inventory() {
        INVENTORY = new HashMap<>();
        INVENTORY.put("Health Potion", 0);
        INVENTORY.put("Vision Potion", 0);
    }

    public String addItem(final Item theItem) {
        INVENTORY.put(theItem.toString(), INVENTORY.get(theItem.toString()) + 1);
        return theItem+" added to Inventory!";
    }

    public void removeItem(final String theItem) {
        if(INVENTORY.get(theItem) > 0) {
            INVENTORY.put(theItem, INVENTORY.get(theItem) - 1);
        }
    }

    public int getSize(){
        int size = 0;
        if(INVENTORY.get("Health Potion")>0) size++;
        if(INVENTORY.get("Vision Potion")>0) size++;
        return size;
    }

    public int getItem(final String theItem){
        return INVENTORY.get(theItem);
    }

    @Override
    public String toString() {
        return "[ Health Potions: " + INVENTORY.get("Health Potion") + ", Vision Potions: " + INVENTORY.get("Vision Potion") + " ]";
    }
}