package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.Random;

public class Assassin extends Hero {

    //load Assassin stats
    public Assassin() throws SQLException {
        loadAssassin();
    }

    private void loadAssassin() throws SQLException {
        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        //Establish connection to driver url
        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='ASSASSIN' ");

        //Load hero stats from the database
       loadHero(rs);

       //close connection
        connection.close();
    }

    @Override
    public String ultimate(final Character theDefender) {
       int hitCount = 0;
       String result;

       if(Math.random()< getMyUltChance()) hitCount++;
        if(Math.random()< getMyUltChance()) hitCount++;

        Random r = new Random();


        switch(hitCount){
            case 0 -> result = getMyName()+" Missed Rhythm Echo...\n"; //failed
            case 1 ->  result = "Partial Success:Single Hit!\n"+theDefender.damage(r.nextInt(getMyMaxDmg() - getMyMinDmg()) + getMyMinDmg());//partial success
            case 2 ->  result = "Complete Success: Double Hit!\n"+theDefender.damage(r.nextInt(getMyMaxDmg() - getMyMinDmg()) + getMyMinDmg())+theDefender.damage(r.nextInt(getMyMaxDmg() - getMyMinDmg()) + getMyMinDmg());//complete success
            default -> throw new NumberFormatException("HitCount should be >=2, hitCount: "+hitCount);
        }
        return result;
    }
}