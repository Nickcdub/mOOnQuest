package View;

import Model.*;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.sql.SQLException;

public class GameFrame extends JFrame {
    private final JPanel MAIN_PANEL;
    private final int MY_WIDTH, MY_HEIGHT;

    public GameFrame(final int theWidth, final int theHeight){

        //All other panels will be placed on this main panel
        MAIN_PANEL = new JPanel();
        MY_WIDTH = theWidth;
        MY_HEIGHT = theHeight;

        MAIN_PANEL.setBackground(Color.GREEN);

        MAIN_PANEL.setLayout(new BorderLayout());

        setPreferredSize(new Dimension( theWidth, theHeight));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       pack();
        add(MAIN_PANEL);
    }

    public void introPanel() throws IOException {
        clearPanels();
        IntroPanel intro = new IntroPanel();
        MAIN_PANEL.add(intro, BorderLayout.CENTER);
        setVisible(true);
    }

    public void savePanel() throws IOException {
        clearPanels();
        SaveLoadPanel save = new SaveLoadPanel(MY_WIDTH,MY_HEIGHT);
        MAIN_PANEL.add(save, BorderLayout.CENTER);
        setVisible(true);
    }

    public void showMap(final Maze theMaze, final Hero theHero, final StringBuilder theBuilder) throws IOException {
        clearPanels();
        final MapPanel myMap = new MapPanel(theMaze, 400, theHero, theBuilder);
        MAIN_PANEL.add(myMap, BorderLayout.CENTER);
        setVisible(true);

    }

    public void characterSelectPanel() throws SQLException, IOException {
        clearPanels();
        final JPanel SELECT = new JPanel();
        SELECT.setLayout(new GridLayout());

        final CharacterPanel knight = new CharacterPanel(new Knight(),Color.decode("#204CC5") ,Color.decode("#455AC1"), MY_WIDTH /3, MY_HEIGHT);
        final CharacterPanel mender = new CharacterPanel(new Mender(),Color.decode("#2BA304"),Color.decode("#89DD6E"), MY_WIDTH /3, MY_HEIGHT);
        final CharacterPanel assassin = new CharacterPanel(new Assassin(),Color.decode("#AC2306"),Color.decode("#C96161"), MY_WIDTH /3, MY_HEIGHT);

        knight.setSelection();
        mender.setSelection();
        assassin.setSelection();

        SELECT.add(knight);
        SELECT.add(mender);
        SELECT.add(assassin);

        MAIN_PANEL.add(SELECT,BorderLayout.CENTER);
        setVisible(true);
    }

    public void difficultyPanel() throws IOException {
        clearPanels();
        DifficultyPanel diff = new DifficultyPanel();
        MAIN_PANEL.add(diff,BorderLayout.CENTER);
        setVisible(true);
    }

    public void battlePanel(final Hero theHero, final Monster theDefender, final StringBuilder theBuilder) throws IOException {
    clearPanels();

        //Left and right panels will hold our two combatants
        CharacterPanel hero = new CharacterPanel(theHero,Color.decode("#3DB4FF"),null, MY_WIDTH /3, MY_HEIGHT);
        CharacterPanel enemy = new CharacterPanel(theDefender,Color.decode("#DC143C"),null, MY_WIDTH /3, MY_HEIGHT);

        BattlePanel battle = new BattlePanel(hero,enemy,"Forest.png",theBuilder);

        MAIN_PANEL.add(battle,BorderLayout.CENTER);
        setVisible(true);

    }

    public void battlePanel(final Hero theHero, final Guardian theDefender,final StringBuilder theString) throws IOException {
       clearPanels();
        CharacterPanel hero = new CharacterPanel(theHero,Color.decode("#3DB4FF"),null, MY_WIDTH /3, MY_HEIGHT);
        CharacterPanel enemy = new CharacterPanel(theDefender,Color.decode("#CF54FF"),null, MY_WIDTH /3, MY_HEIGHT);

        String background = theDefender.getMyName();
        switch(background){
            case "HYDRA" -> background = "Swamp.png";
            case "RED_DRAGON" -> background = "Volcano.png";
            case "CERBERUS" -> background = "UnderWorld.png";
            case "TOM" -> background = "Space.png";
            default -> background = "Forest.png";
        }

        BattlePanel battle = new BattlePanel(hero,enemy,background, theString);
        MAIN_PANEL.add(battle,BorderLayout.CENTER);
        setVisible(true);
    }

    public void inventoryPanel(final Hero theHero) throws IOException {
        clearPanels();
        InventoryPanel inventory = new InventoryPanel(theHero);
        MAIN_PANEL.add(inventory,BorderLayout.CENTER);
        setVisible(true);
    }

    public void deathPanel(final String theMessage) throws IOException {
        clearPanels();
        DeathPanel death = new DeathPanel(theMessage, MY_WIDTH,MY_HEIGHT);
        MAIN_PANEL.add(death, BorderLayout.CENTER);
        setVisible(true);
    }

    public void winPanel() throws IOException {
        clearPanels();
        WinPanel win = new WinPanel(MY_WIDTH,MY_HEIGHT);
        MAIN_PANEL.add(win, BorderLayout.CENTER);
        setVisible(true);
    }

    public void clearPanels(){
        MAIN_PANEL.removeAll();
    }

}

