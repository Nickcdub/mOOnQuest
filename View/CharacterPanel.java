package View;

import Controller.GameController;
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
    private Color myBackground = null;
    private final int MY_WIDTH, MY_HEIGHT;
    private final JPanel PEDESTAL;

    public CharacterPanel(final Character theCharacter, final Color theColor, final Color theBackground, final int theWidth, final int theHeight) throws IOException {
        myBackground = theBackground;
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

    public CharacterPanel(final Character theCharacter, final Color theColor, final int theWidth, final int theHeight) throws IOException {
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
         if(myBackground!=null) png.setBackground(myBackground);

        BufferedImage myPicture = ImageIO.read(new File(CHARACTER.getMyName()+".png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        png.add(picLabel);
        add(png);
    }

    public void setSelection(){
        PEDESTAL.setLayout(new GridBagLayout());
        JButton select = new JButton(CHARACTER.getMyName());
        select.addActionListener(new GameController.HeroInput());
        select.setForeground(COLOR);
        select.setPreferredSize(new Dimension(MY_WIDTH,100));
        PEDESTAL.add(select);
    }

    public void setStats(){
        PEDESTAL.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(4,1));
        center.setPreferredSize(PEDESTAL.getPreferredSize());
        center.setOpaque(false);

        JLabel name = new JLabel(CHARACTER.getMyName(), JLabel.CENTER);
        JLabel health = new JLabel("HEALTH POINTS: "+CHARACTER.getHealth()+" / "+CHARACTER.getMaxHealth(), JLabel.CENTER);
        JLabel speed = new JLabel("ATTACK SPEED: "+ CHARACTER.getMyAttackSpeed(), JLabel.CENTER);
        JLabel damage = new JLabel("ATTACK DAMAGE: "+CHARACTER.getMyMinDmg()+" - "+CHARACTER.getMyMaxDmg(), JLabel.CENTER);

        name.setFont(new Font("Serif",Font.BOLD,24));
        health.setFont(new Font("Serif",Font.BOLD,18));
        speed.setFont(new Font("Serif",Font.BOLD,18));
        damage.setFont(new Font("Serif",Font.BOLD,18));


        center.add(name);
        center.add(health);
        center.add(speed);
        center.add(damage);
        PEDESTAL.add(center);
    }

    public Character getCharacter(){
        return CHARACTER;
    }
}
