import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the SnartypamtsPlayer Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class SnartypamtsPlayerTest {
	/**
	 * number of games to play times 100.
	 */
	public static final int GAMES = 100;

	private File outFile;

	/**
	 * Checks that the SnartypamtsPlayer beats the CleverPlayer by at least the requested distribution - EPSILON.
	 * <p>
	 * It is recommended you temporarily remove the 'final' keyword from SIZE and WIN_STREAK and than
	 * uncomment the commented out lines for a full check of all scenarios.
	 * <p>
	 * if not the check will be done only for your current SIZE and WIN_STREAK constants.
	 * <p>
	 * NOTICE:
	 * This test relies on probability, so you might fail it once in a few tries even if your code is correct.
	 */
	@Test
	void checkAllWinDistribution() {
		checkWinDistribution();
		// TODO uncomment from here
//		int initialSize = Board.SIZE, initialStreak = Board.WIN_STREAK;
//		for (int streak = 3; streak < 8; ++streak) {
//			for (int size = 3; size < 8; ++size) {
//				if (streak > size) {
//					continue;
//				}
//				Board.WIN_STREAK = streak;
//				Board.SIZE = size;
//		checkWinDistribution();
//			}
//		}
//		Board.WIN_STREAK = initialStreak;
//		Board.SIZE = initialSize;
		// TODO uncomment to here
	}

	/**
	 * Checks that the CleverPlayer beats the Whatever player by at least the requested distribution - EPSILON.
	 * The check is done only for your current SIZE and WIN_STREAK constants.
	 * <p>
	 * NOTICE:
	 * This test relies on probability, so you might fail it once in a few tries even if your code is correct.
	 */
	private void checkWinDistribution() {
		PrintStream stream;
		try {
			stream = printToFile();
		} catch (IOException e) {
			fail("unable to print to file.");
			return;
		}
		Tournament tournament = new Tournament(
				GAMES * 100,
				new VoidRenderer(),
				new Player[]{new SnartypamtsPlayer(), new CleverPlayer()}
		);
		tournament.playTournament();
		stream.close();
		var results = getResults();
		assert (results[0] > results[1]);
	}

	private int[] getResults() {
		var results = new int[3];
		String lastLine = "";
		String currentLine;
		try (BufferedReader br = new BufferedReader(new FileReader("out.txt"))) {
			while ((currentLine = br.readLine()) != null && !currentLine.equals("")) {
				lastLine = currentLine;
			}
		} catch (IOException e) {
			fail("unable to read out file.");
			return results;
		}
		lastLine = lastLine.replaceAll("[^0-9]", " ");
		var resultsAsStrings = lastLine.split("\\s+");
		if (resultsAsStrings.length > 4) {
			results[0] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 4]);
			results[1] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 2]);
			results[2] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 1]);
		} else if (resultsAsStrings.length > 2) {
			for (int i = resultsAsStrings.length - 3; i < resultsAsStrings.length; ++i) {
				results[i] = Integer.parseInt(resultsAsStrings[i]);
			}
		} else {
			fail("Tournament.playTournament didn't print enough numbers.");
		}
		return results;
	}

	private PrintStream printToFile() throws FileNotFoundException {
		var outFile = new File("out.txt");
		PrintStream out = new PrintStream("out.txt");
		System.setOut(out);
		return out;
	}
}