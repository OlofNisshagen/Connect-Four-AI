import java.util.Scanner;

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
        //Prints start message
        System.out.println("------------------------");
        System.out.println("Welcome to Connect Four!");
        System.out.println("------------------------");
        System.out.println("Do you want to play against:");
        System.out.println("(1) a friend");
        System.out.println("(2) AI");

        //Choosing your opponent (Friend / AI)
        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.print("Enter choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid opponent, please try again!");
            }
        }
        player1 = new Player(false, 1, 'X');
        player2 = new Player(choice == 2, 2, 'O');

        //Player 1 starts
        currentPlayer = player1;
    }
        
    public boolean run() {
        //Print board and who's turn it is
        printBoard();
        System.out.println(currentPlayer.icon + "'s turn");

        //Get either human or AI move
        int activeColumn = (currentPlayer.isAi)? currentPlayer.getAiMove(board) : getHumanMove();

        //Makes said move
        board.place(activeColumn, currentPlayer.id);
        
        //If win, end Game-loop
        if (currentPlayer.checkWin(board)) {
            printBoard();
            System.out.println(currentPlayer.icon + " wins!");
            return false;
        }

        //If tie, end Game-loop
        if (board.boardFull()) {
            printBoard();
            System.out.println("It's a tie!");
            return false;
        }
        
        //Change turn
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