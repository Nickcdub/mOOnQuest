package Model.AbstractClasses;

public abstract class Guardian extends SpecialCharacter {
    public String pillar;
    public float ultChance;

    @Override
    public void damage(int damage) {
        hitPoints=hitPoints-damage;
    }
}