public class Window {
    private int directionRow;
    private int directionColumn;
    private int maxRow;
    private int maxColumn;
    private int diagonalOffset;

    private static final int[] EVALUATION_VALUES = {0, 1, 25, 100, 100000000};

    public Window(int directionRow, int directionColumn) {
        this.directionRow = directionRow;
        this.directionColumn = directionColumn; 
        maxRow = 6 - Math.abs(directionRow) * 3;
        maxColumn = 7 - Math.abs(directionColumn) * 3;
        diagonalOffset = (directionColumn < 0) ? 3 : 0;
    }

    public int evaluateDirection(Board board) {
        int boardEval = 0;
        for (int row = 0; row < maxRow; row++) {
            for(int column = 0; column < maxColumn; column++) {
                int count = evaluateWindow(row, column + diagonalOffset, board);
                int value = EVALUATION_VALUES[Math.abs(count)];
                int evaluation = (count > 0) ? value : -value;

                if (Math.abs(count) == 4) {
                    return evaluation;
                }
                boardEval += evaluation;
            }
        }
        return boardEval;
    }

    private int evaluateWindow(int baseRow, int baseColumn, Board board) {
        int countX = 0;
        int countO = 0;
        for(int step = 0; step < 4; step++) {
            int row = baseRow + (directionRow * step);
            int column = baseColumn + (directionColumn * step);
            int value = board.grid[row][column];
            
            if (value == 1) {
                countX++;
            } else if (value == 2) {
                countO++;
            }
        }

        if ((countX > 0 && countO > 0) || (countX == 0 && countO == 0)) {
            return 0; 
        }
        return (countX > 0) ? countX : -countO;
    }
}