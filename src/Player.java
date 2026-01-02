import java.util.List;

public class Player {
    public boolean isAi;
    public int id;
    public char icon;

    public static final int BIG = 100000000;
    private static final int winThreshhold = 10000000;
    private  int maxDepth;

    //Best order of columns to check
    private static final int[] searchOrder = {3, 2, 4, 1, 5, 0, 6};

    //Table that stores the amount of combinations each square can in theory be a part of, the more the "better"
    private static final int[][] WEIGHT_TABLE = {
    {3, 4, 8, 10, 8, 4, 3},
    {4, 6, 10, 12, 10, 6, 4},
    {5, 8, 11, 13, 11, 8, 5},
    {5, 8, 11, 13, 11, 8, 5},
    {4, 6, 10, 12, 10, 6, 4},
    {3, 4, 8, 10, 8, 4, 3}
    };

    private static final List<Window> WINDOWS = List.of(
        new Window(0, 1),  //Right
        new Window(1, 0),  //Up
        new Window(1, 1),  //Up-right
        new Window(1, -1)  //Up-left
    );
    
    public Player(boolean isAI, int id, char icon, int maxDepth) {
        this.isAi = isAI;
        this.id = id;
        this.icon = icon;
        this.maxDepth = maxDepth;
    }

    public class Node {
        public int evaluation;
        public int column;

        public Node(int evaluation, int column) {
            this.evaluation = evaluation;
            this.column = column;
        }
    }
    
    public int getAiMove(Board board) {
        return minimax(board, maxDepth, -BIG, BIG, true).column;
    }
    
    private int staticEvaluation(Board board) {
        int evaluation = evaluateTableWeight(board);

        for (Window window : WINDOWS) {
            evaluation += window.evaluateDirection(board);
        }          
        return evaluation;
    }

    private int evaluateTableWeight(Board board) {
        int evaluation = 0;
        for (int row = 0; row < board.rows; row++) {
            for (int column = 0; column < board.columns; column++) {
                evaluation += evaluateSlotWeight(board, row, column);
            }
        }
        return evaluation;
    }

    private int evaluateSlotWeight(Board board, int row, int column) {
        int weight = WEIGHT_TABLE[row][column];
        return switch (board.grid[row][column]) {
            case 1 -> weight;
            case 2 -> -weight;
            default -> 0;
        };
    }

    private Node minimax(Board board, int depth, int alpha, int beta, boolean isMax) {
        int nodeEvaluation = staticEvaluation(board);

        if (Math.abs(nodeEvaluation) >= winThreshhold) {
            int adjustment = (nodeEvaluation > 0) ? depth : -depth;
            return new Node(nodeEvaluation + adjustment, -1);
        }

        if (depth == 0 || board.boardFull()) {
            return new Node(nodeEvaluation, -1);
        }

        int currentEvaluation = (isMax) ? -BIG : BIG;
        int bestColumn = -1;
        int currentTurn = (isMax) ? 1 : 2;

        for (int column : searchOrder) {
            if (board.columnFull(column)) {
                continue;
            }

            board.place(column, currentTurn);
            int evaluation = minimax(board, depth - 1, alpha, beta, !isMax).evaluation;
            board.undoPlace(column);

            //Minimaxing
            if (isMax) {
                if (evaluation > currentEvaluation) {
                    currentEvaluation = evaluation;
                    bestColumn = column;
                }
                alpha = Math.max(alpha, currentEvaluation);
            } else {
                if (evaluation < currentEvaluation) {
                    currentEvaluation = evaluation;
                    bestColumn = column;
                }
                beta = Math.min(beta, currentEvaluation);
            }
            if (beta <= alpha) {
                break;
            }
        }
        return new Node(currentEvaluation, bestColumn);
    }

    public boolean checkWin(Board board) {
        for (Window window : WINDOWS) {
            if (Math.abs(window.evaluateDirection(board)) == BIG) {
                return true;
            }
        }     
        return false;   
    }
}