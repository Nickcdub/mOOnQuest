package Model.AbstractClasses;

import Model.Interfaces.Blockable;
import Model.Interfaces.Healable;
import Model.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

public abstract class Hero extends SpecialCharacter implements Blockable, Healable {
    protected final Inventory myInventory;
    protected float myBlockChance;

    protected Hero() {
        myInventory = new Inventory();
    }


    public String damage(final int theDamage) {
        int result = block(theDamage);
        myHitPoints = myHitPoints - result;
        return result == 0 ? myName + " blocked all incoming damage!\n" : myName + " took " + theDamage + " damage!\n";
    }

    @Override
    public int block(final int theDamage) {
        return Math.random() <= myBlockChance ? 0 : theDamage;
    }

    public Inventory getInventory() {
        return myInventory;
    }

    public void trap(final int theDamage){
        myHitPoints = myHitPoints - theDamage;
        System.out.println(myName+" was ensared by a trap and took "+theDamage+" damage!");
    }

    public String heal(final int myMax, final int myMin) {
        Random r = new Random();
        int result = r.nextInt(myMax - myMin) + myMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + result < MAX_HEALTH ? myHitPoints + result : MAX_HEALTH;
        return myHitPoints == MAX_HEALTH ? myName + " healed to Max Health!" : myName + " healed for " + result + " HP!\n";
    }

    protected void loadHero(final ResultSet rs) throws SQLException {
        myName = rs.getString("NAME");
        myHitPoints = MAX_HEALTH = rs.getInt("HP");
        myAttackSpeed = rs.getInt("SPEED");
        myHitChance = rs.getFloat("HITCHANCE");
        myBlockChance = rs.getFloat("BLOCKCHANCE");
        myMinDmg = rs.getInt("MINDMG");
        myMaxDmg = rs.getInt("MAXDMG");
        myUltChance = rs.getFloat("ULTCHANCE");
    }

    public String toString() {
        return "CLASS:" + myName + " HP:" + myHitPoints + "/" + MAX_HEALTH + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " PROTECTION:" + myBlockChance;
    }
}