package Model.AbstractClasses;

import Model.Interfaces.Blockable;
import Model.Interfaces.Healable;
import Model.Inventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

public abstract class Hero extends SpecialCharacter implements Blockable, Healable {
    protected final Inventory myInventory;
    protected float myBlockChance;
    protected int myVision;

    protected Hero() {
        myInventory = new Inventory();
    }


    public String damage(int theDamage) {
        theDamage = block(theDamage);
        myHitPoints = myHitPoints - theDamage;
        return theDamage == 0 ? myName + " blocked all incoming damage!\n" : myName + " took " + theDamage + " damage!\n";
    }

    @Override
    public int block(int theDamage) {
        return Math.random() <= myBlockChance ? 0 : theDamage;
    }

    public String getMyInventory() {
        return myInventory.toString();
    }

    public String heal(int myMax, int myMin) {
        Random r = new Random();
        int result = r.nextInt(myMax - myMin) + myMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + result < MAX_HEALTH ? myHitPoints + result : MAX_HEALTH;
        return myHitPoints == MAX_HEALTH ? myName + " healed to Max Health!" : myName + " healed for " + result + " HP!\n";
    }

    protected void loadHero(Connection connection, ResultSet rs) throws SQLException {
        myName = rs.getString("NAME");
        myHitPoints = MAX_HEALTH = rs.getInt("HP");
        myAttackSpeed = rs.getInt("SPEED");
        myHitChance = rs.getFloat("HITCHANCE");
        myBlockChance = rs.getFloat("BLOCKCHANCE");
        myMinDmg = rs.getInt("MINDMG");
        myMaxDmg = rs.getInt("MAXDMG");
        ultChance = rs.getFloat("ULTCHANCE");
        myVision = 0;
    }

    public String toString() {
        return "CLASS:" + myName + " HP:" + myHitPoints + "/" + MAX_HEALTH + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " PROTECTION:" + myBlockChance;
    }

    public void setVision(int theVision) {
        this.myVision = theVision;
    }
}