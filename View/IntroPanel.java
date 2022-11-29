package View;

import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {
    private final JButton myNewGameButton;
    private final JButton myLoadGameButton;
    protected IntroPanel(final int thePanelSize, final GameFrame theGameFrame) {
        myNewGameButton = new JButton("New Game");
        myNewGameButton.addActionListener(theEvent -> {
            theGameFrame.showCharacter();
            setVisible(false);
        });

        myLoadGameButton = new JButton("Load Game");
        myLoadGameButton.addActionListener(theEvent -> {
            theGameFrame.showLoad();
            setVisible(false);
        });

        //positioning not implemented
        BoxLayout localLayout = new BoxLayout(IntroPanel.this, BoxLayout.PAGE_AXIS);

        setBackground(Color.YELLOW);
        setPreferredSize(new Dimension(thePanelSize, thePanelSize));
        setLayout(localLayout);
        add(myNewGameButton);
        add(myLoadGameButton);


    }
}
