package sudoku.sudokusolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SudokuSolver extends Thread
{
    private final int[][] grid;
    private final int row;
    private final int col;

    // A lock is used so that only one thread can set its grid as the solution.
    private static final Lock lock = new ReentrantLock();

    // The total amount of threads an executor can hold and the size of a standard sudoku grid.
    private static final int TOTAL_THREADS = 256, SIZE = 9;

    // Responsible for the currently SudokuSolver working threads and any impending threads.
    private static ExecutorService threadExecutor;


    /*
        SudokuSolver constructor.
        Copies a grid and submits to the thread executor waiting for its turn to execute.
     */
    public SudokuSolver(int[][] grid, int r, int c)
    {
        this.grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                this.grid[i][j] = grid[i][j];
            }
        }

        this.row = r;
        this.col = c;
        threadExecutor.submit(this);
    }

    /*
        The routine each SudokuSolver thread will execute.
        It will either set its grid as the solution or branch out to other cells for solving.
     */
    public void run()
    {
        if (row >= SIZE)
        {
            set_Finished_Grid_As_Solution();
        }

        if (row < SIZE)
        {
            if (grid[row][col] != 0)
            {
                if (col+1 < SIZE)
                {
                    new SudokuSolver(grid, row, col+1);
                }
                else
                {
                    new SudokuSolver(grid, row+1, 0);
                }
            }
            else
            {
                branch();
            }
        }
    }

    /*
        Sets only one finished grid as the solution.
        A lock is used so that only one thread can access this function at any given time.
     */
    private void set_Finished_Grid_As_Solution()
    {
        lock.lock();

        if (!SudokuSolverController.getSolutionFlag())
        {
            Square3x3[][] array = Square3x3.construct_3X3Square_Array(grid);
            Sudoku sudoku = new Sudoku(array);
            if (sudoku.is_Valid_Complete_Grid())
            {
                SudokuSolverController.set_Solution(grid);
            }
        }

        lock.unlock();
    }

    /*
        Fills a boolean array according to the existing values within the given row and column.
        Fills the cell at the intersection point, of the given row and column, with the missing values.
        For each missing value launches a new SudokuSolver thread
        that will continue the process until a solution is found.
     */
    private void branch()
    {
        boolean[] existing_values = new boolean[SIZE];

        for (int j = 0; j < SIZE; j++)
        {
            if (grid[row][j] != 0)
            {
                existing_values[grid[row][j] - 1] = true;
            }
            if (grid[j][col] != 0)
            {
                existing_values[grid[j][col] - 1] = true;
            }
        }

        for (int k = 0; k < SIZE; k++)
        {
            if (!existing_values[k])
            {
                grid[row][col] = k+1;
                if (col+1 < SIZE)
                {
                    new SudokuSolver(grid, row, col+1);
                }
                else
                {
                    new SudokuSolver(grid, row+1, 0);
                }
            }
        }
    }

    /*
        Safely shutdowns the thread executor.
     */
    public static void shutdownThreadExecutor()
    {
        threadExecutor.shutdown();
    }

    /*
        Creates a new instance of thread executor that overrides the previous instance.
        A thread executor that has been shut can not be restarted so we create a new instance.
     */
    public static void resetThreadExecutor()
    {
        threadExecutor = Executors.newFixedThreadPool(TOTAL_THREADS);
    }

}
