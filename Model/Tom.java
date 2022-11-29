package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;

import java.sql.*;
import java.util.Random;

public class Tom extends Guardian {

    //Just load toms stats
    public Tom() throws SQLException {
        loadTom();
    }

    //Load Tom's stats by passing the connection and result set to load guardian
    private void loadTom() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM guardian_table WHERE NAME ='TOM' ");

        loadGuardian(rs);

        connection.close();
        myPillar = "Pillar of Abstraction";
    }

    private String heal(int theMax, int theMin) {
        Random r = new Random();
        int result = r.nextInt(theMax - theMin) + theMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + result < maxHealth ? myHitPoints + result : maxHealth;
        return myHitPoints == maxHealth ? myName + " healed to Max Health!" : myName + " healed for " + result + " HP!\n";
    }

    @Override
    public String ultimate(final Character theDefender) {

        //If our random value is not within our chance range, do nothing, the hit misses
        if (Math.random() < myUltChance) return myName + " Missed mOOn Lecture...\n";
        final Random hit = new Random();

        int damage = (hit.nextInt(myMaxDmg - myMinDmg) + myMinDmg);

        return "Y'AIN'T GONNA NEED IT! " + (theDefender.damage(damage)) + (heal(50, 30));
    }
}