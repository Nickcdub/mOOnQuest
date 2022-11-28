package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;

import java.sql.*;

public class RedDragon extends Guardian {

    public RedDragon() throws SQLException {
        loadRedDragon();
    }

    private void loadRedDragon() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM guardian_table WHERE NAME ='RED_DRAGON' ");

        loadGuardian(connection, rs);

        connection.close();
    }

    @Override
    public String ultimate(final Character theDefender) {
        //If our random value is not within our chance range, do nothing, the hit misses
        if (Math.random() < myUltChance) return myName + " Missed Incinerate...\n";
        return "Incinerate! " + (theDefender.damage(70));
    }
}