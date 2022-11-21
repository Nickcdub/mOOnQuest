package Model;

import Model.AbstractClasses.Hero;
import Model.AbstractClasses.Item;

public class HealthPotion extends Item {
    public HealthPotion() {
        this.myPotionName = "Health Potion";
    }

    @Override
    public void useEffect(Hero theHero) {
        theHero.heal(50,30);
    }
}