package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.Random;

public class Mender extends Hero {

    //Load mender stats
    public Mender() throws SQLException {
        loadMender();
    }

    //Load hero stats for methods
    private void loadMender() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='MENDER' ");

        loadHero(connection, rs);

        connection.close();
    }

    //Ultimate heals mender between 65 and 75 health
    @Override
    public String ultimate(Character theDefender) {
        if (Math.random() > ultChance) return myName + " Missed Heal...";
        return heal(75, 65);
    }

}