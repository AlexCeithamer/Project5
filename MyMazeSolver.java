
import java.util.*;

/**
 * Write a description of class MyMazeSolver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyMazeSolver extends MazeSolver {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private static final int NUM_DIRECTIONS = 4;
    private int currentWeight = 0;
    private int turn = 0;
    
    //The 3 states of the maze solver (what it just did)
    private boolean justMovedForward = true;
    private boolean lookingLeft = false;
    private boolean turnedRight = false;
    //keep track of direction we are facing
    private int direction = NORTH;
    
    //2d arrayList to keep track of the maze
    private static List<List<Integer> > arrayList = new ArrayList<List<Integer> >();
    //arrayList.get(1).add(0, 13); changes values
    //arrayList.add(new ArrayList<Integer>()); adds column
    //keep track of starting positions
    private int currX = 0;
    private int currY = 0;
    private int numRows = 0;
    private int numCols = 0;
    private boolean isDone = false;
    
    public static void main(String[] args) {
        
        TheAmazingRace.play(new MazeSolver[]{ new MyMazeSolver(),
                                              new MazeSolverZombie() });
        
    }
    
    
    public void reset() {
        currentWeight = 0;
        arrayList.clear();
        //initialize arrayList
        arrayList.add(new ArrayList<Integer>());
        arrayList.get(0).add(0, -1);
        numRows++;
        numCols++;
    }
    
    public String getName() {
        return "ALEXandBRETT";
    }
    
    /**
     * move forward method for the arrayList. First we change variables for the
     * takeTurn() method. After that, depending on the direction we face, we update
     * currY and currX to keep track of our relative position. If our current position
     * goes out of bounds of our ArrayList "map", we add a row or column to it (basically
     * mapping out the maze.).
     * 
     * @author Alex Ceithamer
     */
    private void moveForward() {
        justMovedForward = true;
        lookingLeft = false;
        turnedRight = false;
        //markArray();
        if (direction == NORTH) {
            currY--;
            if (currY < 0) {
                addRowToTop();
                currY++;
            }
            //row--;
        }
        else if (direction == EAST) {
            currX++;
            if (currX >= arrayList.get(0).size()) {
                addColToBack();
            }
            //col++;
        }
        else if (direction == SOUTH) {
            currY++;
            if (currY > arrayList.size() - 1) {
                addRowToBottom();
            }
            //row++;
        }
        else if (direction == WEST) {
            currX--;
            if (currX < 0) {
                addColToFront();
                currX++;
            }
            //col++;
        }
        markArray();
    }
    
    
    private void turnLeft() {
        direction = direction - 1;
        //need to check if a wall is to the left once we move forward
        lookingLeft = true;
        justMovedForward = false;
        turnedRight = false;
        if (direction < 0) {
            direction = WEST;
        }
    }
    private void turnRight() {
        direction = direction + 1;
        justMovedForward = false;
        lookingLeft = false;
        turnedRight = true;
        if (direction > WEST) {
            direction = NORTH;
        }
    }
    
    
    private boolean pickupItems() {
        if (isItemAvailableToPickUp()) {
            int itemWeight = getWeightOfItem();
            int newWeight;
            newWeight = currentWeight + itemWeight;
            if (newWeight <= KNAPSACK_CAPACITY) {
                if (itemWeight < KNAPSACK_CAPACITY - 1) {
                    currentWeight = currentWeight + itemWeight;
                    return true;
                } 
            }
        }
        return false;
    }
    
    public int takeTurn() {
        if (isDone) {
            return GIVE_UP;
        }
        if (pickupItems()) {
            return PICK_UP_ITEM;
        }
        if (justMovedForward) {
            turnLeft();
            return TURN_LEFT;
        }
        if (lookingLeft) {
            if (!isFacingWall()) {
                moveForward();
                return MOVE_FORWARD;
            }
            else {
                turnRight();
                return TURN_RIGHT;
            }
        }
        if (turnedRight) {
            if (!isFacingWall()) {
                moveForward();
                return MOVE_FORWARD;
            }
            else {
                turnRight();
                return TURN_RIGHT;
            }
        }
        return GIVE_UP;
    }
    
    /**
     * Marks our arrayList with a 1 to indicate where our mazeSolver has been.
     * If we are standing over the starting position, and our arrayList doesn't contain
     * any 0, and the arrayList is bigger than 2x2 grid, we set containsZero to false
     * to quit the program in takeTurn()
     * 
     * @author Alex Ceithamer
     */
    private void markArray() {
        boolean containsZero = true;
        //should set the current position of the mazeSolver to 1 in our array
        if (arrayList.get(currY).get(currX) != -1 ) {
            arrayList.get(currY).set(currX, 1);
        }
        else {
            //
            for (int i = 0; i < arrayList.size(); ++i) {
                if (arrayList.contains(0)) {
                    //this should exit the loop
                    i = arrayList.size();
                } 
                else if (i == arrayList.size() - 1 && 
                            arrayList.size() > 2 &&
                            arrayList.get(0).size() > 2) {
                    containsZero = false;
                }
            
            }
        }
        
        if (!containsZero) {
            isDone = true;
        }
        
    }
    
    /**
     * Adds column to the front of the 2d arrayList, incrementing numCols variable too.
     * 
     * @author Alex Ceithamer
     */
    private void addColToFront() {
        //add 0 to front of inner arrayLists
        numCols++;
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).add(0, 0);
        }
        
    }
    
    /**
     * Adds Column to the end of the 2d arrayList, incrementing numCols
     * 
     * @author Alex Ceithamer
     */
    private void addColToBack() {
        //extend length of inner ArrayList
        numCols++;
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).add(0);
        }
        
    }
    
    /**
     * adds another arrayList to our 2d ArrayList at index 0, then fills it with 0's
     * 
     * @author Alex Ceithamer
     */
    private void addRowToTop() {
        //add 0 to front of outer arrayList
        arrayList.add(0, new ArrayList<Integer>());
        numRows++;
        //populate the row
        while (numCols > arrayList.get(0).size()) {
            arrayList.get(0).add(0);
        }
        
    }
    
    /**
     * adds another arrayList to the end of our 2d arrayList, then fills it with 0's
     * 
     * @author Alex Ceithamer
     */
    private void addRowToBottom() {
        //extend outer arrayList
        arrayList.add(new ArrayList<Integer>());
        numRows++;
        //populate row
        while (numCols > arrayList.get(numRows - 1).size()) {
            arrayList.get(numRows - 1).add(0);
        }
        
    }
    
    
}
