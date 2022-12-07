import java.util.Random;

// This code is for demo purposes only. It doesn't
// satisfy the formatting guidelines. 
public class MazeSolverZombie extends MazeSolver { 
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private int currentWeight = 0;
    private int turn = 0;
    private Random rand = new Random();
    
    public int takeTurn() {
        if (isItemAvailableToPickUp()) {
            int itemWeight = getWeightOfItem();
            int newWeight;
            newWeight = currentWeight + itemWeight;
            if (newWeight <= KNAPSACK_CAPACITY) {
                if (itemWeight < KNAPSACK_CAPACITY - 1) {
                    currentWeight = currentWeight + itemWeight;
                    return PICK_UP_ITEM;
                }
            }
        }
        if (!isFacingWall() && !isFacingOpponent()) {
            turn = 0;
            return MOVE_FORWARD;
        }
        else {
            if (turn == 0) {
                turn = rand.nextInt(2) - rand.nextInt(2);
                while (turn == 0) {
                    turn = rand.nextInt(2) - rand.nextInt(2);
                }
            }
            if (turn < 0) {
                return TURN_LEFT;
            }
            else {
                return TURN_RIGHT;
            }
        }
    }
    
    public void reset() {
        currentWeight = 0;
    }

    public String getName() {
        return "Zombie";
    }
    
    
    
    
    
    
    
    public static void main(String[] args) {
        TheAmazingRace.play(new MazeSolver[]{ new MazeSolverZombie() });
    }

}

