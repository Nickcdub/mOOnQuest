package View;

import Controller.GameController;
import Model.*;
import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class GameFrame extends JFrame {
    private final JPanel MAIN_PANEL;
    private int myWidth, myHeight;

    public GameFrame(final int theWidth, final int theHeight) throws IOException {

        //All other panels will be placed on this main panel
        MAIN_PANEL = new JPanel();
        myWidth = theWidth;
        myHeight = theHeight;

        MAIN_PANEL.setBackground(Color.GREEN);

        MAIN_PANEL.setLayout(new BorderLayout());

        setPreferredSize(new Dimension( theWidth, theHeight));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       pack();
        add(MAIN_PANEL);
    }

    public void showMap(final Maze theMaze, final Hero theHero) throws IOException {
        clearPanels();
        final MapPanel myMap = new MapPanel(theMaze, 400, theHero);
        MAIN_PANEL.add(myMap, BorderLayout.CENTER);
        setVisible(true);

    }

    public void characterSelectPanel() throws SQLException, IOException {
        clearPanels();
        final JPanel SELECT = new JPanel();
        SELECT.setLayout(new GridLayout());

        final CharacterPanel knight = new CharacterPanel(new Knight(),Color.decode("#204CC5") ,Color.decode("#455AC1"), myWidth/3, myHeight);
        final CharacterPanel mender = new CharacterPanel(new Mender(),Color.decode("#2BA304"),Color.decode("#89DD6E"), myWidth/3, myHeight);
        final CharacterPanel assassin = new CharacterPanel(new Assassin(),Color.decode("#AC2306"),Color.decode("#C96161"), myWidth/3, myHeight);

        knight.setSelection();
        mender.setSelection();
        assassin.setSelection();

        SELECT.add(knight);
        SELECT.add(mender);
        SELECT.add(assassin);

        MAIN_PANEL.add(SELECT,BorderLayout.CENTER);
        setVisible(true);
    }

    public void battlePanel(final Hero theHero, final Monster theDefender, final StringBuilder theString) throws IOException {
    clearPanels();

        //Left and right panels will hold our two combatants
        CharacterPanel hero = new CharacterPanel(theHero,Color.decode("#204CC5"),new Color(0,0,0,0),myWidth/3, myHeight);
        CharacterPanel enemy = new CharacterPanel(theDefender,Color.decode("#DC143C"),new Color(0,0,0,0),myWidth/3, myHeight);

        BattlePanel battle = new BattlePanel(hero,enemy,"Forest.png",theString);

        MAIN_PANEL.add(battle,BorderLayout.CENTER);
        setVisible(true);

    }

    public void battlePanel(final Hero theHero, final Guardian theDefender,final StringBuilder theString) throws IOException {
       clearPanels();
        CharacterPanel hero = new CharacterPanel(theHero,Color.decode("#204CC5"),new Color(0,0,0,0),myWidth/3, myHeight);
        CharacterPanel enemy = new CharacterPanel(theDefender,Color.decode("#81007F"),new Color(0,0,0,0),myWidth/3, myHeight);

        String background = theDefender.getMyName();
        switch(background){
            case "HYDRA" -> background = "Swamp.png";
            case "RED_DRAGON" -> background = "Volcano.png";
            case "CERBERUS" -> background = "UnderWorld.png";
            default -> background = "Forest.png";
        }

        BattlePanel battle = new BattlePanel(hero,enemy,background, theString);
        MAIN_PANEL.add(battle,BorderLayout.CENTER);
        setVisible(true);
    }

    public void clearPanels(){
        MAIN_PANEL.removeAll();
    }

}

