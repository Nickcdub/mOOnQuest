package View;

import Controller.GameController;
import Model.AbstractClasses.Hero;
import Model.Inventory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class InventoryPanel extends JPanel {

    InventoryPanel(final Hero theHero) throws IOException {
        Inventory inventory = theHero.getInventory();
        ImagePanel background = new ImagePanel(ImageIO.read(new File("Inventory.png")));
        JPanel items = new JPanel(new GridLayout(1,inventory.getSize()));
        JButton back = new JButton("Back");
        back.setName("back");
        back.addActionListener(new GameController.InventoryInput());

        //Only add items to the inventory if we have them
        if(inventory.getItem("Health Potion")>0){
            BufferedImage buttonIcon = ImageIO.read(new File("HealthPotion.png"));
            ImageIcon img = new ImageIcon(buttonIcon);
            JButton Health = new JButton(img);
            Health.setName("Health");
            addItem(items,Health);
        }
        if(inventory.getItem("Vision Potion")>0) {
            BufferedImage buttonIcon = ImageIO.read(new File("VisionPotion.png"));
            ImageIcon img = new ImageIcon(buttonIcon);
            JButton Vision = new JButton(img);
            Vision.setName("Vision");
            addItem(items,Vision);
        }
        back.setPreferredSize(new Dimension(100,100));

        items.setOpaque(false);

        background.setLayout(new BorderLayout());
        background.add(items, BorderLayout.CENTER);
        background.add(back,BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(background);
    }

    //Add button images to the Inventory panel
    private void addItem(final JPanel thePanel, final JButton theItemButton) {
        theItemButton.setBorder(BorderFactory.createEmptyBorder());
        theItemButton.setContentAreaFilled(false);
        theItemButton.addActionListener(new GameController.InventoryInput());
        theItemButton.setOpaque(false);
        thePanel.add(theItemButton);
    }
}
