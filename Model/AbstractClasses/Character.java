package Model.AbstractClasses;

import java.util.Random;

public abstract class Character{
    protected String myName;
    //Hp: health, speed: attack turn, hitchance: possibility of attack landing, min/maxDMG: range of damage from successful attack
    protected int myHitPoints;
    protected int MAX_HEALTH;
    protected float myAttackSpeed;
    protected float myHitChance;
    protected int myMinDmg;
    protected int myMaxDmg;

    public String attack(final Character theDefender){
        //This random object will be used to perform a complex random operation with damage range.
        final Random r = new Random();

        //hit will be whether our not our hit lands, if a random value is within our chance range, hit = 1
        final int hit = Math.random()<= myHitChance ? 1:0;
        //result stores our random value between maxDmg and minDmg multiplied by the outcome of hit. If hit is 0 it is all 0.
        final int result = hit*(r.nextInt(myMaxDmg - myMinDmg) + myMinDmg);

        //If no damage is dealt, don't bother executing .damage()
        if(result != 0) return myName+" Attacks! "+(theDefender.damage(result));
        else return myName+" Missed...\n";
    }

    public abstract String damage(final int theDamage);

    public int getHealth(){
        return myHitPoints;
    }

    public int getMaxHealth(){
        return MAX_HEALTH;
    }

    public float getMyAttackSpeed() {
        return myAttackSpeed;
    }

    public float getMyHitChance() {
        return myHitChance;
    }

    public int getMyMinDmg() {
        return myMinDmg;
    }

    public int getMyMaxDmg() {
        return myMaxDmg;
    }
}