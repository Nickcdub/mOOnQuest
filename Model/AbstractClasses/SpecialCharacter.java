

package Model.AbstractClasses;

public abstract class SpecialCharacter extends Character {
    protected float myUltChance;

    protected SpecialCharacter() {
    }

    public abstract String ultimate(final Character theDefender);
}
