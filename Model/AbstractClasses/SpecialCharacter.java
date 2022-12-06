

package Model.AbstractClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SpecialCharacter extends Character {
    private float myUltChance;

    protected SpecialCharacter() {
        myUltChance = 0;
    }

    protected void loadSpecialCharacter(ResultSet theRS) throws SQLException {
        super.loadCharacter(theRS);
        myUltChance = theRS.getFloat("ULTCHANCE");
    }

    public abstract String ultimate(final Character theDefender);

    protected float getMyUltChance() {
        return myUltChance;
    }
}