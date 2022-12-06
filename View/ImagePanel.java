/*
This method of creating an Image Panel was from a thread on stackoverflow
The original creator's username was Maxim Shoustin
Forum URL: https://stackoverflow.com/questions/19125707/simplest-way-to-set-image-as-jpanel-background
This method was used to design all the backgrounds for the panels.
*/
package View;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final Image MY_IMAGE;
    private final int MY_WIDTH;
    private final int MY_HEIGHT;

    ImagePanel(final Image theImage) {
        MY_IMAGE = theImage;
        MY_WIDTH = theImage.getWidth(this)/ 2;
        MY_HEIGHT = theImage.getHeight(this) /2;
        setLayout(new GridLayout());
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (MY_IMAGE != null) {
            //Gets the width and height of the containing class to draw image in the top left corner.
            //The image observer keeps track of space and alerts draw Image to update the image when more space is loaded.
            g.drawImage(MY_IMAGE, this.getParent().getWidth() / 2 - MY_WIDTH, this.getParent().getHeight() / 2 - MY_HEIGHT, this);
        }
    }
}
