package View;

import Model.AbstractClasses.Hero;
import Model.Maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPanel extends JPanel {

    private final int PANEL_SIZE;
    private final Maze MAZE;
    private final Hero HERO;
    protected MapPanel(final Maze theMaze, final int thePanelSize, final Hero theHero) throws IOException {

        //This panel will hold our maze map
        setBackground(Color.BLACK);

        MAZE = theMaze;
        PANEL_SIZE = thePanelSize;
        HERO = theHero;

        //Set grid layout for our maze grid
        setLayout(new GridLayout(theMaze.getRows() + 1, theMaze.getColumns() + 1));

        //Create RegionPanels and add them to grid
        for (int i = 0; i < theMaze.getRows(); i++) {
            for (int j = 0; j < theMaze.getColumns(); j++) {
                add(new RegionPanel(theMaze, i, j));
            }
        }

        //setOpaque(false);
        setPreferredSize(new Dimension(thePanelSize, thePanelSize));
    }

    class RegionPanel extends JPanel {

        private RegionPanel(final Maze theMaze, int theRow, int theCol) throws IOException {

            boolean[] walls = theMaze.getRegionWalls(theRow, theCol);
            //Size factor will control how many pixels it takes to draw a wall.
            int sizeFacter = 4;
            //Make walls, true = 1, false = 0 & multiply them by the sizeFactor
            int top = sizeFacter * (walls[2] ? 1 : 0);
            int left = sizeFacter * (walls[0] ? 1 : 0);
            int bottom = sizeFacter * (walls[3] ? 1 : 0);
            int right = sizeFacter * (walls[1] ? 1 : 0);
            setBackground(Color.decode("#F3EAD3"));
            setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
            if(theMaze.getHeroLocation()[0] == theRow && theMaze.getHeroLocation()[1] == theCol) {
                setBackground(Color.decode("#016064"));
                BufferedImage myPicture = ImageIO.read(new File(HERO.getMyName()+".png"));
                Image img =myPicture.getScaledInstance(PANEL_SIZE/MAZE.getColumns(),PANEL_SIZE/MAZE.getRows()+30,0);

                JLabel picLabel = new JLabel(new ImageIcon(img));
                //picLabel.setPreferredSize(new Dimension(PANEL_SIZE/MAZE.getColumns(),PANEL_SIZE/MAZE.getRows()));
                add(picLabel);
            }
            if(theMaze.getRegionGuardian(theRow,theCol)) setBackground(Color.decode("#81007F"));
            else if(theMaze.getRegionMonster(theRow,theCol) || theMaze.getRegionTrap(theRow,theCol)) setBackground(Color.decode("#DC143C"));
            else if(theMaze.getRegionPotion(theRow,theCol)) setBackground(Color.decode("#d4af37"));
        }
    }
}
