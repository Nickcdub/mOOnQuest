package Model.Interfaces;

public interface Healable {
    //maxHealth: keep track of health cap ,so we don't heal too much.
    int maxHealth = 0;

    default void Heal(int max, int min){

    }
}