import java.util.Scanner;


class chat {
    public static void main(String[] args){
        final String INITIAL_STATEMENT = "Good night!";



        ChatterBot[] bots = new ChatterBot[2];
        bots[0] = new ChatterBot(
                "Henry",
                new String[] {
                        "You want me to say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + ", do you? alright: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "Okay, here goes " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                },
                new String[] {
                        "say I should say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "what " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "?"
                }
        );
        bots[1] = new ChatterBot(
                "George",
                new String[] {
                        "What is it? I can say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "I love to say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                },
                new String[] {
                        "say say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "whaaat "
                }
        );

        String current_statement = INITIAL_STATEMENT;

        Scanner scanner = new Scanner(System.in);
        while(true) {
            for (ChatterBot bot : bots) {
                System.out.print(bot.getName() + ": " + current_statement);
                current_statement = bot.replyTo(current_statement);
                scanner.nextLine();
            }
        }
    }

}