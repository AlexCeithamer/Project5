
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
    private int currentWeight = 0;
    private int turn = 0;
    
    public int takeTurn() {
        return -1;
    }
    
    
    public void reset() {
        currentWeight = 0;
    }
    
    public String getName() {
        return "Human";
    }
}
