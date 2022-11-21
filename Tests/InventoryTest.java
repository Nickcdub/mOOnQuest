package Model;

import Model.AbstractClasses.Item;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryTest {
    @Test
    public void testInventory() {
        Inventory inv = new Inventory();
        Item item1 = new HealthPotion();
        Item item2 = new HealthPotion();
        Item item3 = new VisionPotion();
        Item item4 = new VisionPotion();

        inv.addItem(item1);
        assertEquals(1, inv.inventory.get("Health Potion"));

        inv.addItem(item2);
        assertEquals(2, inv.inventory.get("Health Potion"));

        inv.addItem(item3);
        assertEquals(1, inv.inventory.get("Vision Potion"));

        inv.addItem(item4);
        assertEquals(2, inv.inventory.get("Vision Potion"));

        inv.removeItem(item1);
        assertEquals(1, inv.inventory.get("Health Potion"));

        inv.removeItem(item2);
        assertEquals(0, inv.inventory.get("Health Potion"));

        inv.removeItem(item3);
        assertEquals(1, inv.inventory.get("Vision Potion"));

        inv.removeItem(item4);
        assertEquals(0, inv.inventory.get("Vision Potion"));
    }
}
