public class Game {
    /**
     *
     * Using Renderer class to render a new board, defining new players using Player class.
     */
    Renderer renderer;
    Player playerX;
    Player playerO;

    public Game(Player pX, Player pO, Renderer r) {
        playerX = pX;
        playerO = pO;
        renderer = r;
    }
    /**
     *
     * This method is actually the one that is running the whole game, using all classes - Board to generate the board,
     * Player to generate the players and to use playTurn, Mark to generate marks, and renderer in order to generate
     * the X and O of the game.
     * @return
     */
    public Mark run() {
        int counter;
        Board board = new Board();
        final Player[] players = { playerX, playerO };
        final Mark[] marks = { Mark.X, Mark.O };

        for (counter = 0; !board.gameEnded(); counter++) {
            renderer.renderBoard(board);
            players[counter%2].playTurn(board, marks[counter%2]);

        }

        return board.getWinner();
    }


}
