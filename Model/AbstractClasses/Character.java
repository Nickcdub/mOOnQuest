package Model.AbstractClasses;

import java.util.Random;

public abstract class Character{
    //Hp: health, speed: attack turn, hitchance: possibility of attack landing, min/maxDMG: range of damage from successful attack
    public int hitPoints;
    public int MAX_HEALTH;
    public float attackSpeed;
    public float hitChance;
    public int minDmg;
    public int maxDmg;
    public void attack(Character defender){
        //This random object will be used to perform a complex random operation with damage range.
        Random r = new Random();

        //hit will be whether our not our hit lands, if a random value is less than our chance, hit = 1
        int hit = Math.random()<=hitChance ? 1:0;
        //result stores our random value between maxDmg and minDmg multiplied by the outcome of hit. If hit is 0 it is all 0.
        int result = hit*(r.nextInt(maxDmg-minDmg) + minDmg);

        //If no damage is dealt, don't bother executing .damage()
        if(result != 0) defender.damage(result);
    }

    public abstract void damage(int damage);
}