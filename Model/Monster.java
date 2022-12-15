package Model;

import Model.AbstractClasses.Character;
import Model.CharacterConstants.MonsterType;
import Model.Interfaces.Healable;

import java.sql.*;

public class Monster extends Character implements Healable{
    private float myHealChance;
    private int myRegeneration;

    public Monster(final MonsterType theType) throws SQLException {

        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        switch (theType) {
            case OGRE -> loadStats(statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='OGRE' "));
            case GOBLIN -> loadStats(statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='GOBLIN' "));
            case DIREWOLF -> loadStats(statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='DIREWOLF' "));
        }
        connection.close();
    }

    //load heal chance and regeneration on top of basic character stuff
    private void loadStats(final ResultSet theRS) throws SQLException {
        loadCharacter(theRS);
        myHealChance = theRS.getFloat("HEALCHANCE");
        myRegeneration = theRS.getInt("REGENERATION");
    }

    //Monsters take damage straight out, no block here.
    @Override
    public String damage(final int theDamage) {
        setMyHitPoints( getHealth() - theDamage);
        return getMyName() + " took " + theDamage + " damage.\n";
    }

    //healing is a fixed amount for monster, they either heal or they don't
    @Override
    public String heal() {
        //Is this monster lucky enough to heal? if it is, it regenerates health, if not, recover = 0
        int recover = Math.random() <= myHealChance ? myRegeneration : 0;

        //If hit points are healed beyond maxHealth, reset back at maxHealth;
        setMyHitPoints(Math.min(getHealth() + recover, getMaxHealth()));
        return recover == 0 ? getMyName() + " did not regenerate health.\n" : getMyName() + " regenerated " + recover + " health!\n";
    }

    public String toString() {
        return "MONSTER:" + getMyName() + " HP:" + getHealth() + "/" + getMaxHealth() + " SPEED:" + getMyAttackSpeed() + " ACCURACY:" + getMyHitChance() + " HEALCHANCE:" + myHealChance;
    }
}