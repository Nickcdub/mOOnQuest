package Model.AbstractClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Guardian extends SpecialCharacter {
    private String myPillar;

    //Damage taken by guardians goes right through
    @Override
    public String damage(final int theDamage) {
        setMyHitPoints( getHealth() - theDamage);
        return getMyName() + " took " + theDamage + " damage.\n";
    }

    //Load pillar info and basic special character stuff
    protected void loadGuardian(ResultSet theRS) throws SQLException {
        loadSpecialCharacter(theRS);
        myPillar = theRS.getString("PILLAR");
    }

    public String toString() {
        return "GUARDIAN:" + getMyName() + " HP:" + getHealth() + "/" + getMaxHealth() + " SPEED:" + getMyAttackSpeed() + " ACCURACY:" + getMyHitChance() + " ULTCHANCE:" + getMyUltChance();
    }

    public String getPillar() {
        return myPillar;
    }
}