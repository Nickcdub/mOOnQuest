package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;

import java.sql.*;
import java.util.Random;

public class Moon_Demon extends Guardian {

    //Just load toms stats
    public Moon_Demon() throws SQLException {
        loadMoon_Demon();
    }

    //Load Tom's stats by passing the connection and result set to load guardian
    private void loadMoon_Demon() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM guardian_table WHERE NAME ='MOON_DEMON' ");

        loadGuardian(rs);

        connection.close();
    }

    private String heal() {
        Random r = new Random();
        int result = r.nextInt(50 - 30) + 30;

        //If hit points are healed beyond maxHealth, reset back at maxHealth;
        setMyHitPoints(Math.min(getHealth() + result, getMaxHealth()));
        return getHealth() == getMaxHealth() ? getMyName() + " healed to Max Health!\n" : getMyName() + " healed for " + result + " HP!\n";
    }

    @Override
    public String ultimate(final Character theDefender) {

        //If our random value is not within our chance range, do nothing, the hit misses
        if (Math.random() < getMyUltChance()) return getMyName() + " Missed Feature Lecture...\n";
        final Random hit = new Random();

        int damage = (hit.nextInt(getMyMaxDmg() - getMyMinDmg()) + getMyMinDmg());

        return "Y'AIN'T GONNA NEED IT! " + (theDefender.damage(damage)) + (heal());
    }
}