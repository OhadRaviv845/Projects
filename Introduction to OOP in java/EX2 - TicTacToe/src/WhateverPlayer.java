import java.util.Random;

public class WhateverPlayer implements  Player {

        private Random random = new Random();

        public WhateverPlayer() {}
            /**
             *
             * Setting a field for the type Player - playTurn. Given the currentBoard, and a mark to play, the method will
             * randomize a row and a col and then place a mark in that spot. if the spot is not empty it will rndomize
             * again until it finds an empty spot.
             */

        public void playTurn(Board board, Mark mark) {
            int row, col;
            do {
                row = random.nextInt(board.SIZE);
                col = random.nextInt(board.SIZE);
            } while(!board.putMark(mark, row, col));
        }

}


