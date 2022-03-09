import java.util.Scanner;

public class HumanPlayer implements Player {
    public HumanPlayer() {
        /**
         *
         * Setting a field for the type Player - playTurn. The method gets an input from the user and then it places
         * the mark in the spot the user has chosen - given that the spot is empty.
         */
    }
    public void playTurn(Board board, Mark mark) {
        Scanner in = new Scanner(System.in);
        int row, col;
        do {
            int num = in.nextInt();
            row = num/10 - 1;
            col = num%10 - 1;
        } while(!board.putMark(mark, row, col));
    }

}
