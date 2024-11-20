package sudoku.sudokusolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class SudokuSolverController {

    @FXML
    private GridPane grid;

    @FXML
    private Button reset;

    @FXML
    private Button solve;

    @FXML
    private Label time;

    @FXML
    private Label error;

    // The array that holds each cell within the grid.
    private TextArea[] textAreas;

    // Standard size of each sudoku row and column.
    private static final int SIZE = 9;

    // Measures the time passed since solved has been pressed.
    private long startTime;

    // Holds the solved grid given by the SudokuSolver class.
    private static final int[][] solution = new int[SIZE][SIZE];

    // This flag is raised when the SudokuSolver class found a solution.
    private static boolean solutionFlag;

    // A background task that tracks solutionFlag.
    private Timeline timeline = null;


    // This function only runs once as the application launches.
    public void initialize()
    {
        // Initialize the 81 sudoku cells array.
        textAreas = new TextArea[SIZE * SIZE];

        /*
            Initializes each individual cell,
            set its position within the grid,
            sets the cell's text font and style
            and for each cell, sets an event handler that will enable the user
            to shift through the cells with the up, down, left and right arrows.
            Finally, after setting up the cell, we add it to the grid.
         */
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            textAreas[i] = new TextArea();
            textAreas[i].setPrefSize(grid.getPrefWidth() / SIZE, grid.getPrefHeight() / SIZE);
            textAreas[i].setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

            int cellPosition = i;
            textAreas[i].addEventHandler(KeyEvent.KEY_PRESSED,
                    new EventHandler<KeyEvent>()
                    {
                        @Override
                        public void handle(KeyEvent event)
                        {
                            switch (event.getCode())
                            {
                                case KeyCode.UP:
                                    if (cellPosition - SIZE >= 0)
                                    {
                                        textAreas[cellPosition - SIZE].requestFocus();
                                    }
                                    break;

                                case KeyCode.DOWN:
                                    if (cellPosition + SIZE <= SIZE*SIZE - 1)
                                    {
                                        textAreas[cellPosition + SIZE].requestFocus();
                                    }
                                    break;

                                case KeyCode.LEFT:
                                    if (cellPosition - 1 >= 0)
                                    {
                                        textAreas[cellPosition - 1].requestFocus();
                                    }
                                    break;

                                case KeyCode.RIGHT:
                                    if (cellPosition + 1 <= SIZE*SIZE - 1)
                                    {
                                        textAreas[cellPosition + 1].requestFocus();
                                    }
                            }
                        }
            });

            grid.add(textAreas[i], i % SIZE, i / SIZE);
        }

        setSolutionFlag(false);
        time.setText("0");
        reset.setDisable(true);
    }


    @FXML
    void solvePressed(ActionEvent event)
    {
        error.setText("");

        if (examine_Input())
        {
            int[][] grid = new int[SIZE][SIZE];

            fill_Grid(grid);

            // Start measuring execution time.
            startTime = System.nanoTime();

            // Create a thread executor
            SudokuSolver.resetThreadExecutor();

            // Start solving from the upper and left most cell.
            new SudokuSolver(grid, 0, 0);

            // Sets and starts the background task.
            timeline = getTimeline();

            timeline.play();

            reset.setDisable(false);
            solve.setDisable(true);
        }
        else
        {
            error.setText("Invalid Grid");
        }
    }

    /*
        Sets the background task that will check every second if a solution has been found.
        If a solution has been found, we will safely shut down the thread executor.
     */
    private Timeline getTimeline()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                            if (solutionFlag)
                            {
                                print_Solution();

                                SudokuSolver.shutdownThreadExecutor();

                                solutionFlag = false;
                            }
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    @FXML
    void resetPressed(ActionEvent event)
    {
        /*
            Clear the cells,
            safely shut down the thread executor,
            stops the background task.
         */
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            textAreas[i].setText("");
            textAreas[i].setDisable(false);
        }

        setSolutionFlag(false);

        time.setText("0");

        error.setText("");

        SudokuSolver.shutdownThreadExecutor();

        if (timeline != null)
        {
            timeline.stop();
        }

        solve.setDisable(false);
    }

    /*
        Loads the solution found by the SudokuSolver class into the solution array.
     */
    public static void set_Solution(int[][] sudoku)
    {
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                solution[i][j] = sudoku[i][j];
            }
        }

        setSolutionFlag(true);
    }

    /*
        Prints the solution and the execution time and stops the backround task.
     */
    public void print_Solution()
    {
        String cell;
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            cell = String.valueOf(solution[i / SIZE][i % SIZE]);
            textAreas[i].setText(cell);
            textAreas[i].setDisable(true);
        }

        // Stop measuring execution time.
        long endTime = System.nanoTime();

        // Calculate the execution time in milliseconds.
        long MILLISECONDS = 1000000;

        double executionTime = (double) (endTime - startTime) / MILLISECONDS;

        String executionTimeString = String.valueOf(executionTime);

        time.setText(executionTimeString);

        timeline.stop();
    }

    /*
        Examines whether the values within the cells
        are whole numbers between 1 and 9 and that no number appears more than once
        in any given row and column.
     */
    private boolean examine_Input()
    {
        boolean validityFlag = valid_Input();

        if (validityFlag)
        {
            int[][] grid = new int[SIZE][SIZE];

            fill_Grid(grid);

            validityFlag = valid_Grid(grid);
        }

        return validityFlag;
    }

    /*
        Examines the values within the cells so that they range between 1 and 9.
     */
    private boolean valid_Input()
    {

        int k = 0, number;
        boolean flag = true;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!textAreas[k].getText().isEmpty())
                {
                    if (is_A_Number(textAreas[k].getText()))
                    {
                        number = Integer.parseInt(textAreas[k].getText());
                        if (!(1 <= number && number <= 9))
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = false;
                    }
                }
                k++;
            }
        }

        return flag;
    }

    /*
        Examines whether a given string is a number.
     */
    private boolean is_A_Number(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    /*
        Examines whether the input grid is valid, as in no number appears more than once in a row and column.
        This function connects this project with my previous project.
     */
    private boolean valid_Grid(int[][] grid)
    {
        Square3x3[][] array = Square3x3.construct_3X3Square_Array(grid);
        Sudoku sudoku = new Sudoku(array);
        return sudoku.is_Valid_Incomplete_Grid();
    }

    /*
        Fills the grid that will be sent to the SudokuSolver class.
     */
    private void fill_Grid(int[][] grid)
    {
        int k = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (textAreas[k].getText().isEmpty()) {
                    grid[i][j] = 0;
                } else {
                    grid[i][j] = Integer.parseInt(textAreas[k].getText());
                }
                k++;
            }
        }
    }

    public static boolean getSolutionFlag()
    {
        return solutionFlag;
    }

    public static void setSolutionFlag(boolean value)
    {
        solutionFlag = value;
    }
}
