package Model;

import Model.AbstractClasses.Character;
import Model.CharacterConstants.MonsterType;
import Model.Interfaces.Healable;

import java.sql.*;

public class Monster extends Character implements Healable {
    public MonsterType type;
    public float healChance;
    private String name;

    public Monster(MonsterType type) throws SQLException {

        name = type.name();
        switch(type){
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

        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        healChance = rs.getFloat("HEALCHANCE");

        connection.close();
    }

    private void loadGoblin() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='GOBLIN' ");

        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        healChance = rs.getFloat("HEALCHANCE");

        connection.close();
    }

    private void loadDirewolf() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM monster_table WHERE NAME ='DIREWOLF' ");

        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        healChance = rs.getFloat("HEALCHANCE");

        connection.close();
    }

    @Override
    public void damage(int damage) {
        hitPoints=hitPoints-damage;
    }

    public String toString(){
        return "MONSTER:"+name+" HP:"+hitPoints+"/"+MAX_HEALTH+" SPEED:"+attackSpeed+" ACCURACY:"+hitChance+" REGENERATION:"+healChance;
    }
}