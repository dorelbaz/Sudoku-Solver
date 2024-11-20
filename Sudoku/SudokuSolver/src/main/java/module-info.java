module sudoku.sudokusolver {
    requires javafx.controls;
    requires javafx.fxml;


    opens sudoku.sudokusolver to javafx.fxml;
    exports sudoku.sudokusolver;
}