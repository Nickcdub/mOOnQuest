package View;

import Controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BattlePanel extends JPanel {

    BattlePanel(final CharacterPanel theHero, final CharacterPanel theDefender, final String theBackgroundFileName, final StringBuilder theBuilder) throws IOException {
        JPanel battle = new JPanel();
        battle.setLayout(new GridLayout());
        battle.setOpaque(false);


        //Left and right panels will hold our two combatants
        theHero.setOpaque(false);
        theDefender.setOpaque(false);
        theHero.setStats();
        theDefender.setStats();

        //This menu panel will hold our player's action buttons
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(4, 1));

        //Create buttons
        var attack = new JButton("Attack");
        var special = new JButton("Special");
        var inventory = new JButton("Inventory");
        var cheats = new JButton("Cheats");

        //add action listener
        attack.addActionListener(new GameController.BattleInput(theDefender.getCharacter()));
        special.addActionListener(new GameController.BattleInput(theDefender.getCharacter()));
        inventory.addActionListener(new GameController.BattleInput(theDefender.getCharacter()));
        cheats.addActionListener(new GameController.BattleInput(theDefender.getCharacter()));

        //Add buttons to menu
        menu.add(attack);
        menu.add(special);
        menu.add(inventory);
        menu.add(cheats);
        menu.setOpaque(false);


        //log will be a text panel that can scroll, this will hold our attack log
        JTextArea txt = new JTextArea();
        JScrollPane log = new JScrollPane(txt);
        log.setVerticalScrollBarPolicy(log.VERTICAL_SCROLLBAR_ALWAYS);
        log.setHorizontalScrollBarPolicy(log.HORIZONTAL_SCROLLBAR_ALWAYS);
        txt.setEditable(false);
        //Our txt resets whenever the panel is recalled, keep the old info of the previous text area
        txt.insert(theBuilder.toString(), 0);

        //This panel will take our action menu and our attack log and split it between one panel
        final JPanel centerUI = new JPanel();
        centerUI.setLayout(new GridLayout(2, 1));
        centerUI.add(menu);
        centerUI.add(log);
        centerUI.setOpaque(false);

        //This panel holds our background
        ImagePanel myPicture = new ImagePanel(ImageIO.read(new File(theBackgroundFileName)));

        //Add 3 main panels to battle panel
        battle.add(theHero);
        battle.add(centerUI);
        battle.add(theDefender);
        //Add battle panel onto background
        myPicture.setLayout(new GridLayout());
        myPicture.add(battle);
        //Add all contents onto class panel
        setLayout(new GridLayout());
        add(myPicture);
    }
}
