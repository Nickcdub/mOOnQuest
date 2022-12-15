package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DeathPanel extends JPanel {

    DeathPanel(final String theMessage, final int theWidth, final int theHeight) throws IOException {

        ImagePanel grave = new ImagePanel(ImageIO.read(new File("Graveyard.png")));
        JPanel emptyCenter = new JPanel();
        JPanel south = new JPanel(new GridLayout(2,1));
        JLabel message = new JLabel(theMessage);
        JPanel buttons = new JPanel(new GridLayout(1,2));

        message.setForeground(Color.RED);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Serif",Font.BOLD,32));

        JButton again = new JButton("Try Again?");
        JButton exit = new JButton("Give Up?");

        again.addActionListener(new GameController.DeathInput());
        exit.addActionListener(new GameController.DeathInput());

        buttons.add(again);
        buttons.add(exit);
        buttons.setOpaque(false);

        south.add(message);
        south.add(buttons);
        south.setPreferredSize(new Dimension(theWidth, (int) Math.floor(theHeight*0.3)));
        south.setOpaque(false);

        emptyCenter.setOpaque(false);

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        grave.setLayout(new BorderLayout());

        grave.add(emptyCenter,BorderLayout.CENTER);
        grave.add(south,BorderLayout.SOUTH);
        add(grave,BorderLayout.CENTER);


    }
}
