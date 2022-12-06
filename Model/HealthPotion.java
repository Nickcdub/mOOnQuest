package Model;

import Model.AbstractClasses.Hero;
import Model.AbstractClasses.Item;

public class HealthPotion extends Item {
    public HealthPotion() {
        myPotionName = "Health Potion";
    }

    public void useEffect(Hero theHero) {
        System.out.print(theHero.heal(50,30));
    }
}