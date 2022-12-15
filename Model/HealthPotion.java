package Model;

import Model.AbstractClasses.Hero;
import Model.AbstractClasses.Item;

public class HealthPotion extends Item {
    public HealthPotion() {
        myPotionName = "Health Potion";
    }

    public String useEffect(Hero theHero) {
        //Heal the hero
        return theHero.heal(60,40);
    }
}