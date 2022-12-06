package Model;

import Model.Maze.Region;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MazeTest {

    @Test
    @DisplayName("Test Top RemoveWalls")
    public void testTopWalls() {

        Region current = new Region(1,1);

        Region top = new Region(0,1);
        Region falseTop = new Region(0,2);

        //Top test
        Maze.removeWalls(current, top);
        assertArrayEquals(new boolean[]{true, true, false, true}, current.getWalls());

        Region finalCurrent3 = current;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Maze.removeWalls(finalCurrent3, falseTop));
        current = new Region(1, 1);

    }

    @Test
    @DisplayName("Test Left RemoveWalls")
    public void testLeftWalls() {
        Region current = new Region(1,1);
        Region left = new Region(1,0);
        Region falseLeft = new Region(0,0);
        //Left test
        Maze.removeWalls(current, left);
        assertArrayEquals(new boolean[]{false, true, true, true}, current.getWalls());

        Region finalCurrent = current;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Maze.removeWalls(finalCurrent, falseLeft));
        current = new Region(1, 1);

    }
    @Test
    @DisplayName("Test Right RemoveWalls")
    public void testRightWalls() {
        Region current = new Region(1,1);
        Region right = new Region(1,2);
        Region falseRight = new Region(2,2);
        //Right test
        Maze.removeWalls(current, right);
        assertArrayEquals(new boolean[]{true, false, true, true}, current.getWalls());

        Region finalCurrent1 = current;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Maze.removeWalls(finalCurrent1, falseRight));
        current = new Region(1, 1);
    }
        @Test
        @DisplayName("Test Bottom RemoveWalls")
        public void testBottomWalls() {
            Region current = new Region(1,1);
            Region bottom = new Region(2,1);
            Region falseBottom = new Region(2,0);
            //Bottom test
            Maze.removeWalls(current, bottom);
            assertArrayEquals(new boolean[]{true, true, true, false}, current.getWalls());

            Region finalCurrent2 = current;
            Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Maze.removeWalls(finalCurrent2, falseBottom));
            current = new Region(1, 1);
        }

       @Test
       @DisplayName("Test Index")
    public void testIndex(){
            Maze maze = new Maze(3,10);
            assertEquals(9,maze.index(0,9),"Index Test: 1");
            assertEquals(29,maze.index(2,9),"Index Test: 2");
            assertEquals(15,maze.index(1,5),"Index Test: 3");
            assertEquals(0,maze.index(0,0),"Index Test: 4");
            assertEquals(25,maze.index(2,5),"Index Test: 5");
            assertEquals(-1,maze.index(100,100),"OutOfBounds Test: 1");
            assertEquals(-1,maze.index(-1,-1),"OutOfBounds Test: 1");
            assertEquals(-1,maze.index(2,10),"OutOfBounds Test: 1");
            assertEquals(-1,maze.index(3,9),"OutOfBounds Test: 1");

        }
        @Test
        @DisplayName("Test Random Neighbor")
        public void testRandomNeighbor(){
            Maze maze1 = new Maze(1,1);
            Maze maze2 = new Maze(3,3);

            assertEquals(null,maze1.getRegion(0,0).randomNeighbor(), "No Neighbors Test");

           //top neighbor
            maze2.getRegion(0,1).visited = true;
            //left and right neighbors
            maze2.getRegion(1,0).visited = true;
            maze2.getRegion(1,2).visited = true;
            //bottom neighbor
            maze2.getRegion(2,1).visited = true;

            assertEquals(null,maze2.getRegion(1,1).randomNeighbor(),"Visited Neighbors Test");
        }
    }