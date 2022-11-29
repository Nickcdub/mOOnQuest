package Model.AbstractClasses;


import Model.Interfaces.Blockable;
import Model.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

public abstract class Hero extends SpecialCharacter implements Blockable {
    private final Inventory INVENTORY;
    private float myBlockChance;
    private int myPillarCount;

    protected Hero() {
        INVENTORY = new Inventory();
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

    public void trap(final int theDamage){
        myHitPoints = myHitPoints - theDamage;
        System.out.println(myName+" was ensared by a trap and took "+theDamage+" damage!");
    }

    public String heal(final int theMax, final int theMin) {
        Random r = new Random();
        int result = r.nextInt(theMax - theMin) + theMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + result < maxHealth ? myHitPoints + result : maxHealth;
        return myHitPoints == maxHealth ? myName + " healed to Max Health!\n" : myName + " healed for " + result + " HP!\n";
    }

    protected void loadHero(final ResultSet theRS) throws SQLException {
        myName = theRS.getString("NAME");
        myHitPoints = maxHealth = theRS.getInt("HP");
        myAttackSpeed = theRS.getInt("SPEED");
        myHitChance = theRS.getFloat("HITCHANCE");
        myBlockChance = theRS.getFloat("BLOCKCHANCE");
        myMinDmg = theRS.getInt("MINDMG");
        myMaxDmg = theRS.getInt("MAXDMG");
        myUltChance = theRS.getFloat("ULTCHANCE");
    }

    public void addPillar(){
        myPillarCount++;
    }

    public Inventory getInventory() {
        return INVENTORY;
    }
    public int getPillarCount(){
        return myPillarCount;
    }

    public String toString() {
        return "CLASS:" + myName + " HP:" + myHitPoints + "/" + maxHealth + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " PROTECTION:" + myBlockChance;
    }
}