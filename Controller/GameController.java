package Controller;

import Model.*;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;
import View.GameFrame;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Locale;
import java.util.Scanner;

import static Model.CharacterConstants.MonsterType.OGRE;

public class GameController {

    private Hero myHero;
    private Maze myMaze;
    private GameFrame frame;
    private boolean win;

    public GameController() throws SQLException {
        intro();
        battle(new Monster(OGRE));
        battle(new Cerberus());
    }

    void intro() throws SQLException {
        frame = new GameFrame();
        frame.showIntro();
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Welcome to mOOn Quest, please use the keyboard to type a single character input.\n" +
                "Would you like to start a new game(1) or load an old game file(2)?");
        input = sc.nextLine().toLowerCase(Locale.ROOT);
        switch (input) {
            case "1" -> characterSelect();
            case "2" -> input = null; //change this at some point.
            default -> intro();
        }
    }

    void characterSelect() throws SQLException {
        frame.showCharacter();
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.print("Please choose a character!\n" +
                "Knight(k) Mender(m) Assassin(a)\n" +
                "Character: ");
        input = sc.next().toLowerCase(Locale.ROOT);
        switch (input) {
            case "k" -> myHero = new Knight();
            case "m" -> myHero = new Mender();
            case "a" -> myHero = new Assassin();
            default -> characterSelect();
        }
        System.out.println(myHero + "\n");
        difficultySelect();
    }

    void difficultySelect() throws SQLException {
        frame.showDifficulty();
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.print("Please choose a difficulty!\n" +
                "Bubble Blowin Baby(1): 4 by 4 maze, nothing too fancy, couple goblins, couple pillars.\n" +
                "Amateur Explorer(2): Watched a lot of Dora as a kid, feel confident, 5 by 5 maze, goblins, wolves, and pillars.\n" +
                "Big Kids Table(3): You eat nails for breakfast... without any milk! 6 by 6 maze, goblins, wolves, ogres oh my! Oh and pillars too.\n" +
                "Difficulty: ");
        input = sc.next().toLowerCase(Locale.ROOT);
        switch (input) {
            case "1" -> myMaze = new Maze(4,1, myHero);
            case "2" -> myMaze = new Maze(5, 2,myHero);
            case "3" -> myMaze = new Maze(6, 3,myHero);
            default -> difficultySelect();
        }
        frame.showMap(myMaze);
        traverse();
    }

    void traverse() throws SQLException {
        int pillarCount = 0;
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Use W,A,S,D to traverse NORTH, EAST, SOUTH, WEST respectively");
        while(pillarCount!=4) {
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
            if(myMaze.getEnemy()!=null) battle(myMaze.getEnemy());
            else if(myMaze.getBoss()!=null){
                battle(myMaze.getBoss());
                pillarCount++;
            }
            frame.showMap(myMaze);
        }
        win();
    }

    //Character can not pass data using its getters, so we need to overload battle for Monsters and Guardians.
    void battle(final Monster theDefender) throws SQLException {
        System.out.println("A " + theDefender.getMyName() + " approaches!");
        System.out.println("Use 1 to trigger attack, 2 to trigger skill!");
        Scanner sc = new Scanner(System.in);
        String input;
        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / (2 * theDefender.getMyAttackSpeed()));
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName() + "'s Turn!");
                input = sc.next();
                switch (input) {
                    case "1" -> System.out.print(myHero.attack(theDefender));
                    case "2" -> System.out.print(myHero.ultimate(theDefender));
                    case "0" -> {
                        System.out.println("Combat Skip!\n");
                        theDefender.damage(500);
                    }
                    default ->  {
                        System.out.println(" Invalid Input! Use 1 to trigger attack, 2 to trigger skill!");
                        i--;
                    }
                }
            }

            //Enemy's turn, if the enemy is still alive, let the monster heal and have it attack.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName() + "'s Turn!");
                System.out.print(theDefender.heal());
                System.out.print(theDefender.attack(myHero));
            }
        }

        //One of our combatants has suffered their untimely demise :'( tell the user! Give health bonus to hero for overboard damage.
        if (theDefender.getHealth() < 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            System.out.println("Health Boost for overboard damage: "+ myHero.heal((-theDefender.getHealth()), 0));
        } else if (myHero.getHealth() < 0) {
            System.out.println("You fell unconscious, you were defeated by " + theDefender.getMyName() + "!\n" +
                    "try again?(1) or give up(2)");
            input = sc.next();
            switch (input) {
                case "1" -> intro();
                case "2" -> exit();
            }
        }
    }

    void battle(final Guardian theDefender) throws SQLException {
        System.out.println("A " + theDefender.getMyName() + " approaches!");
        System.out.println("Use 1 to trigger attack, 2 to trigger skill!");
        Scanner sc = new Scanner(System.in);
        String input;
        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / (2 * theDefender.getMyAttackSpeed()));
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        int turnCounter = 0;
        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            //1 is normal attack, 2 is ultimate, 0 is a cheat to skip (possibility of cerberus blocking)
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName() + "'s Turn!");
                input = sc.next();
                switch (input) {
                    case "1" -> System.out.print(myHero.attack(theDefender));
                    case "2" -> System.out.print(myHero.ultimate(theDefender));
                    case "0" -> {
                        System.out.println("Combat Skip!\n");
                        theDefender.damage(500);
                    }
                    default ->  {
                        System.out.println(" Invalid Input! Use 1 to trigger attack, 2 to trigger skill!");
                        i--;
                    }
                }
            }

            //Enemy's turn, if the enemy is still alive, let the Guardian try to ultimate every 3rd turn.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName() + "'s Turn!");
                String result = turnCounter % 3 == 0 && turnCounter!=0 ? theDefender.ultimate(myHero) : theDefender.attack(myHero);
                turnCounter++;
                System.out.print(result);
            }
        }
        //One of our combatants has suffered their untimely demise :'( tell the user!
        if (theDefender.getHealth() < 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            System.out.print(myHero.heal((-theDefender.getHealth()), 0));
        } else if (myHero.getHealth() < 0) {
            System.out.println("You fell unconscious, you were defeated by " + theDefender.getMyName() + "!\n" +
                    "try again?(1) or give up(2)");
            input = sc.next();
            switch (input) {
                case "1" -> intro();
                case "2" -> exit();
            }
        }
    }

    private void win() throws SQLException {
        System.out.println("\nAll four pillar retrieved! Thank you for playing!");
        System.out.println("Play Again?");
        System.out.print("y/n: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch(input){
            case "y" -> intro();
            case "n" -> exit();
        }
    }

    private void exit() {
        System.exit(0);
    }

}
