package Model;

import Model.AbstractClasses.Guardian;
import Model.AbstractClasses.Hero;

import java.io.BufferedReader;
import java.sql.*;
import java.util.*;

import static Model.CharacterConstants.MonsterType.*;

public class Maze {
    //We want grid to be an ArrayList so that we can take advantage of its dynamic methods.
    private List<Region> myGrid;
    //We will keep track of rows and columns for modular use and have breadcrumbs for recursive backtracking.
    private int myRows;
    private int myCols;
    private Stack<Region> myBreadCrumbs;

    //We will always know the hero's location and have reference to our hero
    private static Hero myHero;
    private static int[] myLocation;
    //When a monster is in a region, it will have a chance to be 3 different monsters on hard
    private static float myOgreChance;
    private static float myWolfChance;
    private static float myGoblinChance;

    //We need some way of telling the controller that we have to fight something after move() and Region's loot()
    private static Monster myEnemy;
    private static Guardian myBoss;

    //We need to generate guardians and were going to use the maze generator to help us.
    //We need a data structure to hold our guardian bosses and an int to maintain our place within the data structure.
    private static List<Guardian> myBossList;
    private static int myPlaceHolder;


    //The constructor will:
    // - Know how many rows and columns it has
    // - Initialize a grid for the maze to be generated in
    // - Call the initialize method to fill the grid with region objects
    // - Call the generate method to march through region walls and form a maze


    public Maze(final int theSize, final int theDifficulty, Hero theHero) throws SQLException {
        myGrid = new ArrayList<>();

        myRows = theSize;
        myCols = theSize;
        myBreadCrumbs = new Stack<>();

        myHero = theHero;
        myLocation = new int[]{0,0};

        myEnemy = null;
        myBoss = null;

        myBossList = new ArrayList<>();
        myBossList.add(new Cerberus());
        myBossList.add(new Hydra());
        myBossList.add(new RedDragon());
        myBossList.add(new Tom());
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
        initializeGrid(myGrid);
        getRegion(0,0).clearRoom();
        getRegion(0,0).myVisible = true;

        //Generate our maze with stationary bosses
        generateMaze();
    }

    private void loadDifficulty(final ResultSet theRS) throws SQLException {
        myOgreChance = theRS.getFloat("OGRE");
        myWolfChance = theRS.getFloat("DIREWOLF");
        myGoblinChance = theRS.getFloat("GOBLIN");
    }

    //This method will fill the myGrid with default regions
    private void initializeGrid(final List<Region> theGrid) {
        for (int i = 0; i < myRows; i++) {
            for (int j = 0; j < myCols; j++) {
                theGrid.add(new Region(i, j));
            }
        }
    }

    //This private method will be used by the generate method to remove walls between adjacent cells
    private static void removeWalls(final Region theCurrent, final Region theNext) {
        //Check if theNext is to the left of theCurrent
        if (theCurrent.COL - theNext.COL == 1 && theCurrent.ROW == theNext.ROW) {
            theCurrent.WALLS[0] = false;
            theNext.WALLS[1] = false;
        } else if (theCurrent.COL - theNext.COL == -1 && theCurrent.ROW == theNext.ROW) { //Check if theNext is to the right of theCurrent
            theNext.WALLS[0] = false;
            theCurrent.WALLS[1] = false;
        } else if (theCurrent.ROW - theNext.ROW == 1 && theCurrent.COL == theNext.COL) { //Check if theNext is above theCurrent
            theCurrent.WALLS[2] = false;
            theNext.WALLS[3] = false;
        } else if (theCurrent.ROW - theNext.ROW == -1 && theCurrent.COL == theNext.COL) { //Check if theNext is below theCurrent
            theNext.WALLS[2] = false;
            theCurrent.WALLS[3] = false;
        } else { //If this else is reached, the two Regions that were passed in are not adjacent
            throw new IndexOutOfBoundsException("Rows " + theCurrent.ROW + "," + theCurrent.COL + " and " + theNext.ROW + "," + theNext.COL + " must be theNext to each other!");
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
    private void generateMaze() {
        //Keep track of current and next region in the march, mark current as visited
        //Keep track of region that gets blocked so that we can save the final region in the generation.
        Region current = myGrid.get(0);
        Region next = current.randomNeighbor();
        Region blocked = null;

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
            //If we can, we should clear the room, so we don't encounter monsters or items and add a guardian.
            bossMarcher++;
            if(bossMarcher==Math.floor(myRows * myCols /4) && bossCount<3){
                bossCount++;
                current.clearRoom();
                current.myGuardian = true;
                bossMarcher=0;
            }

            //Oh no! we've hit a block, just backtrack to a time when we had neighbors
            //If we end up back at the beginning, then our maze is complete and the loop can end
            blocked = current;
            while (next == null && myBreadCrumbs.size() != 0) {
                current = myBreadCrumbs.pop();
                next = current.randomNeighbor();
            }

        }
        //We've reached the end, place our final boss
        assert blocked != null;
        blocked.clearRoom();
        blocked.myGuardian = true;
    }

    //We need this to tell controller if a certain direction is possible
    public String getPath(){
        boolean[] walls = getRegion(myLocation[0], myLocation[1]).WALLS;
        StringBuilder st = new StringBuilder();
        //Is West Possible?
        if(!walls[0]) st.append(" WEST ");
        if(!walls[1]) st.append(" EAST ");
        if(!walls[2]) st.append(" NORTH ");
        if(!walls[3]) st.append(" SOUTH ");
        return st.toString();
    }

    //Move the hero in a direction, DO NOT ASSUME THAT INPUT IS CORRECT!
    public String move(final String theDirection) throws SQLException {
        Region newRoom;

        //Update location
        switch (theDirection){
            case "EAST" ->{
                if(index(myLocation[0], myLocation[1]+1)==-1) throw new IndexOutOfBoundsException("Controller should not read in EAST if EAST is not an option, Location: "+(myLocation[0])+","+(myLocation[1]+1));
                myLocation[1]++;
            }
            case "WEST" ->{
                if(index(myLocation[0], myLocation[1]-1)==-1) throw new IndexOutOfBoundsException("Controller should not read in WEST if WEST is not an option, Location: "+ myLocation[0]+","+(myLocation[1]-1));
                myLocation[1]--;
            }
            case "NORTH" ->{
                if(index(myLocation[0]-1, myLocation[1])==-1) throw new IndexOutOfBoundsException("Controller should not read in NORTH if NORTH is not an option, Location: "+(myLocation[0]-1)+","+ myLocation[1]);
                myLocation[0]--;
            }
            case "SOUTH" ->{
                if(index(myLocation[0]+1, myLocation[1])==-1) throw new IndexOutOfBoundsException("Controller should not read in SOUTH if SOUTH is not an option, Location: "+(myLocation[0]+1)+","+ myLocation[1]);
                myLocation[0]++;
            }
            default ->throw new IllegalArgumentException("There are 4 possible Directions: NORTH, SOUTH, EAST, WEST. Input: "+theDirection);
        }
        //check if new room has stuff
        newRoom = getRegion(myLocation[0], myLocation[1]);
        getRegion(myLocation[0], myLocation[1]).myVisible = true;
        if(!newRoom.hasLoot()) {
            myEnemy = null;
            myBoss = null;
        }else return newRoom.loot();
        return "Error! this should not be reached";
    }

    /*
    *
    * Get Region Info:
    * We don't want anyone touching regions important data, but it's okay for Controller to ask for some things...
    *
     */

    private Region getRegion(final int theRow, final int theColumn) {
        return myGrid.get(index(theRow, theColumn));
    }

    public boolean[] getRegionWalls(final int theRow, final int theCol) {
        return getRegion(theRow, theCol).WALLS;
    }

    public boolean getRegionGuardian(final int theRow, final int theCol){
        return getRegion(theRow, theCol).myGuardian;
    }
    public boolean getRegionMonster(final int theRow, final int theCol){
        return getRegion(theRow,theCol).myMonster;
    }

    public boolean getRegionTrap(final int theRow, final int theCol){
        return getRegion(theRow,theCol).myTrap;
    }

    public boolean getRegionPotion(final int theRow, final int theCol){
        return getRegion(theRow,theCol).myPotion;
    }

    public boolean getRegionVisibility(final int theRow, final int theCol) {
        return getRegion(theRow,theCol).myVisible ;
    }

    public int getRows() {
        return myRows;
    }

    public int getColumns() {
        return myCols;
    }

    public int[] getHeroLocation(){
        return myLocation;
    }

    public Monster getEnemy() {
        return myEnemy;
    }

    //fast way to remove enemy
    public Monster popEnemy(){
        Monster enemy = myEnemy;
        myEnemy = null;
        return enemy;
    }

    public Guardian getBoss() {
        return myBoss;
    }

    //fast way to remove boss
    public Guardian popBoss(){
        Guardian boss = myBoss;
        myBoss = null;
        return boss;
    }

    //For vision potion to reveal the adjacent areas
    String revealArea(){
        int areaCount = 0;
        for (int i = getHeroLocation()[0]-1; i <= getHeroLocation()[0]+1 ; i++) {
            for (int j = getHeroLocation()[1]-1; j <= getHeroLocation()[1]+1 ; j++) {
                if(index(i,j)!=-1 &&  getRegion(i,j).myVisible == false){
                    getRegion(i,j).myVisible = true;
                    areaCount++;
                }
            }
        }
        return "The Vision Potion revealed "+areaCount+" areas.";
    }


    class Region {
        final private int ROW, COL;
        //Visited and Walls must be able to change as our generate method marches through regions
        private boolean myVisited;

        //determines whether this region is visible or not to the hero.
        private boolean myVisible;
        //make this private
        private final boolean[] WALLS;

        private boolean myPotion;
        private boolean myTrap;
        private boolean myMonster;
        private boolean myGuardian;

        private boolean myLoot;

        //Region will be private and should only be accessed through the maze.
        //This is necessary to allow the maze instance to own regions
        private Region(final int theRow, final int theCol) {
            ROW = theRow;
            COL = theCol;
            //When regions are initialized, they are unrefined and have not yet been visited by the generator
            myVisited = false;
            //These booleans represent a wall on the left, right, top, bottom of a region respectively.
            WALLS = new boolean[]{true, true, true, true};
            myLoot = true;

            myVisible = false;

            //generate potions, traps, and monsters
            myPotion = Math.random() < 0.8;
            myTrap = Math.random() < 0.1;
            myMonster = Math.random() < 0.25;

            //extend this to autofill the region with the item/trap/monster
            //if potion true then 95% chance to create health potion, 5% vision potion
            //if monster true then 50% weakest 30% next one and 20% the strongest
            //if trap true then create a trap
            //all will be stored in a hashmap with string and object

        }

        private boolean hasLoot(){
            return myLoot;
        }

        //Needed for entrance and boss rooms
        private void clearRoom(){
            myPotion = false;
            myTrap = false;
            myMonster = false;
        }

        //Return a string to give move to give controller to give view :)
        private String loot() throws SQLException {
            //Build string why checking for items and enemies
            StringBuilder sb = new StringBuilder();
            myLoot = false;

            if(myGuardian){
                myGuardian = false;
                myBoss = myBossList.get(myPlaceHolder);
                myPlaceHolder++;
            }

            if(myPotion) {
                sb.append(myHero.getInventory().addItem(Math.random() < 0.85 ? new HealthPotion() : new VisionPotion())+"\n");
                myPotion = false;
            }

            //Special case: the trap could kill the hero, if it does, return and let controller deal with it
            if(myTrap){
                sb.append(myHero.trap(20)+"\n");
                myTrap = false;
                if(myHero.getHealth()<0) return sb.toString();
            }

            if(myMonster){
                myMonster = false;
                float type = (float) Math.random();
                if(type<myGoblinChance) myEnemy = new Monster(GOBLIN);
                else if(type<myGoblinChance+myWolfChance) myEnemy = new Monster(DIREWOLF);
                else if(type>1-myOgreChance) myEnemy = new Monster(OGRE);
            }
            return sb.toString();
        }

        //This private method: randomNeighbor method will give generator a random region to mark as next
        private Region randomNeighbor() {
            var neighbors = new ArrayList<Region>();
            //Use index method in Model.Maze to locate adjacent regions

            //Each if statement will check if the index exists && whether it has already been visited or not.
            if (index(ROW, COL - 1) != -1 && !myGrid.get(index(ROW, COL - 1)).myVisited) {//Check left neighbor
                Region left = myGrid.get(index(ROW, COL - 1));
                neighbors.add(left);
            }
            if (index(ROW, COL + 1) != -1 && !myGrid.get(index(ROW, COL + 1)).myVisited) {//Check right neighbor
                Region right = myGrid.get(index(ROW, COL + 1));
                neighbors.add(right);
            }
            if (index(ROW - 1, COL) != -1 && !myGrid.get(index(ROW - 1, COL)).myVisited) {//Check neighbor above
                Region top = myGrid.get(index(ROW - 1, COL));
                neighbors.add(top);
            }
            if (index(ROW + 1, COL) != -1 && !myGrid.get(index(ROW + 1, COL)).myVisited) {//Check neighbor below
                Region bottom = myGrid.get(index(ROW + 1, COL));
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