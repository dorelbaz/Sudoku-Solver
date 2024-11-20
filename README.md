# Sudoku-Solver
In this project, I expanded upon my Sudoku Examiner project. 
At first, we take the input grid and examine its validity.
If the input grid is invalid, we'll notify the user with an error message,
otherwise, we send it to the SudokuSolver class, there we will solve the grid utilizing concurrency.
This project's GUI has been made with JavaFX.

Changes made in the Sudoku Examiner:
(I) Utilization of counter matrices instead of boolean matrices.
(II) Remaned isValid() to is_Valid_Complete_Grid() and examine_Validity() to examine_Complete_Grid_Validity().
(III) Added constants.


New features added to Sudoku Examiner:
(*) In Sudoku Class:

  (I) public boolean is_Valid_Incomplete_Grid() - Examines an incomplete sudoku grid for its validity.
  (II) private boolean examine_InComplete_Grid_Validity(int[][] vr, int[][] vc) - Examines the validity of the counter matrices.
  (III) private void fill_Counter_Matrix(int[][] validRows, int[][] validCols) - Goes through the grid and increments the counter matrices according to the value present in each row and column.
  
(*) In Square3X3 class:
  (I) public static Square3x3[][] construct_3X3Square_Array(int[][] grid) - Constructs a Square3x3 array according to a given grid.
  (II) private static void fill_3x3Integer_Array(int[][] a, int[][] grid, int r, int c) - Fills a 3X3 int array with values from the grid starting from grid[r][c].
