/**
 * Model for Conway's Game of Life.
 *
 * This class stores the society in a 2D boolean array.
 * true  = live cell
 * false = empty location
 *
 * IMPORTANT FOR THIS PROJECT:
 * The board does NOT wrap around. Any location outside the array is simply
 * ignored when counting neighbors.
 */
public class GameOfLife {

    private boolean[][] society;

    /**
     * Creates an empty society with the requested number of rows and columns.
     */
    public GameOfLife(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows and columns must be positive.");
        }

        society = new boolean[rows][cols];
    }

    /** Returns the number of rows in the society. */
    public int numberOfRows() {
        return society.length;
    }

    /** Returns the number of columns in the society. */
    public int numberOfColumns() {
        return society[0].length;
    }

    /** Makes the location at row, col alive. */
    public void growCellAt(int row, int col) {
        society[row][col] = true;
    }

    /** Makes the location at row, col dead. */
    public void killCellAt(int row, int col) {
        society[row][col] = false;
    }

    /** Returns true if the location contains a live cell. */
    public boolean cellAt(int row, int col) {
        return society[row][col];
    }

    /** Makes every location in the society dead. */
    public void clear() {
        society = new boolean[society.length][society[0].length];
    }

    /**
     * Counts the live neighbors surrounding one location.
     *
     * A location can have at most eight neighbors. Locations outside the
     * board DO NOT wrap around to the other side.
     *
     * TODO: Complete this method.
     */
    public int neighborCount(int row, int col) {
        int num = 0;
        if(row != 0)
        {
            if(society[row-1][col])
            {
                num++;
            }   
            if(col != 0)
            {
                if(society[row-1][col-1])
                {
                    num++;
                }   
            }
            if(col != society[0].length - 1)
            {
                if(society[row-1][col+1])
                {
                    num++;
                }
            }
        }
        if(row != society.length - 1)
        {
            if(society[row+1][col])
            {
                num++;
            }
            if(col != 0)
            {
                if(society[row+1][col-1])
                {
                    num++;
                }   
            }
            if(col != society[0].length - 1)
            {
                if(society[row+1][col+1])
                {
                    num++;
                }
            }
        }
        if(col != 0)
        {
            if(society[row][col-1])
            {
                num++;
            }   
        }
        if(col != society[0].length - 1)
        {
            if(society[row][col+1])
            {
                num++;
            }   
        }
        return num;
    }

    /**
     * Advances the entire society by one generation.
     *
     * Rules:
     * 1. A dead cell with exactly 3 live neighbors becomes alive.
     * 2. A live cell with 2 or 3 live neighbors survives.
     * 3. A live cell with fewer than 2 neighbors dies from isolation.
     * 4. A live cell with more than 3 neighbors dies from overpopulation.
     *
     */
    public void update() {
        // TODO: Create a SECOND 2D boolean array for the next generation.
        //
        // IMPORTANT:
        // Do not change society while you are still using it to calculate
        // neighbors. Every cell in the new generation must be based on the
        // same old generation.
        boolean[][] nextGen = new boolean[society.length][society[0].length];

        for(int r = 0; r < society.length; r++)
        {
            for(int c = 0; c < society[0].length; c++)
            {
                if(neighborCount(r, c) == 3 || (neighborCount(r, c) == 2 && society[r][c]))
                {
                    nextGen[r][c] = true;
                }
                else if(neighborCount(r, c) > 3 || neighborCount(r, c) < 2)
                {
                    nextGen[r][c] = false;
                }
                else
                {
                    nextGen[r][c] = society[r][c];
                }
            }
        }

        society = nextGen;
    }

    /**
     * Returns a text version of the board.
     * O = live cell
     * . = dead cell
     *
     * TODO: Complete this method.
     */
    @Override
    public String toString() {
        // TODO: Use nested loops to build one String containing the board.
        //       Add a newline after every row.
        String board = "";
        for(int r = 0; r < society.length; r++)
        {
            for(int c = 0; c < society[0].length; c++)
            {
                if(society[r][c])
                {
                    board += "◼";
                }
                else
                {
                    board += "☐";
                }
            }
            board += "\n";
        }
        return board;
    }
}
