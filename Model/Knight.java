package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Knight extends Hero {

    private int minSpecial;
    private int maxSpecial;
    private List<String> inventory;

    public Knight() throws SQLException{

        loadKnight();

        inventory = new ArrayList<>();

    }

    private void loadKnight() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='KNIGHT' ");

        heroType = rs.getString("NAME");
        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        blockChance = rs.getFloat("BLOCKCHANCE");

        connection.close();
    }

    @Override
    public void ultimate(final Character defender) {
        final Random r = new Random();
        int result = r.nextInt(75-175) + 75;
        defender.damage(result);
    }


    @Override
    public void Block() {

    }
}