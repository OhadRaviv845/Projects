package ascii_art;

import image.Image;
import java.util.logging.Logger;

/**
 * Class Driver holds the main. It has a default constructor which take no parameters. The class's purpose is to read
 * an image fro the file, then run the method run which is in the Shell class.
 */
public class Driver {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " + args[0]);
            return;
        }
        new Shell(img).run();
    }
}