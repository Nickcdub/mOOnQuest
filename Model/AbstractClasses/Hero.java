package models.AbstractClasses;

import models.AbstractClasses.SpecialCharacter;
import models.Interfaces.Blockable;
import models.Interfaces.Healable;
import models.Inventory;

public abstract class Hero extends SpecialCharacter implements Blockable, Healable {
    public Inventory inventory;

    public Inventory getInventoy() {
        return null;
    }
}