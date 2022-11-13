package View;

import Model.Maze;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MapPanel extends JPanel {
    //final Character hero;
    final Maze maze;

    protected MapPanel(/*final Character hero,*/ final Maze maze, final int panelSize) {
        //this.hero = hero;
        this.maze=maze;

        //This panel will hold our maze map
        setBackground(Color.BLACK);

        //Set grid layout for our maze grid
        setLayout(new GridLayout(maze.getRows()+1,maze.getColumns()+1));

        //Create RegionPanels and add them to grid
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                add(new RegionPanel(maze.getRegion(i,j)));
            }
        }

        //setOpaque(false);
       setPreferredSize(new Dimension(panelSize,panelSize));
    }

    class RegionPanel extends JPanel{
         private Maze.Region region;

        public RegionPanel(Maze.Region region) {
            boolean[] walls = region.getWalls();
            //Size factor will control how many pixels it takes to draw a wall.
            int sizeFacter = 1;
            //Make walls, true = 1, false = 0
            int top = sizeFacter*(walls[2] ? 1:0);
            int left = sizeFacter*(walls[0] ? 1:0);
            int bottom = sizeFacter*(walls[3] ? 1:0);
            int right = sizeFacter*(walls[1] ? 1:0);
            setBackground(Color.GREEN);
            setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
           // setOpaque(true);
        }
    }
}
