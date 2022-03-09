public class SnartypamtsPlayer implements Player {
        public SnartypamtsPlayer() {
            /**
             *
             * Setting a field for the type Player - playTurn. Given the currentBoard, and a mark to play, the method will
             * use the given calculations to apply the play to the board.
             */
        }
        // This ai looks to extend or block the longest streak on the board,
        // prefering places where it would extend or block other streaks as well.
    /**
     * Iterating over all possible board positions, count the streaks that could be extended by
     * this position.
     */
        public void playTurn(Board board, Mark mark) {
            int bestRow = 0, bestCol = 0, bestSum = 0;
            for (int row = 0; row < board.SIZE; row++) {
                for (int col = 0; col < board.SIZE; col++) {
                    if (board.getMark(row, col) != Mark.BLANK)
                        continue;
                    int sumX = 0, sumO = 0;
                    for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
                        for (int colDelta = -1; colDelta <= 1; colDelta++) {
                            if (rowDelta != 0 || colDelta != 0) {
                                sumX += 1 << 2*board.countMarkInDirection(row, col, rowDelta, colDelta, Mark.X);
                                sumO += 1 << 2*board.countMarkInDirection(row, col, rowDelta, colDelta, Mark.O);
                            }
                            if (Math.max(sumX, sumO) >= bestSum) {
                                bestSum = Math.max(sumX, sumO);
                                bestRow = row;
                                bestCol = col;
                            }

                        }
                    }
                }
            }
            board.putMark(mark, bestRow, bestCol);

    }

}
