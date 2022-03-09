package ascii_art;

import java.util.HashMap;
import java.util.HashSet;

public class Algorithms {

    // Summing over all the elemenets in the list, subtracting the sum from 1-n. we will receive the only duplicate
    // number as an output returned.
    // Running time - O(n), memory - two variables (i, sum).
    public static int findDuplicate(int[] numList) {
        int sum = 0;
        for (int i = 0; i < numList.length; i++)
            sum += numList[i] - i;
        return sum;
    }

    public static int uniqueMorseRepresentations(String[] words) {
        HashSet<String> morseCodes = new HashSet<>();
        final String[] morse =
                {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---",
                ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        for (String word : words) {
            String code = "";
            for (byte c : word.toLowerCase().getBytes())
                code += morse[c - 'a'];
            morseCodes.add(code);
        }
        return morseCodes.size();
    }
}
