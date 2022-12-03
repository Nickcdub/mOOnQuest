package Controller;

import Model.*;
import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;
import View.GameFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

public class GameController {

    private static Hero myHero;
    private static Maze myMaze;
    private static GameFrame myFrame;


    public GameController() throws SQLException, IOException, InterruptedException {
        intro();
    }

    private static void intro() throws SQLException, IOException, InterruptedException {
        myFrame = new GameFrame(800,800);
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Welcome to mOOn Quest, please use the keyboard to type a single character input.\n" +
                "Would you like to start a new game(1) or load an old game file(2)?");
        input = sc.nextLine().toLowerCase(Locale.ROOT);
        switch (input) {
            case "1" -> characterSelect(sc);
            case "2" -> input = null; //change this at some point.
            default -> intro();
        }
    }

    private static void characterSelect(Scanner sc) throws SQLException, IOException, InterruptedException {
        System.out.print("""
                Please choose a character!
                Knight(k) Mender(m) Assassin(a)
                Character:\s""");
        //Launch Character select panel
        myFrame.characterSelectPanel();
        do{
            Thread.sleep(200);
        }while(myHero == null);
        System.out.println(myHero + "\n");
        difficultySelect(sc);
    }

    private static void difficultySelect(Scanner sc) throws SQLException, IOException, InterruptedException {
        String input;
        System.out.print("""
                Please choose a difficulty!
                Bubble Blowin Baby(1): 4 by 4 maze, nothing too fancy, couple goblins, couple pillars.
                Amateur Explorer(2): Watched a lot of Dora as a kid, feel confident, 5 by 5 maze, goblins, wolves, and pillars.
                Big Kids Table(3): You eat nails for breakfast... without any milk! 7 by 7 maze, goblins, wolves, ogres oh my! Oh and pillars too.
                Difficulty:\s""");
        input = sc.next().toLowerCase(Locale.ROOT);
        switch (input) {
            case "1" -> myMaze = new Maze(4,1, myHero);
            case "2" -> myMaze = new Maze(5, 2,myHero);
            case "3" -> myMaze = new Maze(6, 3,myHero);
            default -> difficultySelect(sc);
        }
        myFrame.showMap(myMaze,myHero);
        traverse(sc);
    }

    private static void traverse(Scanner sc) throws SQLException, IOException, InterruptedException {
        String input;
        System.out.println("Use W,A,S,D to traverse NORTH, EAST, SOUTH, WEST respectively");

        StringBuilder travelLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(travelLog));
        System.setOut(ps);

        while(myHero.getPillarCount()!=4) {
            System.out.println("Possible Moves: "+myMaze.getPath());
            input = sc.next().toLowerCase(Locale.ROOT);
            switch (input) {
                case "w" -> {
                    if(myMaze.getPath().contains("NORTH")) myMaze.move("NORTH");

                }
                case "a" -> {
                    if(myMaze.getPath().contains("WEST")) myMaze.move("WEST");
                }
                case "s" -> {
                    if(myMaze.getPath().contains("SOUTH")) myMaze.move("SOUTH");
                }
                case "d" -> {
                    if(myMaze.getPath().contains("EAST")) myMaze.move("EAST");
                }
            }
            if(myHero.getHealth()<=0) death("You hit a trap and lacked the energy to escape, you fell unconscious...",sc);
            if(myMaze.getEnemy()!=null) battle(myMaze.popEnemy(),sc);
            else if(myMaze.getBoss()!=null){
                battle(myMaze.popBoss(),sc);
                myHero.addPillar();
            }
            myFrame.showMap(myMaze,myHero);

        }
        win();
    }

    //Character can not pass data using its getters, so we need to overload battle for Monsters and Guardians.
    private static void battle(final Monster theDefender, Scanner sc) throws SQLException, IOException, InterruptedException {
        BattleInput.resetAttack();
        StringBuilder atkLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(atkLog));
        System.setOut(ps);

        System.out.println("A "+theDefender.getMyName()+ " approaches!");

        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / (2 * theDefender.getMyAttackSpeed()));
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName()+"'s Health: "+myHero.getHealth()+"/"+myHero.getMaxHealth());
                System.out.println(myHero.getMyName() + "'s Turn!");
                myFrame.battlePanel(myHero,theDefender,atkLog);

                do{
                    Thread.sleep(200);
                }while(BattleInput.noAttack());
                BattleInput.resetAttack();
                myFrame.battlePanel(myHero,theDefender,atkLog);
            }

            //Enemy's turn, if the enemy is still alive, let the monster heal and have it attack.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName()+"'s Health: "+theDefender.getHealth()+"/"+theDefender.getMaxHealth());
                System.out.println(theDefender.getMyName() + "'s Turn!");
                System.out.print(theDefender.heal());
                System.out.print(theDefender.attack(myHero));
            }
        }

        //One of our combatants has suffered their untimely demise :'( tell the user! Give health bonus to hero for overboard damage.
        if (theDefender.getHealth() <= 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            System.out.println("Health Boost for overboard damage: "+ myHero.heal((-theDefender.getHealth()), 0));
        } else if (myHero.getHealth() <= 0) {
            death("You fell unconscious, you were defeated by " + theDefender.getMyName() + "!", sc);
        }
    }

    private static void battle(final Guardian theDefender, Scanner sc) throws SQLException, IOException, InterruptedException {
        //Tell
        BattleInput.resetAttack();
        //We want to set output to atkLog so that our atkLog sustains between panel changes
        StringBuilder atkLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(atkLog));
        System.setOut(ps);

        System.out.println("A "+theDefender.getMyName()+ " approaches!");

        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / (2 * theDefender.getMyAttackSpeed()));
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        int turnCounter = 0;
        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            //1 is normal attack, 2 is ultimate, 0 is a cheat to skip (possibility of cerberus blocking)
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName()+"'s Health: "+myHero.getHealth()+"/"+myHero.getMaxHealth());
                System.out.println(myHero.getMyName() + "'s Turn!");
                myFrame.battlePanel(myHero,theDefender,atkLog);

                do{
                    Thread.sleep(200);
                }while(BattleInput.noAttack());
                BattleInput.resetAttack();
                myFrame.battlePanel(myHero,theDefender,atkLog);
            }

            //Enemy's turn, if the enemy is still alive, let the Guardian try to ultimate every 3rd turn.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName()+"'s Health: "+theDefender.getHealth()+"/"+theDefender.getMaxHealth());
                System.out.println(theDefender.getMyName() + "'s Turn!");
                String result = turnCounter % 3 == 0 && turnCounter!=0 ? theDefender.ultimate(myHero) : theDefender.attack(myHero);
                turnCounter++;
                System.out.print(result);
            }
        }
        //One of our combatants has suffered their untimely demise :'( tell the user!
        if (theDefender.getHealth() <= 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            System.out.print(myHero.heal((-theDefender.getHealth()), 0));
            System.out.println("You received The "+theDefender.getPillar()+"!!!");
        } else if (myHero.getHealth() <= 0) {
           death("You fell unconscious, you were defeated by " + theDefender.getMyName() + "!", sc);
        }
    }
    private static void death(String message, Scanner sc) throws SQLException, IOException, InterruptedException {
        System.out.println(message+"\n"+"try again?(1) or give up(2)");
        String input = sc.next();
        switch (input) {
            case "1" -> intro();
            case "2" -> exit();
        }
    }

    private static void win() throws SQLException, IOException, InterruptedException {
        System.out.println("\nAll four pillars retrieved! Thank you for playing!");
        System.out.println("Play Again?");
        System.out.print("y/n: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch(input){
            case "y" -> intro();
            case "n" -> exit();
            default -> win();
        }
    }

    private static void exit() {
        System.exit(0);
    }

    public static class HeroInput implements ActionListener {

        public void actionPerformed(ActionEvent e){

            String name = e.getActionCommand();
            switch (name) {
                case "KNIGHT" -> {
                    try {
                        myHero = new Knight();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "MENDER" -> {
                    try {
                        myHero = new Mender();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "ASSASSIN" -> {
                    try {
                        myHero = new Assassin();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static class BattleInput implements ActionListener{
        private static Character myDefender;
        private static boolean myAttack;

        public BattleInput(Character theDefender){
            myDefender = theDefender;
        }

        public void actionPerformed(ActionEvent e){
            String name = e.getActionCommand();

            switch(name) {
                case "Attack" -> {
                    System.out.print(myHero.attack(myDefender));
                    myAttack = true;
                }
                case "Special" -> {
                    System.out.println(myHero.ultimate(myDefender));
                    myAttack = true;
                }
                case "Cheats" -> {
                    System.out.println("Combat Skip!\n");
                    myDefender.damage(500);
                    myAttack = true;
                }
            }

        }

        private static void resetAttack(){
            myAttack = false;
        }

        private static boolean noAttack(){
            return !myAttack;
        }
    }


}

