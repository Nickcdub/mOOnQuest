package Model.AbstractClasses;

public abstract class Item {
    public String myPotionName;
    public abstract void useEffect(Hero theHero);

    public boolean equals(Item theItem) {
        if(this.myPotionName.equals(theItem.myPotionName)) return true;
        return false;
    }
}
