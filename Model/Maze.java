package Model;

import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;

import java.sql.*;
import java.util.*;

import static Model.CharacterConstants.MonsterType.*;

public class Maze {
    //We want grid to be an ArrayList so that we can take advantage of its dynamic methods.
    private final List<Region> myGrid;
    //We will keep track of rows and columns for modular use and have breadcrumbs for recursive backtracking.
    private final int myRows;
    private final int myCols;
    private final Stack<Region> myBreadCrumbs;

    //We will always know the hero's location and have reference to our hero
    private final Hero myHero;
    private final int[] myHeroLocation;
    //When a monster is in a region, it will have a chance to be 3 different monsters on hard
    private float myOgreChance;
    private float myWolfChance;
    private float myGoblinChance;
    //We need some way of telling the controller that we have to fight something after move() and Region's loot()
    private Monster myEnemy;
    private Guardian myBoss;

    //We need to generate guardians and were going to use the maze generator to help us.
    //We need a data structure to hold our guardian bosses and an int to maintain our place within the data structure.
    private final List<Guardian> myBosses;
    private int myPlaceHolder;


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

        myEnemy = null;
        myBoss = null;

        myBosses = new ArrayList<>();
        myBosses.add(new Cerberus());
        myBosses.add(new Hydra());
        myBosses.add(new RedDragon());
        myBosses.add(new Tom());
        myPlaceHolder = 0;

        String jdbcURL = "jdbc:sqlite:DungeonAdventure.sqlite";

        Connection connection = DriverManager.getConnection(jdbcURL);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        switch(theDifficulty){
            case 1 -> loadDifficulty(statement.executeQuery("SELECT * FROM spawn_table WHERE DIFFICULTY = 1 "));
            case 2 -> loadDifficulty(statement.executeQuery("SELECT * FROM spawn_table WHERE DIFFICULTY = 2 "));
            case 3 -> loadDifficulty(statement.executeQuery("SELECT * FROM spawn_table WHERE DIFFICULTY = 3 "));
        }

        connection.close();

        //Initialize regions and clear 0,0 (It will be our entrance)
        initialize(myGrid);
        getRegion(0,0).clearRoom();

        //Generate our maze with stationary bosses
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
        if (theCurrent.myCol - theNext.myCol == 1 && theCurrent.myRow == theNext.myRow) {
            theCurrent.myWalls[0] = false;
            theNext.myWalls[1] = false;
        } else if (theCurrent.myCol - theNext.myCol == -1 && theCurrent.myRow == theNext.myRow) { //Check if theNext is to the right of theCurrent
            theNext.myWalls[0] = false;
            theCurrent.myWalls[1] = false;
        } else if (theCurrent.myRow - theNext.myRow == 1 && theCurrent.myCol == theNext.myCol) { //Check if theNext is above theCurrent
            theCurrent.myWalls[2] = false;
            theNext.myWalls[3] = false;
        } else if (theCurrent.myRow - theNext.myRow == -1 && theCurrent.myCol == theNext.myCol) { //Check if theNext is below theCurrent
            theNext.myWalls[2] = false;
            theCurrent.myWalls[3] = false;
        } else { //If this else is reached, the two Regions that were passed in are not adjacent
            throw new IndexOutOfBoundsException("Rows " + theCurrent.myRow + "," + theCurrent.myCol + " and " + theNext.myRow + "," + theNext.myCol + " must be theNext to each other!");
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

    //This method will march through our region walls to carve a maze and plop bosses when not backtracking
    private void generate() {
        //Keep track of current and next region in the march, mark current as visited
        Region current = myGrid.get(0);
        Region next = current.randomNeighbor();
        current.myVisited = true;
        //We want our bossMarcher at 1 because our 0,0 region counts as a visited space.
        //bossCount needs to hold how many bosses our maze has so that when bossCount ==3, we can place the last one at the end.
        int bossMarcher = 1;
        int bossCount = 0;

        //This loop will march through the walls between current and next, mark next as visited,
        // lay breadCrumbs to record path, move current to next, and look at the next wall to march through.
        // If there are no more walls to march through (next == null), stop marching
        //This loop will also place a boss when bossMarcher == Math.floor(myRows*myCols/4), the final boss will be placed at the end.
        while (next != null) {
            next.myVisited = true;
            myBreadCrumbs.push(current);
            removeWalls(current, next);
            current = next;
            next = current.randomNeighbor();

            //Progress bossMarcher as we've entered a new room, check if we can put a guardian!
            //If we can, we should clear the room so we don't encounter monsters or items and add a guardian.
            bossMarcher++;
            if(bossMarcher==Math.floor(myRows*myCols/4) && bossCount<4){
                bossCount++;
                current.clearRoom();
                current.addGuardian();
            }

            //Oh no! we've hit a block, just backtrack to a time when we had neighbors
            //I've we end up back at the beginning, then our maze is complete and the loop can end
            while (next == null && myBreadCrumbs.size() != 0) {
                current = myBreadCrumbs.pop();
                next = current.randomNeighbor();
            }

        }
        //We've reached the end, place our final boss
        current.clearRoom();
        current.addGuardian();
    }
    public String getPath(){
        boolean[] walls = getRegion(myHeroLocation[0],myHeroLocation[1]).getWalls();
        StringBuilder st = new StringBuilder();
        //Is West Possible?
        if(!walls[0]) st.append(" WEST ");
        if(!walls[1]) st.append(" EAST ");
        if(!walls[2]) st.append(" NORTH ");
        if(!walls[3]) st.append(" SOUTH ");
        return st.toString();
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
        if(!newRoom.hasLoot()) {
            myEnemy = null;
            myBoss = null;
        }else newRoom.loot();
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
        final int myRow, myCol;
        //Visited and Walls must be able to change as our generate method marches through regions
        public boolean myVisited;
        //make this private
        private final boolean[] myWalls;

        private boolean myPotion;
        private boolean myTrap;
        private boolean myMonster;
        private boolean myGuardian;

        private boolean myLoot;

        //Region will be private and should only be accessed through the maze.
        //This is necessary to allow the maze instance to own regions
        private Region(final int i, final int j) {
            myRow = i;
            myCol = j;
            //When regions are initialized, they are unrefined and have not yet been visited by the generator
            myVisited = false;
            //These booleans represent a wall on the left, right, top, bottom of a region respectively.
            myWalls = new boolean[]{true, true, true, true};
            myLoot = true;


            //generate potions, traps, and monsters
            myPotion = Math.random() < 0.85;
            myTrap = Math.random() < 0.1;
            myMonster = Math.random() < 0.25;

            //extend this to auto-fill the region with the item/trap/monster
            //if potion true then 95% chance to create health potion, 5% vision potion
            //if monster true then 50% weakest 30% next one and 20% the strongest
            //if trap true then create a trap
            //all will be stored in a hashmap with string and object

        }

        private boolean hasLoot(){
            return myLoot;
        }

        private void clearRoom(){
            myPotion = false;
            myTrap = false;
            myMonster = false;
        }

        private void loot() throws SQLException {
            myLoot = false;

            if(myGuardian){
                myGuardian = false;
                myBoss = myBosses.get(myPlaceHolder);
                myPlaceHolder++;
            }else myBoss = null;

            if(myPotion) {
                myHero.getInventory().addItem(Math.random() < 0.95 ? new HealthPotion() : new VisionPotion());
                myPotion = false;
            }

            if(myTrap){
                myHero.trap(15);
                myTrap = false;
                if(myHero.getHealth()<0) return;
            }

            if(myMonster){
                myMonster = false;
                float type = (float) Math.random();
                if(type<myGoblinChance) myEnemy = new Monster(GOBLIN);
                else if(type<myGoblinChance+myWolfChance) myEnemy = new Monster(DIREWOLF);
                else if(type>1-myOgreChance) myEnemy = new Monster(OGRE);
            }else{
                myEnemy = null;
            }

        }

        private void addGuardian(){
            myGuardian = true;
        }

        // Get methods to use upon entering region.
        public boolean getMonster() {
            return myMonster;
        }

        public boolean getItem() {
            return myPotion;
        }

        public boolean getTrap() {
            return myTrap;
        }


        private boolean[] getWalls() {
            return myWalls;
        }

        //This private method: randomNeighbor method will give generator a random region to mark as next
        private Region randomNeighbor() {
            var neighbors = new ArrayList<Region>();
            //Use index method in Model.Maze to locate adjacent regions

            //Each if statement will check if the index exists && whether it has already been visited or not.
            if (index(myRow, myCol - 1) != -1 && !myGrid.get(index(myRow, myCol - 1)).myVisited) {//Check left neighbor
                Region left = myGrid.get(index(myRow, myCol - 1));
                neighbors.add(left);
            }
            if (index(myRow, myCol + 1) != -1 && !myGrid.get(index(myRow, myCol + 1)).myVisited) {//Check right neighbor
                Region right = myGrid.get(index(myRow, myCol + 1));
                neighbors.add(right);
            }
            if (index(myRow - 1, myCol) != -1 && !myGrid.get(index(myRow - 1, myCol)).myVisited) {//Check neighbor above
                Region top = myGrid.get(index(myRow - 1, myCol));
                neighbors.add(top);
            }
            if (index(myRow + 1, myCol) != -1 && !myGrid.get(index(myRow + 1, myCol)).myVisited) {//Check neighbor below
                Region bottom = myGrid.get(index(myRow + 1, myCol));
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