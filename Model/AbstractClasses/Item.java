package Model.AbstractClasses;

public abstract class Item {
    public String myPotionName;
    public abstract void useEffect(Hero theHero);

    public String toString() {
        return myPotionName;
    }
}
