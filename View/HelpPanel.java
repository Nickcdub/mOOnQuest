package View;

import Controller.GameController;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {

    HelpPanel(){
        JPanel heroes = new JPanel();
        JPanel monsters = new JPanel();
        JPanel guardians = new JPanel();
        JPanel potions = new JPanel();
        JPanel combat = new JPanel();

        heroes.setBackground(Color.decode("#abc8f5"));
        monsters.setBackground(Color.decode("#fa8787"));
        guardians.setBackground(Color.decode("#d987fa"));
        potions.setBackground(Color.decode("#fad787"));
        combat.setBackground(Color.decode("#2a1c30"));

        //Hero Labels
        heroes.setLayout(new GridLayout(5,1));
        heroes.add(new JLabel("HEROES ABILITIES:"));
        heroes.add(new JLabel("      Map Icon: Has the image of the hero with a blue background."));
        heroes.add(new JLabel("      Knight: Knight delivers a mighty swing that deals up to 175 damage! However, it misses most of the time."));
        heroes.add(new JLabel("      Mender: The Mender heals a large amount of health! Has a 5% chance of failing"));
        heroes.add(new JLabel("      Assassin: Assassin makes swift strikes at the enemy! Chance of attacking multiple times, less than half the chance of a normal attack."));

        //Monster Labels
        monsters.setLayout(new GridLayout(6,1));
        monsters.add(new JLabel("MONSTERS AND TRAPS:"));
        monsters.add(new JLabel("       Map Icon: Depicted on the map as red."));
        monsters.add(new JLabel("       Trap: Exists in every maze, an unavoidable hazard."));
        monsters.add(new JLabel("       Goblin: Exists in every maze, the most basic monster."));
        monsters.add(new JLabel("       Direwolf: Exists in medium and hard mazes, a little slower than goblin, but deals more damage."));
        monsters.add(new JLabel("       Ogre: Exists only in hard mazes, slowest monster, but deals hefty damage."));

        //Guardian Labels
        guardians.setLayout(new GridLayout(6,1));
        guardians.add(new JLabel("       BOSS GUARDIAN ABILITIES:"));
        guardians.add(new JLabel("       Map Icon: Depicted on the map as purple"));
        guardians.add(new JLabel("       Cerberus: In addition to Cerberus' unique ability to block, it can also deal a devastating bite"));
        guardians.add(new JLabel("       Hydra: Regenerates up to 80 lost health."));
        guardians.add(new JLabel("       Red Dragon: Trades speed for strength, deal insane damage with a 0% chance to miss!"));
        guardians.add(new JLabel("       Tom: Deals massive damage and regenerates health."));

        //Potion Labels
        potions.setLayout(new GridLayout(4,1));
        potions.add(new JLabel("POTIONS:"));
        potions.add(new JLabel("        Map Icon: Depicted on the map as golden."));
        potions.add(new JLabel("        Health Potion: A red potion that regenerates moderate health."));
        potions.add(new JLabel("        Vision Potion: A blue potion that reveals all adjacent regions and their contents, works through walls."));

        //Combat Labels
        combat.setLayout(new GridLayout(6,1));
        JLabel one = new JLabel("COMBAT:");
        JLabel two = new JLabel("       Turns: Hero will never have less turns than the enemy, if a hero has double the speed than the enemy, they get double the turns");
        JLabel three = new JLabel("      Attack: The basic attack, varies on the hero/enemy.");
        JLabel four = new JLabel("      Special: Specific to the Hero and Guardian, higher miss chance than the basic attack");
        JLabel five = new JLabel("      Inventory: Accessible through the maze and combat. Access and usage does not cost a turn.");
        JLabel six = new JLabel("      Overload Damage: If the monster's health goes below zero, the hero has a chance to heal up to that much health. Enemy death does not interrupt Hero turn.");

       one.setForeground(Color.WHITE);
       two.setForeground(Color.WHITE);
       three.setForeground(Color.WHITE);
       four.setForeground(Color.WHITE);
       five.setForeground(Color.WHITE);
       six.setForeground(Color.WHITE);

        combat.add(one);
        combat.add(two);
        combat.add(three);
        combat.add(four);
        combat.add(five);
        combat.add(six);

        setLayout(new GridLayout(6,1));
        add(heroes);
        add(monsters);
        add(guardians);
        add(potions);
        add(combat);

        JButton back = new JButton("Back");
        back.addActionListener(new GameController.HelpInput());
        add(back);
    }
}
