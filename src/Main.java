public class Main {
	public static void main(String[] args) {
        Game game = new Game();

        game.start();   
        
        boolean gameRunning = true;
        while (gameRunning) { 
            gameRunning = game.run();
        }
	}
}