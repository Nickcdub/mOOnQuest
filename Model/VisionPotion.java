package Model;

import Model.AbstractClasses.Hero;
import Model.AbstractClasses.Item;

public class VisionPotion extends Item {
    public VisionPotion() {
        this.myPotionName = "Vision Potion";
    }
    @Override
    public void useEffect(Hero theHero) {
        //theHero.setVision(2);
    }
}
