public class Tournament {

    private int playRounds;
    private Player players[];
    private Renderer renderer;

    Tournament(int rounds, Renderer r, Player[] p){
        playRounds = rounds;
        renderer = r;
        players = p;
    }
    public static void main(String[] args) {
        if(args.length != 4) {
            System.out.println("Wrong number of arguments, please use: java Tournament rounds renderer player1 player2");
            return;
        }
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();
        Player player1 = playerFactory.buildPlayer(args[2]);
        if (player1 == null) {
            System.out.println("Wrong argument passed for the first player, please try again.");
            return;
        }
        Player player2 = playerFactory.buildPlayer(args[3]);
        if (player2 == null) {
            System.out.println("Wrong argument passed for the second player, please try again.");
            return;
        }
        Renderer renderer = rendererFactory.buildRenderer(args[1]);
        if (renderer == null) {
            System.out.println("Wrong argument passed for the renderer, please try again.");
            return;
        }
        int rounds = Integer.parseInt(args[0]);
        if (rounds < 0) {
            System.out.println("Invalid number of rounds, please try again with a positive number.");
            return;
        }
        Player[] players = new Player[]{player1, player2};
        Tournament tournament = new Tournament(rounds, renderer, players);
        int[] winners = tournament.playTournament();
        //System.out.printf("%d %d  %d\n", winners[0], winners[1], winners[2]);
    }

    public int[] playTournament(){
        int counters[] = {0,0,0};
        for (int i = 0; i < playRounds; i++) {
            Game game = new Game(players[i%2], players[(i+1)%2], renderer);
            Mark winner = game.run();
            if (winner == Mark.BLANK)
                ++counters[2];
            else if (winner == Mark.X)
                ++counters[i%2];
            else if (winner == Mark.O)
                ++counters[(i+1)%2];

        }
        System.out.printf("%d %d  %d\n", counters[0], counters[1], counters[2]);
        return counters;
    }
}
