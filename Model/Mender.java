package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;

public class Mender extends Hero {
    //public Type field; needs rework

    public Mender() throws SQLException {
        loadMender();
    }

    private void loadMender() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='MENDER' ");

        heroType = rs.getString("NAME");
        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        blockChance = rs.getFloat("BLOCKCHANCE");

        connection.close();
    }

    @Override
    public void ultimate(Character defender) {
        Heal(75,65);
    }

}