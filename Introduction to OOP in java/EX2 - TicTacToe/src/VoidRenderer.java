public class VoidRenderer implements Renderer {
    public VoidRenderer() {}
// This allows to run the game without displaying the board each time - good to run a big number of games between
    // ai players and check the win-loss-draw distribution.
    @Override
    public void renderBoard(Board board) {}
}
