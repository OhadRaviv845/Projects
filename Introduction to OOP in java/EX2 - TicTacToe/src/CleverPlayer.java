public class CleverPlayer implements Player {

    public CleverPlayer() {}
    /**
     *
     * Setting a field for the type Player - playTurn. Given the currentBoard, and a mark to play, the method will
     * iterate through the board - starting at the top left spot, then iterating through the columns until reaching
     * the last column on the right, then down one line and once again iterating through the columns. It will place
     * it's mark in the first free spot it can find.
     */

    public void playTurn(Board board, Mark mark) {
        int row = 0, col = 0;
        while(!board.putMark(mark, row, col)) {
            ++col;
            if (col == board.SIZE) {
                col = 0;
                ++row;
            }
        }
    }

}




