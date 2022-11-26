package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.Random;

public class Knight extends Hero {

    //Load knight data from database
    public Knight() throws SQLException {
        loadKnight();
    }

    //load hero stats.
    private void loadKnight() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='KNIGHT' ");

        loadHero(rs);

        connection.close();
    }

    //Ultimate has less likely chance to hit with over 2x the power of a normal attack.
    @Override
    public String ultimate(final Character theDefender) {
        //If our random value is not within our chance range (40%), do nothing, the hit misses
        if (Math.random() > myUltChance) return myName + " Missed Fatal Slash...";
        final Random r = new Random();
        int result = (r.nextInt(175 - 75) + 75);
        return "Fatal Slash! " + (theDefender.damage(result));
    }


}