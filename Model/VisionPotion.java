package Model;

import Model.AbstractClasses.Item;

public class VisionPotion extends Item {
    public VisionPotion() {
        this.myPotionName = "Vision Potion";
    }
    //Reveal parts of the maze
    public String useEffect(Maze theMaze) {
        return theMaze.revealArea();
    }
}
