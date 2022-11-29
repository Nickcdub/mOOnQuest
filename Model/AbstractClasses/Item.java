package Model.AbstractClasses;

public abstract class Item {
    protected String myPotionName;

    public Item(){

    }
    public abstract void useEffect(final Hero theHero);

    public String toString() {
        return myPotionName;
    }
}
