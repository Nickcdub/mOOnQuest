package View;

import Controller.GameController;
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

    MapPanel(final Maze theMaze, final int thePanelSize, final Hero theHero, final StringBuilder theBuilder) throws IOException {

        //This panel will hold our maze map and activity log
        setLayout(new BorderLayout());

        MAZE = theMaze;
        PANEL_SIZE = thePanelSize;
        HERO = theHero;
        //left maze panel
        ImagePanel mazePanel = new ImagePanel(ImageIO.read(new File("Map.png")));
        //right side panel
        JPanel sidePanel = new JPanel();
        JPanel movePanel = new JPanel();
        JTextArea txt = new JTextArea();
        JScrollPane log = new JScrollPane(txt);

        //Set grid layout for our maze grid, borderLayout for our sidePanel
        mazePanel.setLayout(new GridLayout(theMaze.getRows() + 1, theMaze.getColumns() + 1));
        sidePanel.setLayout(new GridLayout(2,1));

        //SubPanels of sidePanel

        log.setVerticalScrollBarPolicy(log.VERTICAL_SCROLLBAR_ALWAYS);
        log.setHorizontalScrollBarPolicy(log.HORIZONTAL_SCROLLBAR_ALWAYS);
        txt.setEditable(false);
        txt.insert(theBuilder.toString(),0);

        movePanel.setLayout(new BorderLayout());

        //Creat North South East West buttons and add action listeners
        JButton north = new JButton("North");
        JButton west = new JButton("West");
        JButton inventory = new JButton("Inventory");
        JButton east = new JButton("East");
        JButton south = new JButton("South");

        north.addActionListener(new GameController.MoveInput());
        west.addActionListener(new GameController.MoveInput());
        inventory.addActionListener(new GameController.MoveInput());
        east.addActionListener(new GameController.MoveInput());
        south.addActionListener(new GameController.MoveInput());

        //Only display these buttons if they are paths that are possible
        if(!MAZE.getPath().contains("NORTH")) {
            north.setEnabled(false);
            north.setForeground(Color.WHITE);
        }
        if(!MAZE.getPath().contains("WEST")) {
            west.setEnabled(false);
            west.setForeground(Color.WHITE);
        }
        if(!MAZE.getPath().contains("EAST")){
            east.setEnabled(false);
            east.setForeground(Color.WHITE);
        }
        if(!MAZE.getPath().contains("SOUTH")){
            south.setEnabled(false);
            south.setForeground(Color.WHITE);
        }

        north.setPreferredSize(new Dimension(thePanelSize,thePanelSize/6));
        east.setPreferredSize(new Dimension(thePanelSize/5,thePanelSize/3));
        inventory.setPreferredSize(new Dimension(0,thePanelSize/3));
        west.setPreferredSize(new Dimension(thePanelSize/5,thePanelSize/3));
        south.setPreferredSize(new Dimension(thePanelSize,thePanelSize/6));

        movePanel.add(north ,BorderLayout.NORTH);
        movePanel.add(west ,BorderLayout.WEST);
        movePanel.add(inventory,BorderLayout.CENTER);
        movePanel.add(east ,BorderLayout.EAST);
        movePanel.add(south ,BorderLayout.SOUTH);

        sidePanel.add(movePanel);
        sidePanel.add(log);

        //Create RegionPanels and add them to grid
        for (int i = 0; i < theMaze.getRows(); i++) {
            for (int j = 0; j < theMaze.getColumns(); j++) {
                mazePanel.add(new RegionPanel(theMaze, i, j));
            }
        }


        add(mazePanel,BorderLayout.CENTER);
        add(sidePanel,BorderLayout.EAST);

        sidePanel.setPreferredSize(new Dimension(thePanelSize, thePanelSize));
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
            setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
            //If region is not visible, don't show it
            if(!theMaze.getRegionVisibility(theRow,theCol)) {
                setVisible(false);
                setOpaque(false);
            } else setBackground(Color.decode("#F3EAD3"));

            //If both the row and the column are 0, its the entrance, make it green
            if(theRow+theCol == 0) {
                setBackground(Color.decode("#A1E48F"));
                add(new JLabel("<<ENTRANCE<<"));
            }

            //If the hero is on this space, set background to blue and add the hero's image
            if(theMaze.getHeroLocation()[0] == theRow && theMaze.getHeroLocation()[1] == theCol) {
                setBackground(Color.decode("#016064"));
                BufferedImage myPicture = ImageIO.read(new File(HERO.getMyName()+".png"));
                Image img =myPicture.getScaledInstance(PANEL_SIZE/MAZE.getColumns(),PANEL_SIZE/MAZE.getRows()+30,0);

                JLabel picLabel = new JLabel(new ImageIcon(img));
                //picLabel.setPreferredSize(new Dimension(PANEL_SIZE/MAZE.getColumns(),PANEL_SIZE/MAZE.getRows()));
                add(picLabel);
            }
            //If guardian: make space purple, else if monster/hazard: make red, else if potion: gold
            if(theMaze.getRegionGuardian(theRow,theCol)) setBackground(Color.decode("#81007F"));
            else if(theMaze.getRegionMonster(theRow,theCol) || theMaze.getRegionTrap(theRow,theCol)) setBackground(Color.decode("#DC143C"));
            else if(theMaze.getRegionPotion(theRow,theCol)) setBackground(Color.decode("#d4af37"));
        }
    }
}
