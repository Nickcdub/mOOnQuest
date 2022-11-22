package Model;

import java.util.*;

public class Maze {
    //We want grid to be an ArrayList so that we can take advantage of its dynamic methods.
    private final List<Region> myGrid;
    private final int myRows;
    private final int myCols;
    private final Stack<Region> myBreadCrumbs;

    //The constructor will:
    // - Know how many rows and columns it has
    // - Initialize a grid for the maze to be generated in
    // - Call the initialize method to fill the grid with region objects
    // - Call the generate method to march through region walls and form a maze


    public Maze(final int theRows, final int theCols) {
        myGrid = new ArrayList<>();

        myRows = theRows;
        myCols = theCols;
        myBreadCrumbs = new Stack<>();

        initialize(myGrid);
        generate();
    }

    //This method will fill the myGrid with default regions
    private void initialize(List<Region> myGrid) {
        for (int i = 0; i < myRows; i++) {
            for (int j = 0; j < myCols; j++) {
                myGrid.add(new Region(i, j));
            }
        }
    }

    //This private method will be used by the generate method to remove walls between adjacent cells
    public static void removeWalls(final Region theCurrent, final Region theNext) {
        //Check if theNext is to the left of theCurrent
        if (theCurrent.col - theNext.col == 1 && theCurrent.row == theNext.row) {
            theCurrent.walls[0] = false;
            theNext.walls[1] = false;
        } else if (theCurrent.col - theNext.col == -1 && theCurrent.row == theNext.row) { //Check if theNext is to the right of theCurrent
            theNext.walls[0] = false;
            theCurrent.walls[1] = false;
        } else if (theCurrent.row - theNext.row == 1 && theCurrent.col == theNext.col) { //Check if theNext is above theCurrent
            theCurrent.walls[2] = false;
            theNext.walls[3] = false;
        } else if (theCurrent.row - theNext.row == -1 && theCurrent.col == theNext.col) { //Check if theNext is below theCurrent
            theNext.walls[2] = false;
            theCurrent.walls[3] = false;
        } else { //If this else is reached, the two Regions that were passed in are not adjacent
            throw new IndexOutOfBoundsException("Rows " + theCurrent.row + "," + theCurrent.col + " and " + theNext.row + "," + theNext.col + " must be theNext to each other!");
        }
    }

    //This private method will be used by randomNeighbor within Region in order to find index regions within grid
    private int index(final int theRowIndex, final int theColumnIndex) {
        //This will let the randomNeighbor method know if the desired index is out of bounds for the grid
        if (theRowIndex < 0 || theColumnIndex < 0 || theRowIndex > myRows - 1 || theColumnIndex > myCols - 1) return -1;
        //This method returns the algorithm column + (row * #of columns in a row)
        //Example: if we are in column 3 in row 1 of base 10 (#13), 13 = 3 + (1 * 10)
        return theColumnIndex + theRowIndex * myCols;
    }

    //This method will march through our region walls to carve a maze
    private void generate() {
        //Keep track of current and next region in the march, mark current as visited
        Region current = myGrid.get(0);
        Region next = current.randomNeighbor();
        current.visited = true;

        //This loop will march through the walls between current and next, mark next as visited,
        // lay breadCrumbs to record path, move current to next, and look at the next wall to march through.
        // If there are no more walls to march through (next == null), stop marching
        while (next != null) {
            next.visited = true;
            myBreadCrumbs.push(current);
            removeWalls(current, next);
            current = next;
            next = current.randomNeighbor();

            //Oh no! we've hit a block, just backtrack to a time when we had neighbors
            //I've we end up back at the beginning, then our maze is complete and the loop can end
            while (next == null && myBreadCrumbs.size() != 0) {
                current = myBreadCrumbs.pop();
                next = current.randomNeighbor();
            }

        }
    }

    Region getRegion(final int theRow, final int theColumn) {
        return myGrid.get(index(theRow, theColumn));
    }

    public boolean[] getRegionWalls(int row, int col) {
        return getRegion(row, col).getWalls();
    }

    public int getRows() {
        return myRows;
    }

    public int getColumns() {
        return myCols;
    }

    public List<Region> getGrid() {
        return myGrid;
    }

    //This data structure will hold Regions that will act as nodes to our grid
    class Region {
        final int row, col;
        //Visited and Walls must be able to change as our generate method marches through regions
        public boolean visited;
        //make this private
        private final boolean[] walls;
        private boolean potion;
        private boolean trap;
        private boolean monster;

        //Region will be private and should only be accessed through the maze.
        //This is necessary to allow the maze instance to own regions
        private Region(final int i, final int j) {
            row = i;
            col = j;
            //When regions are initialized, they are unrefined and have not yet been visited by the generator
            visited = false;
            //These booleans represent a wall on the left, right, top, bottom of a region respectively.
            walls = new boolean[]{true, true, true, true};

            //generate potions, traps, and monsters
            potion = Math.random() < 0.75;
            trap = Math.random() < 0.1;
            monster = Math.random() < 0.25;

            //extend this to auto-fill the region with the item/trap/monster
            //if potion true then 95% chance to create health potion, 5% vision potion
            //if monster true then 50% weakest 30% next one and 20% the strongest
            //if trap true then create a trap
            //all will be stored in a hashmap with string and object

            //initialise the hash map that contains the region content
            myRegionContent = new HashMap<String, Object>();
            myRegionContent.put("Potion", null);
            myRegionContent.put("Trap", null);
            myRegionContent.put("Monster", null);

            //check if potions, traps and monsters are to be added
            if(potion) {
                //health potions have 95% chance of spawning since they are more useful and the vision potion lasts for the entire run
                boolean potionType = Math.random() < 0.95;
                myRegionContent.put("Potion", potionType ? new HealthPotion() : new VisionPotion());
            }
            if(trap) {
                myRegionContent.put("Trap", Boolean.valueOf(true));
            }
            if(monster) {
                try {
                    double monsterType = Math.random();
                    if (monsterType > 0.7) { //ogre has 30% chance of spawning
                        myRegionContent.put("Monster", new Monster(MonsterType.OGRE));
                    } else if (monsterType < 0.7 && monsterType > 0.4) { //direwolf has 30% chance of spawning
                        myRegionContent.put("Monster", new Monster(MonsterType.DIREWOLF));
                    } else { //goblin has 40% chance of spawning
                        myRegionContent.put("Monster", new Monster(MonsterType.GOBLIN));
                    }
                }
                catch(SQLException e) {
                    System.out.println("Exception " + e);
                }
            }
        }

        // Get methods to use upon entering region.
        public Monster getMonster() {
            return (Monster)myRegionContent.get("Monster");
        }

        public Item getItem() {
            return (Item)myRegionContent.get("Potion");
        }

        public boolean getTrap() {
            return (Boolean)myRegionContent.get("Trap");
        }

        private boolean[] getWalls() {
            return walls;
        }

        //This private method: randomNeighbor method will give generator a random region to mark as next
        private Region randomNeighbor() {
            var neighbors = new ArrayList<Region>();
            //Use index method in Model.Maze to locate adjacent regions

            //Each if statement will check if the index exists && whether it has already been visited or not.
            if (index(row, col - 1) != -1 && !myGrid.get(index(row, col - 1)).visited) {//Check left neighbor
                Region left = myGrid.get(index(row, col - 1));
                neighbors.add(left);
            }
            if (index(row, col + 1) != -1 && !myGrid.get(index(row, col + 1)).visited) {//Check right neighbor
                Region right = myGrid.get(index(row, col + 1));
                neighbors.add(right);
            }
            if (index(row - 1, col) != -1 && !myGrid.get(index(row - 1, col)).visited) {//Check neighbor above
                Region top = myGrid.get(index(row - 1, col));
                neighbors.add(top);
            }
            if (index(row + 1, col) != -1 && !myGrid.get(index(row + 1, col)).visited) {//Check neighbor below
                Region bottom = myGrid.get(index(row + 1, col));
                neighbors.add(bottom);
            }
            if (neighbors.size() == 0) {//If none of the neighbors are suitable, return null to tell generate that we've hit a block
                return null;
            }

            //Return a random suitable neighbor from the neighbors arrayList
            return neighbors.get((int) Math.floor(Math.random() * neighbors.size()));
        }
    }
}