package Controller;

import Model.*;
import View.GameFrame;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

import static Model.CharacterConstants.MonsterType.*;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Knight knight = new Knight();
        Mender mender = new Mender();
        Assassin assassin = new Assassin();

        knight.getInventory().addItem(new HealthPotion());
        knight.getInventory().addItem(new VisionPotion());
        /*Monster ogre = new Monster(OGRE);
        Monster goblin = new Monster(GOBLIN);
        Monster direwolf = new Monster(DIREWOLF);

        Tom tom = new Tom();
        RedDragon dragon = new RedDragon();
        Cerberus cerberus = new Cerberus();
        Hydra hydra = new Hydra();

        System.out.println(knight);
        System.out.println(mender);
        System.out.println(assassin);

        System.out.println("\n");
        System.out.println(ogre);
        System.out.println(goblin);
        System.out.println(direwolf);

        System.out.println("\n");
        System.out.println(tom);
        System.out.println(dragon);
        System.out.println(cerberus);
        System.out.println(hydra);*/


       /* Maze maze = new Maze(4,3,new Assassin());
        GameFrame gui = new GameFrame(800,800);
        gui.inventoryPanel(knight);*/

      //  System.out.println(UIManager.getSystemLookAndFeelClassName());
        //Maintain the same look and feel for guis cross-platform.
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

       //GameController controller = new GameController();
        GameFrame gui = new GameFrame(1200,1200);
        gui.winPanel();


        //GameFrame gui = new GameFrame(800,800);
        //gui.battlePanel(new Mender(),new Cerberus());
        //gui.test();

    }
}
