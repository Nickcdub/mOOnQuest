package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SaveLoadPanel extends JPanel {

    public SaveLoadPanel(final int theWidth, final int theHeight) throws IOException {
        //Our background will be the same as InventoryPanel
        ImagePanel background = new ImagePanel(ImageIO.read(new File("Inventory.png")));
        JPanel center = new JPanel();

        JButton s1 = new JButton("Save 1");
        JButton s2 = new JButton("Save 2");
        JButton s3 = new JButton("Save 3");
        JButton back = new JButton("Back");

        s1.addActionListener(new GameController.SaveInput());
        s2.addActionListener(new GameController.SaveInput());
        s3.addActionListener(new GameController.SaveInput());
        back.addActionListener(new GameController.SaveInput());

        center.setOpaque(false);
        center.setLayout(new GridBagLayout());
        back.setPreferredSize(new Dimension(theWidth,theHeight/10));

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
