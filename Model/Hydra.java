package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.Interfaces.Healable;

import java.sql.*;
import java.util.Random;

public class Hydra extends Guardian implements Healable {

    public Hydra() throws SQLException {
        loadHydra();
    }

    private void loadHydra() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM guardian_table WHERE NAME ='HYDRA' ");

        //Load guardian stats
        loadGuardian( rs);


        connection.close();
    }

    public String heal(final int theMax, final int theMin) {
        Random r = new Random();
        int result = r.nextInt(theMax - theMin) + theMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        setMyHitPoints(getHealth() + result < getMaxHealth() ? getHealth() + result : getMaxHealth());
        return getHealth() == getMaxHealth() ? getMyName() + " healed to Max Health!\n" : getMyName() + " healed for " + result + " HP!\n";
    }

    @Override
    public String ultimate(final Character theDefender) {
        return heal(80, 40);
    }
}