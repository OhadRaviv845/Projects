package src.brick_strategies;
import java.util.Random;

import danogl.gui.SoundReader;
import src.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

/**
 * This class is a factory which is used to create all the bricks. It randomizes the functionality of the bricks and
 * then initiates the objects which they extend the decorator.
 */
public class BrickStrategyFactory {
    private Random rand = new Random();
    private GameObjectCollection objectCollection;
    private ImageReader image;
    private UserInputListener input;
    private Vector2 dimensions;
    private SoundReader sound;
    private WindowController window;
    private BrickerGameManager bricker;

    // The class constructor accepts all vital variables it needs to operate and conforms to the api reqs.
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection, BrickerGameManager gameManager,
                                ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                                WindowController windowController, Vector2 windowDimensions) {
        objectCollection = gameObjectCollection;
        image = imageReader;
        input = inputListener;
        dimensions = windowDimensions;
        sound = soundReader;
        window = windowController;
        bricker = gameManager;
    }

    /**
     * Here i chose to use two different functions to implement the fact that a brick can't have more than three
     * different functionalities at once, that the probabilities for each behaviour is a sixth and the prob for the
     * double behaviour is 1/36, and to keep the option for a brick to be without special functionalities - set as
     * default state. The funtions have the same name but are being differentiated by the fact that they take different
     * variables.
     * @return
     */
    public CollisionStrategy getStrategy() {
        CollisionStrategy toBeDecorated = new RemoveBrickStrategy(objectCollection);
        int method = rand.nextInt(6);
        // This is a use for double behaviour.
        if (method == 5) {
            toBeDecorated = getStrategy2(toBeDecorated, rand.nextInt(5));
            method = rand.nextInt(6);
            // Here we can add the third functionality.
            if (method == 5) {
                toBeDecorated = getStrategy2(toBeDecorated, rand.nextInt(5));
                method = rand.nextInt(5);
            }
        }
        // If there is only one functionality it will be rendered through here, also this could be the second or the
        // third in the case that we randomized the double.
        return getStrategy2(toBeDecorated, method);
    }

    // Creating different functionalties using the switch expression.
    private CollisionStrategy getStrategy2(CollisionStrategy toBeDecorated, int method) {
        switch (method) {
            case 1:
                return new AddPaddleStrategy(toBeDecorated, image, input, dimensions);
            case 2:
                return new PuckStrategy(toBeDecorated, image, sound);
            case 3:
                return new ChangeCameraStrategy(toBeDecorated, window, bricker);
            case 4:
                return new Gravity(toBeDecorated, image, window, bricker);
            default:
                return toBeDecorated;
        }
    }
}