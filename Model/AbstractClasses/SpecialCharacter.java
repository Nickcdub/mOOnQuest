

package Model.AbstractClasses;

public abstract class SpecialCharacter extends Character {
    public float myUltChance;

    public SpecialCharacter() {
    }

    public abstract String ultimate(final Character theDefender);
}
