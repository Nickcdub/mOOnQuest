package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.Interfaces.Blockable;

import java.sql.*;
import java.util.Random;

public class Cerberus extends Guardian implements Blockable {
    private float myBlockChance;

    public Cerberus() throws SQLException {
        loadCerberus();
    }

    private void loadCerberus() throws SQLException {

        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM guardian_table WHERE NAME ='CERBERUS' ");

        loadGuardian( rs);

        //Borrow block chance from knight.
        ResultSet set = statement.executeQuery("SELECT * FROM hero_table WHERE NAME = 'KNIGHT'");
        myBlockChance = set.getFloat("BLOCKCHANCE");

        connection.close();
    }

    @Override
    public String ultimate(final Character theDefender) {
        //If our random value is not within our chance range, do nothing, the hit misses
        if (Math.random() < getMyUltChance()) return getMyName() + " Missed Multi-Bite...\n";
        final Random r = new Random();
        int result = (r.nextInt(65 - 50) + 50);
        return "Multi-Bite! " + (theDefender.damage(result));

    }

    public String damage(final int theDamage) {
        int result = block(theDamage);
        setMyHitPoints( getHealth() - result);
        return result == 0 ? getMyName() + " blocked all incoming damage!\n" : getMyName() + " took " + theDamage + " damage!\n";
    }

    @Override
    public int block(final int theDamage) {
        return Math.random() <= myBlockChance ? 0 : theDamage;
    }

    @Override
    public String toString() {
        return "GUARDIAN:" + getMyName() + " HP:" + getHealth() + "/" + getMaxHealth() + " SPEED:" + getMyAttackSpeed() + " ACCURACY:" + getMyHitChance() + " ULTCHANCE:" + getMyUltChance() + " BLOCKCHANCE: " + myBlockChance;
    }

}