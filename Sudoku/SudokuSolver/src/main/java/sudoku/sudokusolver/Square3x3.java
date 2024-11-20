package sudoku.sudokusolver;

/**
 * Class Square3x3.
 * This class constructs a 3 by 3 square matrix.
 * @author Dor Elbaz
 * @version 28.11.21
*/
public class Square3x3
{   
    private int[][] square;
    private final int STANDARD_SIZE = 9, SQUARE_SIZE = 3;
    
    /**
     * Constructor for objects of class Square3X3. Constructs and initializes a 2-dimensional 
     * array of the size 3X3, with the values of –1 in each cell. 
     */
     public Square3x3()
    {
        square = new int[SQUARE_SIZE][SQUARE_SIZE];
        
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                square[i][j] = -1;
            }
        }
    }
    
    /*Note, that the user matrix might be uneven, hence why we first construct a default matrix.*/
    /**
     * Constructs a 2-dimensional array of the size 3X3, whose values are taken from the given 
     * array. If the given array’s size is bigger than 3X3, only the first 3X3 cells are taken. If the 
     * given array is smaller, the rest of the cells are initialized to –1.
     * @param array The array to be copied.
     */
     public Square3x3(int[][] array)
    {
        square = new int[SQUARE_SIZE][SQUARE_SIZE];
        int rows;
        
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                 square[i][j] = -1;
            }
        }
      
        if (array.length >= SQUARE_SIZE)
        {
            rows = SQUARE_SIZE;
        }
        else 
        {
            rows = array.length;
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                if (j < array[i].length)
                {
                    square[i][j] = array[i][j];
                }
            }                             
        }          
    }
    
    /** Copy constructor. Constructs a 2-dimensional array of the size 3X3, whose values are 
     * taken from the array of the given Square3x3 object.
     * @param other The Square3x3 object from which to construct the new Square3x3 object.*/
     public Square3x3(Square3x3 other)
    {
        square = new int[SQUARE_SIZE][SQUARE_SIZE];
        
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
               square[i][j] = other.square[i][j];
            }
        }
    }


    /** Construct a Square3X3 array from a given valid sudoku grid.
     * @param grid The grid from which the Square3X3 array will be constructed.
     * @return Square3X3 array representing the grid.*/
    public static Square3x3[][] construct_3X3Square_Array(int[][] grid)
    {
        Square3x3[][] array = new Square3x3[3][3];
        int[][] a = new int[3][3];
        int r = 0, c = 0;

        for (int i = 0; i < 9; i += 3)
        {
            for (int j = 0; j < 9; j += 3)
            {
                fill_3x3Integer_Array(a, grid, i, j);
                array[r][c] = new Square3x3(a);
                c++;
            }
            r++;
            c = 0;
        }

        return array;
    }

    /** Assists the construct_3X3Square_Array function by filling a 3X3 int array with values from the grid.
     * @param a The int array that needs to be filled.
     * @param grid The grid from which a needs to be filled.
     * @param r The row from which the filling will commence.
     * @param c The column from which the filling will commence.*/
    private static void fill_3x3Integer_Array(int[][] a, int[][] grid, int r, int c)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                a[i][j] = grid[r+i][c+j];
            }
        }
    }

    
    /**
     * Returns the value in the (row, col) cell. If the row and/or col are out of the 
     * array bounds, returns –1. Legal values for row/col are 0,1,2.
     * @param row The row of the desired cell.
     * @param col The column of the desired cell.
     * @return The (row, column) cell's value.
     */
     public int getCell(int row, int col)
     {
         if ((row < 0 || 2 < row) || (col < 0 || 2 < col)) {
             return -1;
         }
         return square[row][col];
     }
    
    
    /**
     * Sets the cell (row, col) in the array to the given value. If the row and/or col 
     * are out of the array bounds – does nothing. Legal values for row/col are 
     * 0,1,2. 
     * @param row The row of the desired cell.
     * @param col The column of the desired cell.
     * @param value The value to be set.
     */
     public void setXY(int row, int col, int value)
    {
         if (0 <= row && row <= 2 && 0 <= col && col <= 2)
        {
             square[row][col] = value;
        }
    }
    
    /**
     * Returns a string representation of the matrix.
     * @return A string representing the matrix. 
     * For example: 
     * 4   2   4
     * 8   3   5
     * 7   6   2
     */
     public String toString()
    {
        return square[0][0] + "\t" + square[0][1] + "\t" + square[0][2] + "\n" +
               square[1][0] + "\t" + square[1][1] + "\t" + square[1][2] + "\n" +
               square[2][0] + "\t" + square[2][1] + "\t" + square[2][2] + "\n";
    }
    
    /**
     * Returns true if all the numbers between 1 and 9 are present within a given matrix, false - otherwise.
     * @return true if all numbers are present.
     */
     public boolean allThere()
    {
        boolean present[] = new boolean[STANDARD_SIZE];
        int truths = 0;
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (1 <= square[i][j] && square[i][j] <= STANDARD_SIZE)
                {
                    present[square[i][j]-1] = true;
                }
            }
        }
        
        for (int i = 0; i < STANDARD_SIZE; i++)
        {
            if (present[i])
            {
                truths++;
            }
        }
        
        if (truths == STANDARD_SIZE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /*For example, the given row contains: {1,2,3}
      It will fill the first, second and third positions within the array with true.*/
    /**
     * Checks what values are within a given row, and fills the counter array accordingly.
     * @param row The row to be checked.
     * @param values The counter array to be filled.
     */
     public void whosThereRow(int row, int[] values)
    {       
        for (int j = 0; j < SQUARE_SIZE; j++)
        {          
            if (1 <= square[row][j] && square[row][j] <= STANDARD_SIZE)
            {
                values[square[row][j]-1]++;
            }
        }      
    }
    
    /**
     * Checks what values are within a given column, and fills the counter array accordingly.
     * @param col The column to be checked.
     * @param values The counter array to be filled.
     */
     public void whosThereCol(int col, int[] values)
    {
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            if (1 <= square[i][col] && square[i][col] <= STANDARD_SIZE)
            {
                values[square[i][col]-1]++;
            }
        }
    }
    
}
