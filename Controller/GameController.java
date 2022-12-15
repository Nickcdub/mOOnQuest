package Controller;

import Model.*;
import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;
import View.GameFrame;
import com.google.gson.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLException;

public class GameController {
    //These are necessary for the playing of the game
    private static Hero myHero;
    private static Maze myMaze;
    private static GameFrame myFrame;

    //These are for serialization
    private static Gson myGson;
    private static FileWriter myMazeWriter;
    private static FileReader myMazeReader;


    public GameController() throws SQLException, IOException, InterruptedException {
        //Initialize the Gson object
        myGson = new GsonBuilder()
                .registerTypeAdapter(Hero.class, new HeroAdapter()) //abstract
                .registerTypeAdapter(Guardian.class, new GuardianAdapter()) //abstract
                .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT) //required to handle static variables
                .create();
        intro(); //Start at the intro panel
    }

    private static void intro() throws SQLException, IOException, InterruptedException {
        //Reset the hero and the maze
        myHero = null;
        myMaze = null;
        //Game can be replayed after death or win, don't open a new frame if that's the case
        if (myFrame == null) {
            myFrame = new GameFrame(1000, 1000);
            myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        //Launch panel and wait for response
        myFrame.introPanel();
        do {
            Thread.sleep(200);
        } while (IntroInput.myInput == 0);

        //Make response so we can make IntroInput 0
        int response = IntroInput.myInput;
        IntroInput.myInput = 0;
        switch (response) {
            case 1 -> characterSelect();
            case 2 -> load();
            default -> {
                help();
                intro();
            }
        }
    }

    private static void load() throws IOException, InterruptedException, SQLException {
        myFrame.savePanel(false);
        do {
            Thread.sleep(200);
        } while (SaveInput.myInput == 0);

        int response = SaveInput.myInput;
        SaveInput.myInput = 0;
        if(response>3) intro();
        //read in file and update maze
        try {
            myMazeReader = new FileReader("maze" + response + ".txt");
            myMaze = myGson.fromJson(myMazeReader, Maze.class);
            traverse();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static void characterSelect() throws SQLException, IOException, InterruptedException {

        //Launch Character select panel
        myFrame.characterSelectPanel();
        do {
            Thread.sleep(200);
        } while (myHero == null);
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
        //Keep playing the game until all pillars are collected and the player returns to entrance
        while (myHero.getPillarCount() != 4 || myMaze.getHeroLocation()[0] + myMaze.getHeroLocation()[1] != 0) {
            System.setOut(ps);
            //Wait for user input
            do {
                Thread.sleep(200);
            } while (MoveInput.myMove == null);
            //Apply appropriate logic to input
            switch (MoveInput.myMove) {
                case "North" -> travelLog.append(myMaze.move("NORTH"));
                case "West" -> travelLog.append(myMaze.move("WEST"));
                case "East" -> travelLog.append(myMaze.move("EAST"));
                case "South" -> travelLog.append(myMaze.move("SOUTH"));
                case "Inventory" -> {
                    myFrame.inventoryPanel(myHero);
                    do {
                        Thread.sleep(200);
                    } while (!InventoryInput.input);
                    InventoryInput.input = false;
                }
                case "Save" -> save();
                case "Help" -> help();
            }
            MoveInput.myMove = null;
            //Update frame as often as possible
            myFrame.showMap(myMaze, myHero, travelLog);

            //Check for death, enemy to fight, boss to fight
            if (myHero.getHealth() <= 0) {
                death("You hit a trap and lacked the energy to escape, you fell unconscious...");
            } else if (myMaze.getEnemy() != null) {
                System.out.println("Defeated a " + myMaze.getEnemy().getMyName());
                battle(myMaze.popEnemy());
            } else if (myMaze.getBoss() != null) {
                System.out.println("Vanquished the " + myMaze.getBoss().getMyName());
                battle(myMaze.popBoss());
                myHero.addPillar();
            }
            myFrame.showMap(myMaze, myHero, travelLog);
        }
        //while loop broken, we won
        win();
    }

    private static void save() throws IOException, InterruptedException {
        //load save panel and wait for input
        myFrame.savePanel(true);
        do {
            Thread.sleep(200);
        } while (SaveInput.myInput == 0);
        //input below 4 is a save button, after that is the button to go back
        if (SaveInput.myInput < 4) {
            myMazeWriter = new FileWriter("maze" + SaveInput.myInput + ".txt", false);
            //Initialize the FileWriter here because setting the false flag will clear the file
            myGson.toJson(myMaze, myMazeWriter);
            myMazeWriter.flush();
        }
        SaveInput.myInput = 0;
    }

    private static void help() throws InterruptedException {
        //Wait for input, only possible input is back
        myFrame.helpPanel();
        do {
            Thread.sleep(200);
        } while (HelpInput.myInput == 0);
        HelpInput.myInput = 0;
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
                if (BattleInput.myInventory) {
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
            //Make sure defender health is less than 0, or we'll get an error
            if (theDefender.getHealth() < 0)
                System.out.println("Health Boost for overboard damage: " + myHero.heal((-theDefender.getHealth()), 0));
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(1000);
        } else if (myHero.getHealth() <= 0) {
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(1000);
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
                do {
                    Thread.sleep(200);
                } while (!BattleInput.myAttack && !BattleInput.myInventory);
                BattleInput.myAttack = false;
                //An attack was not made, inventory is now open, roll back heroTurns counter.
                if (BattleInput.myInventory) {
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
            Thread.sleep(1000);
        } else if (myHero.getHealth() <= 0) {
            myFrame.battlePanel(myHero, theDefender, atkLog);
            Thread.sleep(1000);
            death("You were swallowed by the Forest.., you succumb to the injuries caused by the " + theDefender.getMyName() + "!");
        }
    }

    private static void death(final String theMessage) throws SQLException, IOException, InterruptedException {
        //load frame wait for input
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
        myFrame.winPanel();
        do {
            Thread.sleep(200);
        } while (WinInput.input == 0);
        //case 1, clear pillars and reset maze to play the game again.
        switch (WinInput.input) {
            case 1 -> {
                WinInput.input = 0;
                myHero.clearPillarCount();
                myMaze = null;
                difficultySelect();
            }
            case 2 -> exit();
        }
    }

    private static void exit() throws IOException {
        //Prevent null pointers
        if (myMazeReader != null) myMazeReader.close();
        if (myMazeWriter != null) myMazeWriter.close();
        System.exit(0);
    }



    /*
    *
    * KeyListeners
    * The listeners cannot initiate other panels while the controller is waiting for a response.
    * Pass back a value that correlates with a desired response to the input
    *
    *
     */



    public static class IntroInput implements ActionListener {

        private static int myInput;

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            switch (name) {
                case "New Game" -> myInput = 1;
                case "Load Save" -> myInput = 2;
                case "Help" -> myInput = 3;
            }
        }
    }

    public static class SaveInput implements ActionListener {

        private static int myInput;

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            switch (name) {
                case "Save 1" -> myInput = 1;
                case "Save 2" -> myInput = 2;
                case "Save 3" -> myInput = 3;
                case "Back" -> myInput = 4;
            }
        }
    }

    public static class HelpInput implements ActionListener {
        private static int myInput;

        public void actionPerformed(ActionEvent e) {
            myInput = 1;
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
                        myMaze = new Maze(5, 1, myHero);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Amateur Explorer" -> {
                    try {
                        myMaze = new Maze(7, 2, myHero);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Big Kids Table" -> {
                    try {
                        myMaze = new Maze(8, 3, myHero);
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
                case "God Mode" -> {
                    System.out.println("God Mode Activated!");
                    myHero.setGod();
                    myAttack = true;
                }
                case "Inventory" -> myInventory = true;
            }

        }

        private static void resetAttack() {
            myAttack = false;
        }
    }

    public static class InventoryInput implements ActionListener {
        private static boolean input;

        public void actionPerformed(ActionEvent e) {
            String name = ((JButton) e.getSource()).getName();

            switch (name) {
                case "Health" -> {
                    if (myHero.getHealth() != myHero.getMaxHealth()) {
                        var potion = new HealthPotion();
                        System.out.println(potion.useEffect(myHero));
                        myHero.getInventory().removeItem("Health Potion");
                    } else {
                        System.out.println("Hero already at max health.");
                    }
                }
                case "Vision" -> {
                    var potion = new VisionPotion();
                    System.out.println(potion.useEffect(myMaze));
                    myHero.getInventory().removeItem("Vision Potion");
                }
            }
            input = true;
        }
    }

    public static class MoveInput implements ActionListener {

        private static String myMove;

        public void actionPerformed(ActionEvent e) {
            //myMove = e.getActionCommand();
            myMove = ((JButton) e.getSource()).getName();
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

    public static class WinInput implements ActionListener {

        private static int input;

        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            input = 0;

            switch (name) {
                case "Keep Playing?" -> input = 1;
                case "Touch Grass?" -> input = 2;
            }
        }
    }

    // These classes was created to handle serialization for the abstract hero and guardian classes (they couldn't be instantiated otherwise)
    class HeroAdapter implements JsonDeserializer<Hero> {
        @Override
        public Hero deserialize(final JsonElement theJsonElement, final Type theType, final JsonDeserializationContext theJsonDeserializationContext) throws JsonParseException {
            try {
                String contents = theJsonElement.toString();
                if (contents.contains("KNIGHT")) {
                    myHero = myGson.fromJson(contents, Knight.class);
                }
                if (contents.contains("MENDER")) {
                    myHero = myGson.fromJson(contents, Mender.class);
                }
                if (contents.contains("ASSASSIN")) {
                    myHero = myGson.fromJson(contents, Assassin.class);
                }
                return myHero;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }

    class GuardianAdapter implements JsonDeserializer<Guardian> {
        @Override
        public Guardian deserialize(final JsonElement theJsonElement, final Type theType, final JsonDeserializationContext theJsonDeserializationContext) throws JsonParseException {
            try {
                String contents = theJsonElement.toString();
                if (contents.contains("CERBERUS")) {
                    return new Cerberus();
                }
                if (contents.contains("RED_DRAGON")) {
                    return new RedDragon();
                }
                if (contents.contains("HYDRA")) {
                    return new Hydra();
                }
                if (contents.contains("TOM")) {
                    return new Tom();
                }
                return null;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }
}


