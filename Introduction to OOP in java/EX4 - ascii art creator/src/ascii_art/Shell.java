package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The class is used to implement an application of the ascii to img converter we've created. In this class are defined
 * different commands, which are all called CMD in their variables. The class lets the user apply some simple processes
 * on the output image, as well as to choose weather to export it into a html file or print out to the console.
 */
public class Shell {
    Image image;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private Set<Character> charSet = new HashSet<>();
    private static final String CMD_EXIT = "exit";
    private static final String CMD_CHARS = "chars";
    private static final String CMD_ADD = "add";
    private static final String CMD_REMOVE = "remove";
    private static final String CMD_RES = "res";
    private static final String CMD_RENDER = "render";
    private static final String CMD_CONSOLE = "console";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private static final String INITIAL_CHARS_RANGE = "0-9";

    /**
     * As required, the constructor recieves an image, it initiates a charSet for use, and calculated different sizes
     * needed to create photos.
     * @param img
     */
    public Shell(Image img) {
        image = img;
        Collections.addAll(charSet, 'a','b','c','d');
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     * The method is the implementation of the application. It enables the user do operate the commands of show chars,
     * add chars, remove chars, res up, res down, render and print to console. The method is built using the switch
     * case syntax.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.next().trim();
        while (!cmd.toLowerCase().equals(CMD_EXIT)) {
            switch (cmd.toLowerCase()) {
            case CMD_CHARS:
                showChars();
                break;
            case CMD_ADD:
                addChars(scanner.next().trim());
                break;
            case CMD_REMOVE:
                removeChars(scanner.next().trim());
                break;
            case CMD_RES:
                resChange(scanner.next().trim());
                break;
            case CMD_RENDER:
                render();
                break;
            case CMD_CONSOLE:
                console();
                break;
            default:
                System.out.printf("Unknown command %s\n", cmd);
            }
            var param = scanner.nextLine().trim();
            System.out.print(">>> ");
            cmd = scanner.next();
        }
    }

    /**
     * Used by addChars(), here happens the parse of the string given by the user. As required, if the user puts in
     * one character it will be added to the char set. if the user puts in two characters, the range between them will be
     * added. if the user hits space then space will be added. if the user writes 'all' the all characters will be added.
     * @param param
     * @return
     */
    private static char[] parseCharRange(String param) {
        if (param.length() == 1)
            return new char[]{param.charAt(0), param.charAt(0)};
        if (param.equals("all"))
            return new char[]{' ', '~'};
        if (param.equals("space"))
            return new char[]{' ', ' '};
        if (param.length() == 3 && param.charAt(1) == '-') {
            if (param.charAt(0) > param.charAt(2))
                return new char[]{param.charAt(2), param.charAt(0)};
            else
                return new char[]{param.charAt(0), param.charAt(2)};
        }
        return null;
    }


    private void addChars(String s) {
        char[] range = parseCharRange(s);
        if (range == null)
            return;
        for (char c = range[0]; c <= range[1]; c++)
            charSet.add(c);
//        if (range != null)
//            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::add);
    }


    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        if (range == null)
            return;
        for (char c = range[0]; c <= range[1]; c++)
            charSet.remove(c);
//        if (range != null)
//            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::remove);
    }

    /**
     * Used to display chars in charSet for the user.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();
    }

    /**
     * After checking minimal/maximal number of chars in row is not exceeded, enlarges/shrinks the resolution by
     * multiplying in 2.
     * @param s
     */
    private void resChange(String s) {
        if (s.equals("up")) {
            if (charsInRow * 2 <= maxCharsInRow)
                charsInRow = charsInRow * 2;
            else
                System.out.println("Maximal number of chars in row exceeded");
        }
        else if (s.equals("down")) {
            if (charsInRow / 2 >= minCharsInRow)
                charsInRow = charsInRow / 2;
            else
                System.out.println("Minimal number of chars in row exceeded");
        }
        else
            System.out.println("Wrong input please try again");
        System.out.printf("Width set to %d\n", charsInRow);
    }

    /**
     * Using the HtmlAsciiOutput code which was provided, rendering the converted image into an html file.
     */
    private void render() {

        HtmlAsciiOutput asciiOutput = new HtmlAsciiOutput("out1.html", "Courier New");
        char[][] chars = charMatcher.chooseChars(charsInRow, charSet.toArray(new Character[charSet.size()]));
        asciiOutput.output(chars);
    }

    /**
     * Using the ConsoleAsciiOutput code provided, displaying the converted image into the console.
     */
    private void console() {
        ConsoleAsciiOutput consoleOutput = new ConsoleAsciiOutput();
        char[][] chars = charMatcher.chooseChars(charsInRow, charSet.toArray(new Character[charSet.size()]));
        consoleOutput.output(chars);
    }
}
