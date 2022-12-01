package View;

import Controller.UserInput;
import Model.AbstractClasses.Character;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterPanel extends JPanel{
    private final Character CHARACTER;
    private final Color COLOR;
    private final Color BACKGROUND;
    private final int MY_WIDTH, MY_HEIGHT;
    private final JPanel PEDESTAL;

    public CharacterPanel(final Character theCharacter, final Color theColor, final Color theBackground, final int theWidth, final int theHeight) throws IOException {
        BACKGROUND = theBackground;
        CHARACTER = theCharacter;
        COLOR = theColor;
        MY_WIDTH = theWidth;
        MY_HEIGHT = theHeight;
        PEDESTAL = new JPanel();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(MY_WIDTH, MY_HEIGHT));
        image();
        pedestal();
    }

    private void pedestal(){
        PEDESTAL.setBackground(COLOR);
        PEDESTAL.setPreferredSize(new Dimension(MY_WIDTH,200));
        add(PEDESTAL, BorderLayout.SOUTH);
    }

    private void image() throws IOException {
        JPanel png = new JPanel();
         png.setBackground(BACKGROUND);

        BufferedImage myPicture = ImageIO.read(new File(CHARACTER.getMyName()+".png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        png.add(picLabel);
        add(png);
    }

    public void setSelection(){
        PEDESTAL.setLayout(new GridBagLayout());
        JButton select = new JButton(CHARACTER.getMyName());
        select.addActionListener(new UserInput());
        select.setForeground(BACKGROUND);
        select.setPreferredSize(new Dimension(MY_WIDTH,100));
        PEDESTAL.add(select);
    }
}
