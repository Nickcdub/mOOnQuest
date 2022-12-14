/*
* GridBagLayout is one of the most complex, yet powerful layouts.
* I used a tutorial to learn GridBagLayout https://www.youtube.com/watch?v=eeE44RmE1FM
* - Nick
 */
package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class IntroPanel extends JPanel {

    IntroPanel() throws IOException {
        ImagePanel background = new ImagePanel(ImageIO.read(new File("Entrance.png")));

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JButton start = new JButton("New Game");
        JButton load = new JButton("Load Save");
        JButton help = new JButton("Help");

        start.addActionListener(new GameController.IntroInput());
        load.addActionListener(new GameController.IntroInput());
        help.addActionListener(new GameController.IntroInput());

        //This says that the distance between this button and anything else should be 10 pixels of space
        c.insets = new Insets(30,300,30,300);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 80;      //make this component tall
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        buttons.add(start,c);
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 2;
        c.gridheight = 1;
        buttons.add(load,c);
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 3;
        c.gridheight = 1;
        buttons.add(help,c);

        buttons.setOpaque(false);
        background.add(buttons,BorderLayout.CENTER);

        setLayout(new GridLayout());
        add(background);
    }
}
