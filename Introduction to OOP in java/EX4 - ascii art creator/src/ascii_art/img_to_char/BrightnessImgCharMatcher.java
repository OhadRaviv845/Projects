package ascii_art.img_to_char;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;


public class BrightnessImgCharMatcher {
    private Image imgToConvert;
    private String font;
    private float[] brightLevels;
    private char[] charArr;
    private final HashMap<Image, Double> cache = new HashMap<>();
    public BrightnessImgCharMatcher (Image image, String fontName) {
        imgToConvert = image;
        font = fontName;
    }

    // Count how many white pixels there are in a certain character.
    private int countWhite(char c) {
        int sum = 0;
        for (boolean[] row: CharRenderer.getImg(c, 16, font))
            for (boolean value: row)
                if (value)
                    ++sum;
        return sum;
    }

    /**
     * Setting extremes for min and max, iterating through all the given characters using countWhite() to count how many
     * white pixels there are in the character's image.
     * Then normalizing according to the min and max computed earlier.
     * @param charSet
     * @return
     */
    private float[] brightnessLevels(Character[] charSet) {
        int[] numWhite = new int[charSet.length];
        int minBrightness = 256;
        int maxBrightness = 0;
        for (int i = 0; i < charSet.length; i++) {
            int w = countWhite(charSet[i]);
            if (w < minBrightness)
                minBrightness = w;
            if (w > maxBrightness)
                maxBrightness = w;
            numWhite[i] = w;
        }
        float[] charBrightness = new float[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            if (maxBrightness == minBrightness)
                charBrightness[i] = 0.5f;
            else
                charBrightness[i] = (float) (numWhite[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return charBrightness;
    }

    // Sets the character array and the brightness levels of each character.
    private void setupChars(Character[] charSet) {
        charArr = new char[charSet.length];
        for (int i = 0; i < charSet.length; i++)
            charArr[i] = charSet[i].charValue();
        brightLevels = brightnessLevels(charSet);
    }

    // Find the character in the sed with the closest brightness to a given level.
    private char matchBrightness(float brightness) {
        float dist = 1;
        char c = ' ';
        for (int i = 0; i < charArr.length; i++) {
            float d = Math.abs(brightness - brightLevels[i]);
            if (d < dist) {
                dist = d;
                c = charArr[i];
            }
        }
        return c;
    }

    /**
     * Calculates the average brightness of an image, by first converting each pixel into a greyscale, then averaging
     * over all pixels in the image.
     * After part 2 of the ex, this method is now using a cache to save the pictures already calculated as keys
     * and values. After one instance of the class, next time it will be called instead of calculating from scratch
     * the method will use the saved data from previous runs.
     * @param image
     * @return
     */
    private double calcImgBrightness(Image image) {
        if (cache.containsKey(image)) {
            return cache.get(image);
        }
        double sum = 0;
        int count = 0;
        for (Color pixel: image.pixels()) {
            double greyPixel = (pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722) / 255;
            sum += greyPixel;
            ++count;
        }
        sum /= count;
        if (count > 0) {
            cache.put(image, sum);
            return sum;
        }
        else
            return 0.5;
    }

    /**
     * Variable pixels - represents the size of each sub image, that is avaraged to produce the brightness level for
     * one character in the output.
     * Variable lineLen is the actual length of the output line, which may differ from numCharsInRow due to integer
     * arithmetic.
     * Using the given api of squareSubImagesOfSize(), dividing the picture to convert into smaller sub images. Then
     * using calcAverageBrightness to compute brightness level for one character and use matchBrightness to find an
     * appropriate character from the given set.
     * @param numCharsInRow
     * @return
     */
    private char[][] convertImageToAscii(int numCharsInRow) {
        int pixels = imgToConvert.getWidth() / numCharsInRow;
        int i = 0, j = 0;
        int lineLen = imgToConvert.getWidth()/pixels;
        char[][] result = new char[imgToConvert.getHeight()/pixels][imgToConvert.getWidth()/pixels];
        for (Image subImage : imgToConvert.squareSubImagesOfSize(pixels)) {
            double b = calcImgBrightness(subImage);
            result[i][j] = matchBrightness((float) b);
            if (++j == lineLen) {
                ++i;
                j = 0;
            }
        }
        return result;
    }

    /**
     * The method is being called externally, it gets a certain character set to display the picture given in the
     * counstructor. It also gets a numCharsInRow to determine how many characters to display in each row.
     * The method calls setupChars to set up the two variables of charArr (array of the given character set)
     * and brightLevels (the brightness for each character in the array).
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        setupChars(charSet);
        var chars = convertImageToAscii(numCharsInRow);
//        (new ConsoleAsciiOutput()).output(chars);
        return chars;
    }
}
