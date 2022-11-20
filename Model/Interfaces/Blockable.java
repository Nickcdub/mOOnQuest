package Model.Interfaces;

public interface Blockable {
    float myBlockChance = 0;
    
     default int block(int theDamage){
        return 0;
    }
}
