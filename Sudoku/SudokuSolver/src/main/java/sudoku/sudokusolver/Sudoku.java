package sudoku.sudokusolver;

/**
 * Class Sudoku.
 * This class constructs a standard sudoku template.
 * @author Dor Elbaz
 * @version 28.11.21
 */
public class Sudoku
{
    private final Square3x3[][] sudoku;
    private final int STANDARD_SIZE = 9, SQUARE_SIZE = 3;

    /**
     * First constructor for objects of class Sudoku.
     * Constructs a new sudoku grid and defaults its values to -1.
     */
     public Sudoku()
    {
        sudoku = new Square3x3[SQUARE_SIZE][SQUARE_SIZE];
        
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                sudoku[i][j] = new Square3x3();
            }
        }  
    }
    
    /**
     * Second constructor for objects of class Sudoku.
     * Constructs a new sudoku grid with the specified Square3x3 matrix.
     * @param square3x3array The matrix that holds the values for the sudoku grid.
     */    
     public Sudoku(Square3x3[][] square3x3array)
    {
        sudoku = new Square3x3[SQUARE_SIZE][SQUARE_SIZE];
        
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                sudoku[i][j] = new Square3x3(square3x3array[i][j]);
            }
        }
    }
    
    /**
     * Returns true if the given complete template is solved correctly, false - otherwise.
     * @return true if the template is solved correctly.
     */
    public boolean is_Valid_Complete_Grid()
    {
        //Each counter matrix represents the 9 rows/cols in a sudoku grid and the 9 (between 1 and 9) numbers it should contain in each row/col.
        int[][] validRows = new int[STANDARD_SIZE][STANDARD_SIZE];
        int[][] validCols = new int[STANDARD_SIZE][STANDARD_SIZE];

        fill_Counter_Matrix(validRows, validCols);
        
        //Examining the counter matrices. There should be in each row exactly 9 increments (marking the presence of 1-9).
        return examine_Complete_Grid_Validity(validRows, validCols);
    }

    /**
     * Returns true if the given incomplete template is correct, false - otherwise.
     * Correct as in no repeating numbers for any row and column.
     * @return true if the template is valid.
     */
    public boolean is_Valid_Incomplete_Grid()
    {
        int[][] validRows = new int[STANDARD_SIZE][STANDARD_SIZE];
        int[][] validCols = new int[STANDARD_SIZE][STANDARD_SIZE];

        fill_Counter_Matrix(validRows, validCols);

        /*Examining the counter matrices. There should be in each row at most 9 increments;
         for each number between 1 and 9, its respective counter should be 0 or 1.*/
        return examine_InComplete_Grid_Validity(validRows, validCols);
    }
    
    /**
     * Examines a sudoku row.
     * @param r The row to be examined.
     * @param row_Constant The constant row of the square matrices.
     * @param vr The counter array to be filled.
     */
     private void examine_Row(int r, int row_Constant, int[] vr)
    {
        /*By examining each row of the 3 square3x3 objects, that together compose the sudoku's row, and fills the counter array accordingly.
          For example r = 0, row_Constant = 0, we'll examine: sudoku[0][0] 0th row, sudoku[0][1] 0th row, sudoku[0][2] 0th row, 
          and the rows compose the 0th row of the sudoku's grid. */
        for (int j = 0; j < SQUARE_SIZE; j++)
        {
            sudoku[r][j].whosThereRow(row_Constant, vr);
        }
    }
    
    /**
     * Examines a sudoku column.
     * @param c The column to be examined.
     * @param col_Constant The constant column of the square matrices.
     * @param vc The counter array to be filled.
     */
     private void examine_Col(int c, int col_Constant, int[] vc)
    {
        /*By examining each col of the 3 square3x3 objects, that together compose the sudoku's col, and fills the counter array accordingly.
          For example c = 0, col_Constant = 0, we'll examine: sudoku[0][0] 0th col, sudoku[1][0] 0th col, sudoku[2][0] 0th col,
          and the cols compose the 0th col of the sudoku's grid.*/
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            sudoku[i][c].whosThereCol(col_Constant, vc);
        }
    }
    
    /**
     * Examines the validity of the counter matrices. Returns true if each row in the matrices hold exactly 9 increments, false - otherwise.
     * @param vr The matrix representing the values of the sudoku's rows.
     * @param vc The matrix representing the values of the sudoku's columns.
     * @return true if both matrices rows hold exactly 9 increments.
     */
    private boolean examine_Complete_Grid_Validity(int[][] vr, int[][] vc)
    {
        /*We examine each cell in the matrices simultaneously (since they are of the same size). If we don't detect a single increment, we return false and
          the complete sudoku template is invalid, else we return true and it is valid.*/
        for (int i = 0; i < STANDARD_SIZE; i++)
        {
            for (int j = 1; j < STANDARD_SIZE; j++)
            {
                if (vr[i][j] != 1 || vc[i][j] != 1)
                {
                    return false;
                }                
            }
        }
        
        return true;
    }


    /**
     * Examines the validity of the counter matrices. Returns true if each row in the matrices hold at most 9 increments, false - otherwise.
     * @param vr The matrix representing the values of the sudoku's rows.
     * @param vc The matrix representing the values of the sudoku's columns.
     * @return true if both matrices rows hold at most 9 increments.
     */
    private boolean examine_InComplete_Grid_Validity(int[][] vr, int[][] vc)
    {
        /*We examine each cell in the matrices simultaneously (since they are of the same size). If we detect more than a single increment,
        we return false and the incomplete sudoku template is invalid, else we return true and it is valid.*/
        for (int i = 0; i < STANDARD_SIZE; i++)
        {
            for (int j = 1; j < STANDARD_SIZE; j++)
            {
                if (vr[i][j] > 1 || vc[i][j] > 1)
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Fills the counter matrices according to the values present in the grid.
     * @param validRows The matrix representing the values of the sudoku's rows.
     * @param validCols The matrix representing the values of the sudoku's columns.
     */
    private void fill_Counter_Matrix(int[][] validRows, int[][] validCols)
    {
        int c = 0;

        /*This loop goes by 3 square3x3 objects (so 3 rows in the sudoku template).*/
        for (int i = 0; i < SQUARE_SIZE; i++)
        {
            /*This loop goes by each sudoku row/col individually (3 at a time).*/
            for (int j = 0; j < SQUARE_SIZE; j++)
            {
                /*Goes by a single sudoku row.*/
                examine_Row(i, j, validRows[c]);

                /*Goes by a single sudoku col.*/
                examine_Col(i, j, validCols[c]);

                 /*c is used to mark the respective row/col we examine in the sudoku grid.
                  For example, for c = 0, we'll examine the 0th row of the sudoku grid,
                  that is made up of the 0th rows of the first 3 square3x3 objects.*/
                c++;
            }
        }
    }

}
