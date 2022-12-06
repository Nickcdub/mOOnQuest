package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DifficultyPanel extends JPanel {

    DifficultyPanel() throws IOException {
        ImagePanel background = new ImagePanel(ImageIO.read(new File("DifficultyBackground.png")));

        JPanel button = new JPanel();
        button.setLayout(new GridLayout(1,3));
        button.setPreferredSize(new Dimension(getWidth()/5,getHeight()/5));
        button.setOpaque(false);

        JLabel line1 = new JLabel("Please choose a difficulty!");
        JLabel line2 = new JLabel("Bubble Blowin Baby: 4 by 4 maze, nothing too fancy, couple goblins, couple pillars.");
        JLabel line3 = new JLabel("Amateur Explorer: Watched a lot of Dora as a kid, feel confident, 5 by 5 maze, goblins, wolves, and pillars.");
        JLabel line4 = new JLabel(" Big Kids Table: You eat nails for breakfast... without any milk! 7 by 7 maze, goblins, wolves, ogres oh my! Oh, and pillars too.");

        line1.setPreferredSize(new Dimension(getWidth()/5,getHeight()/5));
        line2.setPreferredSize(new Dimension(getWidth()/5,getHeight()/5));
        line3.setPreferredSize(new Dimension(getWidth()/5,getHeight()/5));
        line4.setPreferredSize(new Dimension(getWidth()/5,getHeight()/5));

        line1.setFont(new Font("Serif",Font.BOLD,24));
        line2.setFont(new Font("Serif",Font.BOLD,24));
        line3.setFont(new Font("Serif",Font.BOLD,24));
        line4.setFont(new Font("Serif",Font.BOLD,24));

        line1.setForeground(Color.WHITE);
        line2.setForeground(Color.WHITE);
        line3.setForeground(Color.WHITE);
        line4.setForeground(Color.WHITE);

        JButton easy = new JButton("Bubble Blowin Baby");
        JButton medium = new JButton("Amateur Explorer");
        JButton hard = new JButton("Big Kids Table");

        easy.addActionListener(new GameController.DifficultyInput());
        medium.addActionListener(new GameController.DifficultyInput());
        hard.addActionListener(new GameController.DifficultyInput());

        button.add(easy);
        button.add(medium);
        button.add(hard);

        background.setLayout(new GridLayout(5,1));
        background.add(line1);
        background.add(line2);
        background.add(line3);
        background.add(line4);
        background.add(button);

        setLayout(new BorderLayout());
        add(background,BorderLayout.CENTER);
    }
}
