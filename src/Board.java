public class Board {
    public int rows;
    public int columns;
    public int[][] grid;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new int[rows][columns];
    }
    
    public boolean columnFull(int column) {
        return grid[rows-1][column] != 0;
    }

    public boolean boardFull() {
        for (int column = 0; column < columns; column++) {
            if (!columnFull(column)) { 
                return false;
            }
        }
        return true;
    }

    public void place(int column, int turn) {
        int height = checkHeight(column);
        grid[height][column] = turn;
    }

    public void undoPlace(int column) {
        grid[checkHeight(column) - 1][column] = 0;
    }

    private int checkHeight(int column) {
        for (int row = 0; row < rows; row++) {
            if (grid[row][column] == 0) {
                return row; 
            }
        }
        return rows;
    }
}