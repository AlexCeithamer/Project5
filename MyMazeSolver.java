
import java.util.*;

/**
 * Solves mazes, avoids opponents. Uses a 2d ArrayList to map out the maze to ensure
 * each spot of the maze has been visited. Picks up items along the way
 *
 * @author Alex Ceithamer
 * @version 12/11/2022
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
    
    //keep track of starting positions
    private int currX = 0;
    private int currY = 0;
    private int numRows = 0;
    private int numCols = 0;
    private boolean isDone = false;
    private boolean hasDoneNothing = false;
    private boolean evading = false;
    
    /**
     * Starts the mazeSolver game
     * 
     * @author Alex Ceithamer
     */
    public static void main(String[] args) {
        
        TheAmazingRace.play(new MazeSolver[]{ new MyMazeSolver(),
                                              new MazeSolverZombie() });
        
    }
    
    /**
     * resets currentWeight and our 2d arrayList, then initializes it for the next game
     * by setting the starting position at row 0 col 0.
     * 
     * @author Alex Ceithamer
     */
    public void reset() {
        currentWeight = 0;
        arrayList.clear();
        //initialize arrayList
        arrayList.add(new ArrayList<Integer>());
        arrayList.get(0).add(0, -1);
        numRows++;
        numCols++;
    }
    
    /**
     * sets the name of MyMazeSolver
     * 
     * @author Alex Ceithamer
     */
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
        if (direction == NORTH) {
            currY--;
            if (currY < 0) {
                addRowToTop();
                currY++;
            }
        }
        else if (direction == EAST) {
            currX++;
            if (currX >= arrayList.get(0).size()) {
                addColToBack();
            }
        }
        else if (direction == SOUTH) {
            currY++;
            if (currY > arrayList.size() - 1) {
                addRowToBottom();
            }
        }
        else if (direction == WEST) {
            currX--;
            if (currX < 0) {
                addColToFront();
                currX++;
            }
        }
        markArray();
    }
    
    /**
     * sets lookingLeft to true to use in the takeTurn() method, updates direction
     * 
     * @author Alex Ceithamer
     */
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
    
    /**
     * sets turnedRight to true to use in the takeTurn() method, updates direction
     * 
     * @author Alex Ceithamer
     */
    private void turnRight() {
        direction = direction + 1;
        justMovedForward = false;
        lookingLeft = false;
        turnedRight = true;
        if (direction > WEST) {
            direction = NORTH;
        }
    }
    
    /**
     * picks up items as long as we have the capacity to do so and it isn't a bomb
     * so we maximize value of our knapsack
     * 
     * @author Alex Ceithamer
     */
    private boolean pickupItems() {
        if (isItemAvailableToPickUp()) {
            int itemWeight = getWeightOfItem();
            int newWeight;
            newWeight = currentWeight + itemWeight;
            if (newWeight <= KNAPSACK_CAPACITY) {
                //avoids picking up bombs to maximize value of knapsack
                if (itemWeight < KNAPSACK_CAPACITY - 1 && 
                                    itemWeight != Integer.MAX_VALUE - 1) {
                    currentWeight = currentWeight + itemWeight;
                    return true;
                } 
            }
        }
        return false;
    }
    
    /**
     * maing driver for the mazeSolver. If we have been through the entire maze AND we
     * are back at the starting position, we terminate. We pick up items if we are standing
     * over an item. Move forward/look left/turn right otherwise.
     * 
     * @author Alex Ceithamer
     */
    public int takeTurn() {
        if (isDone) {
            return GIVE_UP;
        }
        if (isOpponentNearby() || hasDoneNothing == false){
            return evade();
        }
        else if (!isOpponentNearby() && evading) {
            evading = false;
            return TURN_RIGHT;
        }
        if (pickupItems()) {
            return PICK_UP_ITEM;
        }
        if (justMovedForward) {
            turnLeft();
            return TURN_LEFT;
        }
        if (lookingLeft) {
            return notFacingWall();
        }
        if (turnedRight) {
            return notFacingWall();
        }
        return GIVE_UP;
    }
    
    /**
     * If not facing a wall, move forward, otherwise turn right. This is a helper method
     * to reduce code repetition.
     * 
     * @author Alex Ceithamer
     */
    private int notFacingWall() {
        if (!isFacingWall()) {
                moveForward();
                return MOVE_FORWARD;
            }
            else {
                turnRight();
                return TURN_RIGHT;
            }
    }
    
    /**
     * Attempts to evade opponents. First attempts to wait for 1 turn. if it has waited
     * and an opponent is still nearby, it will turn if facing the opponent, or 
     * move if not facing the opponent.
     */
    private int evade() {
        if (!hasDoneNothing && !isOpponentAdjacent()) {
            hasDoneNothing = true;
            return DO_NOTHING;
        }
        hasDoneNothing = false;
        if (isFacingOpponent()) {
            evading = true;
            return TURN_RIGHT;
            
        }
        else if (evading && !isFacingOpponent() && isOpponentAdjacent()) {
            return notFacingWall();
        }
        evading = false;
        return TURN_RIGHT;
        
        
    }
    
    /**
     * Change arrayList elements to indicate where our mazeSolver has been. (with a 1)
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
                            arrayList.size() > 3 &&
                            arrayList.get(0).size() > 3) {
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
