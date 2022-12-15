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

    //Heroes have a chance of blocking before taking damage
    public String damage(final int theDamage) {
        int result = block(theDamage);
        setMyHitPoints( getHealth() - result);
        return result == 0 ? getMyName() + " blocked all incoming damage!\n" : getMyName() + " took " + theDamage + " damage!\n";
    }

    //Heroes block
    @Override
    public int block(final int theDamage) {
        return Math.random() <= myBlockChance ? 0 : theDamage;
    }

    //return string so that we don't have to output
    public String trap(final int theDamage){
        setMyHitPoints(getHealth() - theDamage);
       return getMyName()+" was ensnared by a trap and took "+theDamage+" damage!";
    }

    //Heroes can heal
    public String heal(final int theMax, final int theMin) {
        Random r = new Random();
        int result = r.nextInt(theMax - theMin) + theMin;

        //If hit points are healed beyond maxHealth, reset back at maxHealth;
        setMyHitPoints(Math.min(getHealth() + result,getMaxHealth()));
        return getHealth() == getMaxHealth() ? getMyName() + " healed to Max Health!\n" : getMyName() + " healed for " + result + " HP!\n";
    }

    //Load block chance along with all the inherited traits from special character
    protected void loadHero(final ResultSet theRS) throws SQLException {
        super.loadSpecialCharacter(theRS);
        myBlockChance = theRS.getFloat("BLOCKCHANCE");
    }

    //Use this info to check if the hero has killed all four bosses
    public void addPillar(){
        myPillarCount++;
    }

    public int getPillarCount(){
        return myPillarCount;
    }

    public void clearPillarCount(){
        myPillarCount = 0;
    }

    public Inventory getInventory() {
        return INVENTORY;
    }

    //Important for setting cheats for hero
    public void setGod(){
        myBlockChance = 1;
        setMyDmg(500000,450000);
        setMyMaxHealth(500000);
        setMyUltChance(1);
        setMyHitChance(1);
    }

    public String toString() {
        return "CLASS:" + getMyName() + " HP:" + getHealth() + "/" + getMaxHealth() + " SPEED:" + getMyAttackSpeed() + " ACCURACY:" + getMyHitChance() + " PROTECTION:" + myBlockChance;
    }
}