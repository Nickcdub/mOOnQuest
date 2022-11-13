package Model;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Guardian;
import Model.Interfaces.Blockable;

public class Cerberus extends Guardian implements Blockable {
    public Cerberus() {

    }

    @Override
    public void Block() {
        System.out.println("Block");
    }

    @Override
    public void ultimate(Character defender) {

    }
}