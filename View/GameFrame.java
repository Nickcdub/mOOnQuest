package View;

import Model.AbstractClasses.Hero;
import Model.Knight;
import Model.Maze;
import Model.Mender;
import Model.Assassin;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class GameFrame extends JFrame {
    private final JPanel MAIN_PANEL;
    private int myWidth, myHeight;

    public GameFrame(final int theWidth, final int theHeight) {

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
        final MapPanel myMap = new MapPanel(theMaze, 400, theHero);
        MAIN_PANEL.add(myMap, BorderLayout.CENTER);
        setVisible(true);

    }

    public void characterSelectPanel() throws SQLException, IOException {
        final JPanel SELECT = new JPanel();
        SELECT.setLayout(new GridLayout());

        final CharacterPanel knight = new CharacterPanel(new Knight(),Color.decode("#2138AB"),Color.decode("#455AC1"), myWidth/3, myHeight);
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

    public void clearPanels(){
        MAIN_PANEL.removeAll();
    }
   /* public void testCharacter(final Character theCharacter, final Color theColor, final boolean theBackground) throws IOException {
        final CharacterPanel myCharacter = new CharacterPanel(theCharacter,theColor,theBackground, myWidth, myHeight);
        MAIN_PANEL.add(myCharacter,BorderLayout.CENTER);
        setVisible(true);
    }*/

}
