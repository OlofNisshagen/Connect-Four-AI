import java.util.Scanner;
import java.util.Arrays;

public class Game
{
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Scanner scanner;

    public Game() {
        board = new Board(ROWS, COLUMNS);
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("------------------------");
        System.out.println("Welcome to Connect-Four!");
        System.out.println("------------------------");

        System.out.println("Do you want to play against:");
        System.out.println("(1) a friend");
        System.out.println("(2) AI");
        int opponent = menu(new int[]{1, 2}, "opponent");

        int maxDepth = 1;
        if (opponent == 2){
            System.out.println();
            System.out.println("Choose the difficulty of the AI:");
            System.out.println("(1) Toddler");
            System.out.println("(2) Beginner");
            System.out.println("(3) Decent");
            System.out.println("(4) Master");

            int difficulty = menu(new int[]{1, 2, 3, 4}, "difficulty");
            maxDepth = switch (difficulty) {
                case 1 -> 1;
                case 2 -> 4;
                case 3 -> 9;
                case 4 -> 12;
                default -> 1; 
            };
        }

        player1 = new Player(false, 1, 'X', maxDepth);
        player2 = new Player(opponent == 2, 2, 'O', maxDepth);
        //Player 1 starts
        currentPlayer = player1;
    }

    private int menu(int[] choices, String topic) {
        int choice = -1;
        while (Arrays.binarySearch(choices, choice) < 0) {
            System.out.print("Enter " + topic + ": ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            if (choice != 1 && choice != 2) {
                System.out.println("Invalid " + topic + ", please try again!");
            }
        }
        return choice;
    }
        
    public boolean run() {
        printBoard();
        System.out.println(currentPlayer.icon + "'s turn");

        //Get either human or AI move
        int activeColumn = (currentPlayer.isAi)? currentPlayer.getAiMove(board) : getHumanMove();

        board.place(activeColumn, currentPlayer.id);
        
        if (currentPlayer.checkWin(board)) {
            printBoard();
            System.out.println(currentPlayer.icon + " wins!");
            return false;
        }

        if (board.boardFull()) {
            printBoard();
            System.out.println("It's a tie!");
            return false;
        }
        
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        
        return true;
    }

    public void printBoard() {
        System.out.println("-----------------------------");
        System.out.println();
        for (int row = ROWS - 1; row >= 0; row--) {
            System.out.print("|");
            for (int column = 0; column < COLUMNS; column++) {
                String symbol = switch (board.grid[row][column]) {
                    case 1 -> "X";
                    case 2 -> "O";
                    default -> " ";
                };
                System.out.print(" " + symbol + " |");
            }
            System.out.println();
        }
        System.out.println("-----------------------------");
        System.out.print("  ");
        for (int i = 1; i < 8; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
        System.out.println();
    }
    
    private int getHumanMove() {
        int activeColumn = 0;

        while(true) {
            System.out.print("Choose column: ");
            
            if (scanner.hasNextInt()) {
                activeColumn = scanner.nextInt() - 1;
            }

            if (activeColumn < 0 || activeColumn >= COLUMNS) {
                System.out.println("Column is out of bounds, try again!");
                continue;
            }

            if (board.columnFull(activeColumn)) {
                System.out.println("Column is full, try another!");
                continue;
            }
            return activeColumn;
        }
    }
}