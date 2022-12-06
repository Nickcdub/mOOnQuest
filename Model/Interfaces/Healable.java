package Model.Interfaces;

public interface Healable {

    default String heal(int theMax, int theMin) {
        return null;
    }
    default String heal(){
        return null;
    }
}
