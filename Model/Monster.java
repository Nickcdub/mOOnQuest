package Model;

import Model.AbstractClasses.Character;
import Model.CharacterConstants.MonsterType;
import Model.Interfaces.Healable;

import java.sql.*;

public class Monster extends Character implements Healable {
    public float myHealChance;
    public int myRegeneration;

    public Monster(final MonsterType theType) throws SQLException {
        myName = theType.name();
        switch (theType) {
            case OGRE -> loadOgre();
            case GOBLIN -> loadGoblin();
            case DIREWOLF -> loadDirewolf();
        }
    }

    private void loadOgre() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='OGRE' ");

        loadStats(connection, rs);
    }

    private void loadGoblin() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='GOBLIN' ");

        loadStats(connection, rs);
    }

    private void loadDirewolf() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='DIREWOLF' ");

        loadStats(connection, rs);
    }

    private void loadStats(final Connection connection, final ResultSet rs) throws SQLException {
        myHitPoints = MAX_HEALTH = rs.getInt("HP");
        myAttackSpeed = rs.getInt("SPEED");
        myHitChance = rs.getFloat("HITCHANCE");
        myHealChance = rs.getFloat("HEALCHANCE");
        myMinDmg = rs.getInt("MINDMG");
        myMaxDmg = rs.getInt("MAXDMG");
        myRegeneration = rs.getInt("REGENERATION");

        connection.close();
    }

    @Override
    public String damage(final int theDamage) {
        myHitPoints = myHitPoints - theDamage;
        return myName + " took " + theDamage + " damage.\n";
    }

    @Override
    public String heal() {
        //Is this monster lucky enough to heal? if it is, it regenerates health, if not, recover = 0
        int recover = Math.random() <= myHealChance ? myRegeneration : 0;

        //If hitpoints are healed beyond maxHealth, reset back at maxHealth;
        myHitPoints = myHitPoints + recover < MAX_HEALTH ? myHitPoints + recover : MAX_HEALTH;
        return recover == 0 ? myName + " did not regenerate health." : myName + " regenerated " + recover + " health!\n";
    }

    public String toString() {
        return "MONSTER:" + myName + " HP:" + myHitPoints + "/" + MAX_HEALTH + " SPEED:" + myAttackSpeed + " ACCURACY:" + myHitChance + " HEALCHANCE:" + myHealChance;
    }
}