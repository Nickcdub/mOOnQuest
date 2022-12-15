package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SaveLoadPanel extends JPanel {

    SaveLoadPanel(final int theWidth, final int theHeight, final boolean theDisplayEmptyButtons) throws IOException {
        //Our background will be the same as InventoryPanel
        ImagePanel background = new ImagePanel(ImageIO.read(new File("Inventory.png")));
        JPanel center = new JPanel();

        //Initialize buttons
        JButton s1 = new JButton("Save 1");
        JButton s2 = new JButton("Save 2");
        JButton s3 = new JButton("Save 3");
        JButton back = new JButton("Back");

        //add action listeners
        s1.addActionListener(new GameController.SaveInput());
        s2.addActionListener(new GameController.SaveInput());
        s3.addActionListener(new GameController.SaveInput());
        back.addActionListener(new GameController.SaveInput());

        center.setOpaque(false);
        center.setLayout(new GridBagLayout());
        back.setPreferredSize(new Dimension(theWidth,theHeight/10));

       if(!theDisplayEmptyButtons) {
           //check if saves have contents (only for loading, not for saving)
           File f1 = new File("maze1.txt");
           File f2 = new File("maze2.txt");
           File f3 = new File("maze3.txt");
           //If contents are empty, mark as empty
           if (f1.length() == 0) {
               s1.setEnabled(false);
               s1.setText("Empty");
           }
           if (f2.length() == 0) {
               s2.setEnabled(false);
               s2.setText("Empty");
           }
           if (f3.length() == 0) {
               s3.setEnabled(false);
               s3.setText("Empty");
           }
       }

        //Add buttons use gridbaglayout
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(30,300,30,300);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 80;      //make this component tall
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        center.add(s1,c);
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 2;
        c.gridheight = 1;
        center.add(s2,c);
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 3;
        c.gridheight = 1;
        center.add(s3,c);

        background.add(center);
        setLayout(new BorderLayout());
        add(background);
        add(back,BorderLayout.SOUTH);
    }
}
