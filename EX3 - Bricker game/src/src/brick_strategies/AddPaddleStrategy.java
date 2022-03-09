package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;
/**
 * This class is used to add a mock paddle when a brick with this functinality is being hit. This class performs the
 * check weather there is already a mock paddle active - using the variable of is instantiated - as the requirement was
 * to have a maximum of one mock paddle in every given moment. It is an extension of the decorator which means that
 * the functionality of this class is being attached to the brick through the BrickStrategyFactory. Once initiated from
 * the BrickStrategyFactory this class extends the decorator which is used to assign the functionality to the brick.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {
    private Renderable mockPaddleImage;
    private UserInputListener input;
    private Vector2 dimensions;
    private int MIN_DISTANCE_FROM_SCREEN_EDGE = 5;
    private final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE = 3;
    public AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, UserInputListener inputListener,
                      Vector2 windowDimensions) {
        super(toBeDecorated);
        mockPaddleImage = imageReader.readImage("assets/paddle.png", false);
        input = inputListener;
        dimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (MockPaddle.isInstantiated)
            return;
        MockPaddle mockPaddle = new MockPaddle(Vector2.ZERO, new Vector2(150, 20), mockPaddleImage,
                input, dimensions, getGameObjectCollection(), MIN_DISTANCE_FROM_SCREEN_EDGE,
                NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE);
        mockPaddle.setCenter(
                new Vector2(dimensions.x() / 2, dimensions.y() / 2));
        getGameObjectCollection().addGameObject(mockPaddle);
    }
}
