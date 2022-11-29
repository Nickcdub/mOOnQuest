package View;

import Model.Maze;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GameFrame extends JFrame {
    JPanel myMainPanel;

    public GameFrame() {

        //All other panels will be placed on this main panel
        myMainPanel = new JPanel();
        myMainPanel.setBackground(Color.RED);

        myMainPanel.setLayout(new BorderLayout());

        setPreferredSize(new Dimension(800, 800));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        add(myMainPanel);
    }

    public void showMap(/*final Character hero,*/ final Maze theMaze) {
        hideComponents();
        final MapPanel myMap = new MapPanel(theMaze, 400);
        myMainPanel.add(myMap, BorderLayout.CENTER);
        setVisible(true);
    }

    public void showIntro() {
        final IntroPanel localIntro = new IntroPanel(400, this);
        myMainPanel.add(localIntro, BorderLayout.CENTER);
        setVisible(true);
    }

    public void showCharacter() {
        hideComponents();
        final CharacterPanel localCharacter = new CharacterPanel(400, this);
        myMainPanel.add(localCharacter, BorderLayout.CENTER);
        setVisible(true);
    }

    public void showLoad() {
        hideComponents();
        // not implemented;
    }

    public void showDifficulty() {
        hideComponents();
        final DifficultyPanel localDifficulty = new DifficultyPanel(400, this);
        myMainPanel.add(localDifficulty, BorderLayout.CENTER);
        setVisible(true);
    }

    private void hideComponents() {
        Component[] localComponents = myMainPanel.getComponents();
        for (Component c:
                localComponents) {
            c.setVisible(false);
        }
    }
}
