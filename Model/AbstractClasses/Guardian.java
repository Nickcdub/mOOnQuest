package Model.AbstractClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Guardian extends SpecialCharacter {
    public String pillar;

    @Override
    public String damage(int theDamage) {
        myHitPoints = myHitPoints - theDamage;
        return myName + " took " + theDamage + " damage.";
    }

    public void loadGuardian(Connection connection, ResultSet rs) throws SQLException {
        myName = rs.getString("NAME");
        myHitPoints = MAX_HEALTH = rs.getInt("HP");
        myAttackSpeed = rs.getInt("SPEED");
        myHitChance = rs.getFloat("HITCHANCE");
        ultChance = rs.getFloat("SKILLCHANCE");
        myMinDmg = rs.getInt("MINDMG");
        myMaxDmg = rs.getInt("MAXDMG");
    }

    public String toString() {
        return "GUARDIAN:" + myName + " HP:" + myHitPoints + "/" + MAX_HEALTH + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " ULTCHANCE:" + ultChance;
    }
}