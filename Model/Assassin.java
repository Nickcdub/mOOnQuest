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

        //Establish conection to driver url
        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM hero_table WHERE NAME ='ASSASSIN' ");

        //Load hero stats from the database
       loadHero(connection,rs);

       //close connection
        connection.close();
    }

    @Override
    public String ultimate(Character theDefender) {
       int hitcount = 0;
       String result = null;

       if(Math.random()> ultChance) hitcount++;
        if(Math.random()> ultChance) hitcount++;

        Random r = new Random();


        switch(hitcount){
            case 0 -> result = myName+" Missed Rhythm Echo...\n"; //failed
            case 1 ->  result = "Partial Success:Single Hit!\n"+theDefender.damage(r.nextInt(myMaxDmg - myMinDmg) + myMinDmg);//partial success
            case 2 ->  result = "Complete Success: Double Hit!\n"+theDefender.damage(r.nextInt(myMaxDmg - myMinDmg) + myMinDmg)+theDefender.damage(r.nextInt(myMaxDmg - myMinDmg) + myMinDmg);//complete success
        }

        if (result==null) throw new NumberFormatException("HitCount should be >=2, hitcount: "+hitcount);
        else return result;
    }
}