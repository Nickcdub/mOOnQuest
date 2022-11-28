package Model.Interfaces;

public interface Healable {

    default String heal(int myMax, int myMin) {
        return null;
    }
    default String heal(){
        return null;
    }
}
