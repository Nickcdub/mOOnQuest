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
        myBlockChance = 0;
        myPillarCount = 0;
    }


    public String damage(final int theDamage) {
        int result = block(theDamage);
        setMyHitPoints( getHealth() - result);
        return result == 0 ? getMyName() + " blocked all incoming damage!\n" : getMyName() + " took " + theDamage + " damage!\n";
    }

    @Override
    public int block(final int theDamage) {
        return Math.random() <= myBlockChance ? 0 : theDamage;
    }

    public void trap(final int theDamage){
        setMyHitPoints(getHealth() - theDamage);
        System.out.println(getMyName()+" was ensnared by a trap and took "+theDamage+" damage!");
    }

    public String heal(final int theMax, final int theMin) {
        Random r = new Random();
        int result = r.nextInt(theMax - theMin) + theMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        setMyHitPoints(Math.min(getHealth() + result,getMaxHealth()));
        return getHealth() == getMaxHealth() ? getMyName() + " healed to Max Health!\n" : getMyName() + " healed for " + result + " HP!\n";
    }

    protected void loadHero(final ResultSet theRS) throws SQLException {
        super.loadSpecialCharacter(theRS);
        myBlockChance = theRS.getFloat("BLOCKCHANCE");
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
        return "CLASS:" + getMyName() + " HP:" + getHealth() + "/" + getMaxHealth() + " SPEED:" + getMyAttackSpeed() + " ACCURACY:" + getMyHitChance() + " PROTECTION:" + myBlockChance;
    }
}