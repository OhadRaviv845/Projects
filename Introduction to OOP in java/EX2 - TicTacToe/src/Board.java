public class Board {
    public Board() {
        /**
         * Set integers row and col, use two loops iterating through the board to initiate the board
         * as BLANK objects of type Mark.
         * INT numEmpty will later be used to check weather the game has ended - counting down to zero.
         */
        int row, col;
        for(row = 0; row < SIZE; row++)
            for(col = 0; col < SIZE; col++)
                currentBoard[row][col] = Mark.BLANK;
        numEmpty = SIZE*SIZE;
    }

    /**
     As required, set the SIZE of the board and the desired WIN_STREAK.
     Set an object of an array of arrays which gets values of type Mark - currentBoard is the board we're going to use
     for the game.
     numEmpty checks how many empty spots there are - once it gets to zero the game is ended with a DRAW.
     */
    public static final int SIZE = 8;
    public static final int WIN_STREAK = 3;
    private Mark[][] currentBoard = new Mark[SIZE][SIZE];
    private int numEmpty;

    /**
     * This method is being used to put a mark on the board by the user. If we put a new mark on the board,
     * we will subtract 1 from our counter numEmpty.
     * @param mark - an object which receives the value of type Mark
     * @param row - row in the board
     * @param col - column in the board
     * @return false if input is invalid, true if the method has put a new mark on the board.
     */
    boolean putMark(Mark mark, int row, int col) {
        if(row < 0 || row >= SIZE || col < 0 || col >= SIZE)
            return false;
        if (currentBoard[row][col] != Mark.BLANK)
            return false;
        currentBoard[row][col] = mark;
        --numEmpty;
        return true;
    }

    /**
     * This method is used the check which mark is in a certain spot on the board.
     * @param row - row in the board
     * @param col - column in the board
     * @return - Mark.BLANK if input is invalid, if the input is valid return the requested mark in the spot on the board.
     */
    Mark getMark(int row, int col) {
        if(row < 0 || row >= SIZE || col < 0 || col >= SIZE)
            return Mark.BLANK;
        return currentBoard[row][col];
    }
    // A method to check weather the game has ended, either it ended with a DRAW if numEmpty = 0, or the method
    // will return the mark of the winning player.
   public boolean gameEnded() {
        if (numEmpty <= 0)
            return true;
        return getWinner() != Mark.BLANK;
   }
    /**
     *
     * A method being used by getWinner in order to count the WIN_STREAK of a given player.
     * This method is used for the counting using the given spot on the board and the directions to count towards - the deltas.
     * @param row
     * @param col
     * @param rowDelta
     * @param colDelta
     * @param mark
     * @return
     */
    public int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        do {
            count++;
            row += rowDelta;
            col += colDelta;
        } while (row < SIZE && row >= 0 && col < SIZE && col >= 0 && currentBoard[row][col] == mark);
        return count;
    }
    /**
     *
     * The method is checking which player has won, using the method countMarkInDirection as assistance.
     * @return
     */
    public Mark getWinner() {
        int row, col;

        for (row = 0; row < SIZE; row++) {
            for (col = 0; col < SIZE; col++) {
                Mark mark = currentBoard[row][col];
                if (mark == Mark.BLANK)
                    continue;
                if (countMarkInDirection(row, col, 0, 1, mark) >= WIN_STREAK)
                    return mark;
                if (countMarkInDirection(row, col, 1, 0, mark) >= WIN_STREAK)
                    return mark;
                if (countMarkInDirection(row, col, 1, 1, mark) >= WIN_STREAK)
                    return mark;
                if (countMarkInDirection(row, col, 1, -1, mark) >= WIN_STREAK)
                    return mark;
            }
        }
        return Mark.BLANK;
    }

}
