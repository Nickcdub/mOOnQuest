package Controller;

import Model.*;
import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;
import View.GameFrame;
import View.InventoryPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

public class GameController {

    private static Hero myHero;
    private static Maze myMaze;
    private static GameFrame myFrame;


    public GameController() throws SQLException, IOException, InterruptedException {
        intro();
    }

    private static void intro() throws SQLException, IOException, InterruptedException {
        myHero = null;
        myMaze = null;
        //Game can be replayed after death or win, don't open a new frame if that's the case
        if (myFrame == null) {
            myFrame = new GameFrame(1000, 1000);
            myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        myFrame.introPanel();
        do {
            Thread.sleep(200);
        } while (IntroInput.input == 0);
        if (IntroInput.input == 1) {
            IntroInput.input = 0;
            characterSelect();
        } else {
            IntroInput.input = 0;
            intro();
        }
    }

    private static void characterSelect() throws SQLException, IOException, InterruptedException {

        //Launch Character select panel
        myFrame.characterSelectPanel();
        do {
            Thread.sleep(200);
        } while (myHero == null);
        System.out.println(myHero + "\n");
        difficultySelect();
    }

    private static void difficultySelect() throws SQLException, IOException, InterruptedException {

        //Launch Difficulty Select Panel
        myFrame.difficultyPanel();
        do {
            Thread.sleep(200);
        } while (myMaze == null);
        traverse();
    }

    private static void traverse() throws SQLException, IOException, InterruptedException {

        StringBuilder travelLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(travelLog));
        System.setOut(ps);

        myFrame.showMap(myMaze, myHero, travelLog);
        while (myHero.getPillarCount() != 4) {
            System.setOut(ps);
            do {
                Thread.sleep(200);
            } while (MoveInput.myMove == null);
            switch (MoveInput.myMove) {
                case "North" -> myMaze.move("NORTH");
                case "West" -> myMaze.move("WEST");
                case "East" -> myMaze.move("EAST");
                case "South" -> myMaze.move("SOUTH");
                case "Inventory" -> {
                    myFrame.inventoryPanel(myHero);
                    do {
                            Thread.sleep(200);
                    } while (!InventoryInput.input);
                    InventoryInput.input = false;
                }
            }
            MoveInput.myMove = null;
            myFrame.showMap(myMaze, myHero, travelLog);

            if (myHero.getHealth() <= 0)
                death("You hit a trap and lacked the energy to escape, you fell unconscious...");
            if (myMaze.getEnemy() != null) {
                System.out.println("Defeated a " + myMaze.getEnemy().getMyName());
                battle(myMaze.popEnemy());
            } else if (myMaze.getBoss() != null) {
                System.out.println("Vanquished the " + myMaze.getBoss().getMyName());
                battle(myMaze.popBoss());
                myHero.addPillar();
            }
            myFrame.showMap(myMaze, myHero, travelLog);

        }
        win();
    }

    //Character can not pass data using its getters, so we need to overload battle for Monsters and Guardians.
    private static void battle(final Monster theDefender) throws SQLException, IOException, InterruptedException {
        BattleInput.resetAttack();
        StringBuilder atkLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(atkLog));
        System.setOut(ps);

        System.out.println("A " + theDefender.getMyName() + " approaches!");

        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / theDefender.getMyAttackSpeed());
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName() + "'s Health: " + myHero.getHealth() + "/" + myHero.getMaxHealth());
                System.out.println(myHero.getMyName() + "'s Turn!");
                if (theDefender.getHealth() <= 0) System.out.println("Keep attacking for a chance at gaining health!");
                myFrame.battlePanel(myHero, theDefender, atkLog);

                do {
                    Thread.sleep(200);
                } while (!BattleInput.myAttack && !BattleInput.myInventory);
                BattleInput.myAttack = false;
                //An attack was not made, inventory is now open, roll back heroTurns counter.
                if(BattleInput.myInventory){
                    i--;
                    BattleInput.myInventory = false;
                    myFrame.inventoryPanel(myHero);
                    do {
                        Thread.sleep(200);
                    } while (!InventoryInput.input);
                    InventoryInput.input = false;
                }
                myFrame.battlePanel(myHero, theDefender, atkLog);
            }

            //Enemy's turn, if the enemy is still alive, let the monster heal and have it attack.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName() + "'s Health: " + theDefender.getHealth() + "/" + theDefender.getMaxHealth());
                System.out.println(theDefender.getMyName() + "'s Turn!");
                System.out.print(theDefender.heal());
                System.out.print(theDefender.attack(myHero));
            }
        }

        //One of our combatants has suffered their untimely demise :'( tell the user! Give health bonus to hero for overboard damage.
        if (theDefender.getHealth() <= 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            //Make sure defender health is less than 0 or we'll get an error
            if (theDefender.getHealth() < 0)
                System.out.println("Health Boost for overboard damage: " + myHero.heal((-theDefender.getHealth()), 0));
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(3000);
        } else if (myHero.getHealth() <= 0) {
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(3000);
            death("You were swallowed by the Forest.., you succumb to the injuries caused by the " + theDefender.getMyName() + "!");
        }
    }

    private static void battle(final Guardian theDefender) throws SQLException, IOException, InterruptedException {
        //Tell
        BattleInput.resetAttack();
        //We want to set output to atkLog so that our atkLog sustains between panel changes
        StringBuilder atkLog = new StringBuilder();
        PrintStream ps = new PrintStream(new BuilderOutputStream(atkLog));
        System.setOut(ps);

        System.out.println("A " + theDefender.getMyName() + " approaches!");

        //First find turn order: enemy cannot have more turns than hero, if hero.speed >= 2*defender.speed, hero gets two turns
        int heroTurns = (int) Math.floor(myHero.getMyAttackSpeed() / theDefender.getMyAttackSpeed());
        heroTurns = heroTurns == 0 ? 1 : heroTurns;

        int turnCounter = 0;
        //This is where battling happens.
        while (myHero.getHealth() > 0 && theDefender.getHealth() > 0) {
            //Hero's turn
            //1 is normal attack, 2 is ultimate, 0 is a cheat to skip (possibility of cerberus blocking)
            for (int i = 0; i < heroTurns; i++) {
                System.out.println(myHero.getMyName() + "'s Health: " + myHero.getHealth() + "/" + myHero.getMaxHealth());
                System.out.println(myHero.getMyName() + "'s Turn!");
                if (theDefender.getHealth() <= 0) System.out.println("Keep attacking for a chance at gaining health!");
                myFrame.battlePanel(myHero, theDefender, atkLog);
                do{
                    Thread.sleep(200);
                } while (!BattleInput.myAttack && !BattleInput.myInventory);
                BattleInput.myAttack = false;
               //An attack was not made, inventory is now open, roll back heroTurns counter.
                if(BattleInput.myInventory){
                    i--;
                    BattleInput.myInventory = false;
                    myFrame.inventoryPanel(myHero);
                    do {
                        Thread.sleep(200);
                    } while (!InventoryInput.input);
                    InventoryInput.input = false;
                }
                myFrame.battlePanel(myHero, theDefender, atkLog);
            }

            //Enemy's turn, if the enemy is still alive, let the Guardian try to ultimate every 3rd turn.
            if (theDefender.getHealth() > 0) {
                System.out.println(theDefender.getMyName() + "'s Health: " + theDefender.getHealth() + "/" + theDefender.getMaxHealth());
                System.out.println(theDefender.getMyName() + "'s Turn!");
                String result = turnCounter % 3 == 0 && turnCounter != 0 ? theDefender.ultimate(myHero) : theDefender.attack(myHero);
                turnCounter++;
                System.out.print(result);
            }
        }
        //One of our combatants has suffered their untimely demise :'( tell user, update battle log, give user time to mourn.
        if (theDefender.getHealth() <= 0) {
            System.out.println(theDefender.getMyName() + " has been Vanquished!");
            if (theDefender.getHealth() < 0)
                System.out.print("Health Boost for overboard damage: " + myHero.heal((-theDefender.getHealth()), 0));
            System.out.println("You received The " + theDefender.getPillar() + "!!!");
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(3000);
        } else if (myHero.getHealth() <= 0) {
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(3000);
            death("You were swallowed by the Forest.., you succumb to the injuries caused by the " + theDefender.getMyName() + "!");
        }
    }

    private static void death(final String theMessage) throws SQLException, IOException, InterruptedException {
        myFrame.deathPanel(theMessage);
        do {
            Thread.sleep(200);
        } while (DeathInput.input == 0);
        switch (DeathInput.input) {
            case 1 -> {
                DeathInput.input = 0;
                intro();
            }
            case 2 -> exit();
        }
    }

    private static void win() throws SQLException, IOException, InterruptedException {
        System.out.println("\nAll four pillars retrieved! Thank you for playing!");
        System.out.println("Play Again?");
        System.out.print("y/n: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch (input) {
            case "y" -> intro();
            case "n" -> exit();
            default -> win();
        }
    }

    private static void exit() {
        System.exit(0);
    }

    public static class IntroInput implements ActionListener {

        private static int input;

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            switch (name) {
                case "New Game" -> input = 1;
                case "Load Save" -> input = 0;
            }
        }
    }

    public static class HeroInput implements ActionListener {

        public void actionPerformed(ActionEvent e) {

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

    public static class DifficultyInput implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();

            switch (name) {
                case "Bubble Blowin Baby" -> {
                    try {
                        myMaze = new Maze(4, 1, myHero);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Amateur Explorer" -> {
                    try {
                        myMaze = new Maze(5, 2, myHero);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Big Kids Table" -> {
                    try {
                        myMaze = new Maze(7, 3, myHero);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static class BattleInput implements ActionListener {
        private static Character myDefender;
        private static boolean myAttack;
        private static boolean myInventory;

        public BattleInput(Character theDefender) {
            myDefender = theDefender;
        }

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();

            switch (name) {
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
                case "Inventory" -> {
                    myInventory = true;
                }
            }

        }

        private static void resetAttack() {
            myAttack = false;
        }

        private static boolean noAttack() {
            return !myAttack;
        }
    }

    public static class InventoryInput implements ActionListener {
        private static boolean input;

        public void actionPerformed(ActionEvent e) {
            String name = ((JButton) e.getSource()).getName();

            switch (name) {
                case "Health" -> {
                    var potion = new HealthPotion();
                    potion.useEffect(myHero);
                    myHero.getInventory().removeItem("Health Potion");
                }
                case "Vision" -> {
                    var potion = new VisionPotion();
                    potion.useEffect(myMaze);
                    myHero.getInventory().removeItem("Vision Potion");
                }
            }
            input = true;
        }
    }

    public static class MoveInput implements ActionListener {

        private static String myMove;

        public void actionPerformed(ActionEvent e) {
            myMove = e.getActionCommand();

        }
    }

    public static class DeathInput implements ActionListener {

        private static int input;

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            input = 0;

            switch (name) {
                case "Try Again?" -> input = 1;
                case "Give Up?" -> input = 2;
            }
        }

    }
}


