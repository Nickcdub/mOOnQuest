package Model.AbstractClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Guardian extends SpecialCharacter {
    protected String myPillar;

    @Override
    public String damage(final int theDamage) {
        myHitPoints = myHitPoints - theDamage;
        return myName + " took " + theDamage + " damage.";
    }

    protected void loadGuardian(ResultSet theRS) throws SQLException {
        myName = theRS.getString("NAME");
        myHitPoints = maxHealth = theRS.getInt("HP");
        myAttackSpeed = theRS.getInt("SPEED");
        myHitChance = theRS.getFloat("HITCHANCE");
        myUltChance = theRS.getFloat("SKILLCHANCE");
        myMinDmg = theRS.getInt("MINDMG");
        myMaxDmg = theRS.getInt("MAXDMG");
    }

    public String toString() {
        return "GUARDIAN:" + myName + " HP:" + myHitPoints + "/" + maxHealth + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " ULTCHANCE:" + myUltChance;
    }

    public String getPillar() {
        return myPillar;
    }
}