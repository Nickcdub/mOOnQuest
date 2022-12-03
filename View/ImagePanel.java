/*
This method of creating an Image Panel was from a thread on stackoverflow
The original creator's username was Maxim Shoustin

*/
package View;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final Image MY_IMAGE;
    private final int MY_WIDTH;
    private final int MY_HEIGHT;

    public ImagePanel(final Image theImage) {
        MY_IMAGE = theImage;
        MY_WIDTH = theImage.getWidth(this)/ 2;
        MY_HEIGHT = theImage.getHeight(this) /2;
        setLayout(new GridLayout());
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (MY_IMAGE != null) {
            g.drawImage(MY_IMAGE, this.getParent().getWidth() / 2 - MY_WIDTH, this.getParent().getHeight() / 2 - MY_HEIGHT, this);
        }
    }
}
