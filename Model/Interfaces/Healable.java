package Model.Interfaces;

import java.util.Random;

public interface Healable {

    //This form of heal will be used by potions and ultimate skills
    default String heal(int myMax, int myMin){
        return null;
    }
    //This form of heal will be used by monsters that regenerate health automatically.
    default String heal(){
        return null;
    }
}