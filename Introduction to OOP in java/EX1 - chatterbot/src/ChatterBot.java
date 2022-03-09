import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Ohad Raviv
 */
class ChatterBot {
	/**
	 *
	 * According to REQUEST_PREFIX the bot will reply either to Legal or Illegal request.
	 */
	static final String REQUEST_PREFIX = "say ";
	/**
	 *
	 * Placeholder for legal requests.
	 */
	static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
	/**
	 *
	 * Placeholder for Illegal requests.
	 */
	static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";
	/**
	 *
	 * @param name - for the name of the bot.
	 */
	String name;
	Random rand = new Random();
	String[] repliesToIllegalRequest;
	String[] repliesToLegalRequest;

	/**
	 *
	 * Defining method getName
	 * @return the name of the bot.
	 */
	String getName() {
		return name;
	}

	/**
	 *
	 *
	 * @param new_name - name
	 * @param repliesToLegalRequest - the bot will iterate through the for loop answering the legal replies.
	 * @param repliesToIllegalRequest - the bot will iterate through the for loop answering the Illegal replies.
	 */
	ChatterBot(String new_name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
		this.name = new_name;
		this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
		for(int i = 0; i < repliesToLegalRequest.length; i = i+1) {
			this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
		}
		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
		for(int i = 0; i < repliesToIllegalRequest.length; i = i+1) {
			this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
		}
	}

	/**
	 *
	 * @param statement - param to accept the statement from the bot.
	 * @param place_holder - the part we need to change in the string.
	 * @param replies - out of the given array, a random choose will give the next reply of the bot.
	 * @return - we return the final reply of the bot as a string.
	 */
	String replacePlaceholderInARandomPattern(String statement, String place_holder,  String[] replies) {
		int randomIndex = rand.nextInt(replies.length);
		String reply = replies[randomIndex];
		reply = reply.replaceAll(place_holder, statement);
		return reply;
	}

	/**
	 *
	 * @param statement
	 * @return
	 */
	String replyTo(String statement) {
		if (statement.startsWith(REQUEST_PREFIX)) {
			//we don’t repeat the request prefix, so delete it from the reply
			String statement_without_prefix = statement.replaceFirst(REQUEST_PREFIX, "");
			return replacePlaceholderInARandomPattern(statement_without_prefix, REQUESTED_PHRASE_PLACEHOLDER, repliesToLegalRequest);
		}
		return replacePlaceholderInARandomPattern(statement, ILLEGAL_REQUEST_PLACEHOLDER, repliesToIllegalRequest);
	}


}
