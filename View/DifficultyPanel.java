package View;

import javax.swing.*;
import java.awt.*;

public class DifficultyPanel extends JPanel {
    private final JButton myEasyButton;
    private final JButton myMediumButton;
    private final JButton myHardButton;
    protected DifficultyPanel(final int thePanelSize, final GameFrame theGameFrame) {
        myEasyButton = new JButton("Bubble Blowin' Baby");
        myEasyButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myMaze
            //not implemented
        });

        myMediumButton = new JButton("Amateur Explorer");
        myMediumButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myMaze
            //not implemented
        });

        myHardButton = new JButton("Big Kids Table");
        myHardButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myMaze
            //not implemented
        });

        //positioning not implemented
        BoxLayout localLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

        setBackground(Color.ORANGE);
        setPreferredSize(new Dimension(thePanelSize, thePanelSize));
        setLayout(localLayout);
        add(myEasyButton);
        add(myMediumButton);
        add(myHardButton);
    }
}