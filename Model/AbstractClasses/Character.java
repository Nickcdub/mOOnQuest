

package Model.AbstractClasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public abstract class Character{
   //All characters have these basic essentials
    private String myName;
    private int myHitPoints;
    private int myMaxHealth;
    private float myAttackSpeed;
    private float myHitChance;
    private int myMinDmg;
    private int myMaxDmg;

    //Instantiate fields and let children classes use getters and setters
    protected Character(){
        myName = null;
        myHitPoints = 0;
        myMaxHealth = 0;
        myAttackSpeed = 0;
        myHitChance = 0;
        myMinDmg = 0;
        myMaxDmg = 0;
    }
    //Character concrete classes will reach up the inheritance hierarchy in order to load data from the database
    protected void loadCharacter(ResultSet theRS) throws SQLException {
        myName = theRS.getString("NAME");
        myHitPoints = myMaxHealth = theRS.getInt("HP");
        myAttackSpeed = theRS.getInt("SPEED");
        myHitChance = theRS.getFloat("HITCHANCE");
        myMinDmg = theRS.getInt("MINDMG");
        myMaxDmg = theRS.getInt("MAXDMG");
    }


    //attack returns string so that it can more easily be printed when called.
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

    //We need these getters to perform operations in the controller
    public String getMyName(){
        return myName;
    }

    public int getHealth(){
        return myHitPoints;
    }

    public int getMaxHealth(){
        return myMaxHealth;
    }

    public float getMyAttackSpeed() {
        return myAttackSpeed;
    }

    protected float getMyHitChance(){
        return myHitChance;
    }

    public int getMyMinDmg() {
        return myMinDmg;
    }

    public int getMyMaxDmg() {
        return myMaxDmg;
    }


    //We need these setters for inheriting classes to reset character info
    protected void setMyDmg(final int theMaxDmg,final int theMinDmg){
        myMaxDmg = theMaxDmg;
        myMinDmg = theMinDmg;
    }

    protected void setMyHitPoints(final int theHitPoints) {
        this.myHitPoints = theHitPoints;
    }

    protected void setMyMaxHealth(final int theMax){
        myMaxHealth = theMax;
    }

    protected void setMyHitChance(final float theChance){
        myHitChance = theChance;
    }
}
