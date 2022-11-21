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
        loadGuardian(connection, rs);


        connection.close();
    }

    @Override
    public String heal(int myMax, int myMin) {
        Random r = new Random();
        int result = r.nextInt(myMax - myMin) + myMin;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + result < MAX_HEALTH ? myHitPoints + result : MAX_HEALTH;
        return myHitPoints == MAX_HEALTH ? myName + " healed to Max Health!" : myName + " healed for " + result + " HP!\n";
    }

    @Override
    public String ultimate(Character theDefender) {
        return heal(100, 80);
    }
}