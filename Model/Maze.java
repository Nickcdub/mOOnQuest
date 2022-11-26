package Model;

import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.*;

import static Model.CharacterConstants.MonsterType.*;

public class Maze {
    //We want grid to be an ArrayList so that we can take advantage of its dynamic methods.
    private final List<Region> myGrid;
    private final int myRows;
    private final int myCols;
    private final Stack<Region> myBreadCrumbs;

    private final Hero myHero;
    private final int[] myHeroLocation;

    private float myOgreChance;
    private float myWolfChance;
    private float myGoblinChance;

    private Monster myEnemy;
    private Guardian myBoss;



    //The constructor will:
    // - Know how many rows and columns it has
    // - Initialize a grid for the maze to be generated in
    // - Call the initialize method to fill the grid with region objects
    // - Call the generate method to march through region walls and form a maze


    public Maze(final int theSize, final int theDifficulty, Hero hero) throws SQLException {
        myGrid = new ArrayList<>();

        myRows = theSize;
        myCols = theSize;
        myBreadCrumbs = new Stack<>();

        myHero = hero;
        myHeroLocation = new int[]{0,0};

        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        switch(theDifficulty){
            case 1 -> loadDifficulty(statement.executeQuery("SELECT * FROM difficulty_table WHERE DIFFICULTY = 1 "));
            case 2 -> loadDifficulty(statement.executeQuery("SELECT * FROM difficulty_table WHERE DIFFICULTY = 2 "));
            case 3 -> loadDifficulty(statement.executeQuery("SELECT * FROM difficulty_table WHERE DIFFICULTY = 3 "));
        }

        connection.close();

        initialize(myGrid);
        generate();
    }

    private void loadDifficulty(final ResultSet rs) throws SQLException {
        myOgreChance = rs.getFloat("OGRE");
        myWolfChance = rs.getFloat("DIREWOLF");
        myGoblinChance = rs.getFloat("GOBLIN");
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

    public void move(final String theDirection) throws SQLException {
        Region newRoom;

        switch (theDirection){
            case "EAST" ->{
                if(index(myHeroLocation[0],myHeroLocation[1]+1)==-1) throw new IndexOutOfBoundsException("Controller should not read in EAST if EAST is not an option, Location: "+(myHeroLocation[0])+","+(myHeroLocation[1]+1));
                myHeroLocation[1]++;
            }
            case "WEST" ->{
                if(index(myHeroLocation[0],myHeroLocation[1]-1)==-1) throw new IndexOutOfBoundsException("Controller should not read in WEST if WEST is not an option, Location: "+myHeroLocation[0]+","+(myHeroLocation[1]-1));
                myHeroLocation[1]--;
            }
            case "NORTH" ->{
                if(index(myHeroLocation[0]-1,myHeroLocation[1])==-1) throw new IndexOutOfBoundsException("Controller should not read in NORTH if NORTH is not an option, Location: "+(myHeroLocation[0]-1)+","+myHeroLocation[1]);
                myHeroLocation[0]--;
            }
            case "SOUTH" ->{
                if(index(myHeroLocation[0]+1,myHeroLocation[1])==-1) throw new IndexOutOfBoundsException("Controller should not read in SOUTH if SOUTH is not an option, Location: "+(myHeroLocation[0]+1)+","+myHeroLocation[1]);
                myHeroLocation[0]++;
            }
            default ->throw new IllegalArgumentException("There are 4 possible Directions: NORTH, SOUTH, EAST, WEST. Input: "+theDirection);
        }
        newRoom = getRegion(myHeroLocation[0],myHeroLocation[1]);
        newRoom.loot();
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

    public Monster getEnemy() {
        return myEnemy;
    }

    public Guardian getBoss() {
        return myBoss;
    }


    class Region {
        final int row, col;
        //Visited and Walls must be able to change as our generate method marches through regions
        public boolean visited;
        //make this private
        private final boolean[] walls;

        private boolean potion;
        private boolean trap;
        private boolean monster;

        private boolean myLoot;

        //Region will be private and should only be accessed through the maze.
        //This is necessary to allow the maze instance to own regions
        private Region(final int i, final int j) {
            row = i;
            col = j;
            //When regions are initialized, they are unrefined and have not yet been visited by the generator
            visited = false;
            //These booleans represent a wall on the left, right, top, bottom of a region respectively.
            walls = new boolean[]{true, true, true, true};
            myLoot = true;


            //generate potions, traps, and monsters
            potion = Math.random() < 0.75;
            trap = Math.random() < 0.1;
            monster = Math.random() < 0.25;

            //extend this to auto-fill the region with the item/trap/monster
            //if potion true then 95% chance to create health potion, 5% vision potion
            //if monster true then 50% weakest 30% next one and 20% the strongest
            //if trap true then create a trap
            //all will be stored in a hashmap with string and object

        }

        private void loot() throws SQLException {
            myLoot = false;

            if(potion) {
                myHero.getInventory().addItem(Math.random() < 0.95 ? new HealthPotion() : new VisionPotion());
                potion = false;
            }

            if(trap){
                myHero.trap(15);
                trap = false;
                if(myHero.getHealth()<0) return;
            }

            if(monster){
                monster = false;
                float type = (float) Math.random();
                if(type<myGoblinChance) myEnemy = new Monster(GOBLIN);
                else if(type<myGoblinChance+myWolfChance) myEnemy = new Monster(DIREWOLF);
                else myEnemy = new Monster(OGRE);
            }else{
                myEnemy = null;
            }

        }

        // Get methods to use upon entering region.
        public boolean getMonster() {
            return monster;
        }

        public boolean getItem() {
            return potion;
        }

        public boolean getTrap() {
            return trap;
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