package Model.Interfaces;

public interface Blockable {
     default int block(int theDamage) {
        return 0;
    }
}
