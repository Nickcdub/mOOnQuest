package Controller;

import Model.*;
import View.GameFrame;
import View.Map;

import java.sql.SQLException;

import static Model.CharacterConstants.MonsterType.*;

public class Main {

    public static void main(String[] args) throws SQLException{
       /* Knight knight = new Knight();
        Mender mender = new Mender();
        Assassin assassin = new Assassin();

        Monster ogre = new Monster(OGRE);
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


        Maze maze = new Maze(7,3,new Assassin());
        GameFrame gui = new GameFrame();
        gui.showMap(maze,400);


        //GameController controller = new GameController();
    }
}
