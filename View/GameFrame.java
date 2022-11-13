package View;

import Model.Maze;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GameFrame extends JFrame{
    JPanel mainPanel;
    public GameFrame() {

        //All other panels will be placed on this main panel
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.RED);

        mainPanel.setLayout(new BorderLayout());

        setPreferredSize(new Dimension(800, 800));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        add(mainPanel);
    }

    public void showMap(/*final Character hero,*/ final Maze maze, final int squareSize){
       final MapPanel map = new MapPanel(maze,squareSize);

       mainPanel.add(map,BorderLayout.CENTER);

    }

}
