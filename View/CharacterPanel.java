package View;

import javax.swing.*;
import java.awt.*;

public class CharacterPanel extends JPanel {
    private final JButton myKnightButton;
    private final JButton myAssassinButton;
    private final JButton myMenderButton;
    protected CharacterPanel(final int thePanelSize, final GameFrame theGameFrame) {
        myKnightButton = new JButton("Knight");
        myKnightButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myHero
            theGameFrame.showDifficulty();
            setVisible(false);
        });

        myAssassinButton = new JButton("Assassin");
        myAssassinButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myHero
            theGameFrame.showDifficulty();
            setVisible(false);
        });

        myMenderButton = new JButton("Mender");
        myMenderButton.addActionListener(theEvent -> {
            // logic must be moved to the controller somehow because we don't have access to the myHero
            theGameFrame.showDifficulty();
            setVisible(false);
        });

        //positioning not implemented
        BoxLayout localLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(thePanelSize, thePanelSize));
        setLayout(localLayout);
        add(myKnightButton);
        add(myAssassinButton);
        add(myMenderButton);
    }
}
