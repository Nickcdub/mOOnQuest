

package Model.AbstractClasses;

import java.util.Random;

public abstract class Character{
    protected String myName;
    //Hp: health, speed: attack turn, hit chance: possibility of attack landing, min/maxDMG: range of damage from successful attack
    protected int myHitPoints;
    protected int maxHealth;
    protected float myAttackSpeed;
    protected float myHitChance;
    protected int myMinDmg;
    protected int myMaxDmg;

    protected Character(){

    }
    //This returns string so that it can more easily be printed when called.
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

    public String getMyName(){
        return myName;
    }

    public int getHealth(){
        return myHitPoints;
    }

    public float getMyAttackSpeed() {
        return myAttackSpeed;
    }

}
