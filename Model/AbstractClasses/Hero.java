package Model.AbstractClasses;

import Model.AbstractClasses.SpecialCharacter;
import Model.Interfaces.Blockable;
import Model.Interfaces.Healable;
import Model.Inventory;

import java.util.List;
import java.util.Random;

public abstract class Hero extends SpecialCharacter implements Blockable, Healable {
    public List<String> inventory;
    public float blockChance;
    public String heroType;


    public int Block(int damage){
        return Math.random()<=blockChance ? 0:damage;
    }
    public void damage(int damage){
        damage=Block(damage);
        hitPoints=hitPoints-damage;
    }

    public String getInventory() {
        return inventory.toString();
    }

    public void Heal(int Max, int Min){
        Random r = new Random();
        int result = r.nextInt(Max-Min) + Min;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        hitPoints = hitPoints+result<MAX_HEALTH ? hitPoints+result:MAX_HEALTH;
    }

    public String toString(){
        return "CLASS:"+ heroType+" HP:"+hitPoints+"/"+MAX_HEALTH+" SPEED:"+attackSpeed+" ACCURACY:"+hitChance+" PROTECTION:"+blockChance;
    }
}