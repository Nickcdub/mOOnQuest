package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;

public class Assassin extends Hero {
    //public Type field; needs rework

    public Assassin() throws SQLException {
        loadRogue();
    }

    private void loadRogue() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='ASSASSIN' ");

        heroType = rs.getString("NAME");
        hitPoints= MAX_HEALTH = rs.getInt("HP");
        attackSpeed = rs.getInt("SPEED");
        hitChance = rs.getFloat("HITCHANCE");
        blockChance = rs.getFloat("BLOCKCHANCE");

        connection.close();
    }

    @Override
    public void ultimate(Character defender) {
        int ultChance = (int) Math.round(Math.random()*100);
        if(ultChance<20) {return;}
        else if(ultChance<60){
            attack(defender);
            return;
        }else if(ultChance<=100){
            attack(defender);
            attack(defender);
            return;
        }else{
            throw new NumberFormatException("ultChance must be less than 100, it is: "+ultChance);
        }
    }

    @Override
    public void Block() {

    }
}