
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
    
    private void turnLeft() {
        direction = direction - 1;
        if (direction < 0) {
            direction = WEST;
        }
    }
    private void turnRight() {
        direction = direction + 1;
        if (direction > WEST) {
            direction = NORTH;
        }
    }
    private void moveForward() {
        if (direction == NORTH) {
            row--;
        }
        else if (direction == EAST) {
            col++;
        }
        else if (direction == SOUTH) {
            row++;
        }
        else if (direction == WEST) {
            col++;
        }
    }
    
    public int takeTurn() {
        if (justMovedForward) {
            //need to check if a wall is to the left once we move forward
            lookingLeft = true;
            justMovedForward = false;
            turnedRight = false;
            //this is 1 turn
            return TURN_LEFT;
        }
        if (lookingLeft) {
            if (!isFacingWall()) {
                justMovedForward = true;
                lookingLeft = false;
                turnedRight = false;
                return MOVE_FORWARD;
            }
            else {
                justMovedForward = false;
                lookingLeft = false;
                turnedRight = true;
                return TURN_RIGHT;
            }
        }
        if (turnedRight) {
            if (!isFacingWall()) {
                justMovedForward = true;
                lookingLeft = false;
                turnedRight = false;
                return MOVE_FORWARD;
            }
            else {
                justMovedForward = false;
                lookingLeft = false;
                turnedRight = true;
                return TURN_RIGHT;
            }
        }
        return GIVE_UP;
    }
    
    
    public void reset() {
        currentWeight = 0;
    }
    
    public String getName() {
        return "Human";
    }
}
