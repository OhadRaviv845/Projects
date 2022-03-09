package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.PrimitiveIterator;

public class MockPaddle extends Paddle {
    int collisionsRemaining;
    public static boolean isInstantiated;
    GameObjectCollection gameObjectCollection;

    /**
     * This class is in charge of creating a mock paddle, it is very similar to the paddle class, only that it's location
     * is in the center of the screen and it disappears once the variable collisionsRemaining (being sent through the
     * constructor of the class) reachs zero.
     * @param location - center screen
     * @param size - as the original paddle
     * @param image - as the original paddle
     * @param inputListener - using the danogl pack to take commands from the user who plays
     * @param windowDimensions
     * @param gameObjectCollection
     * @param minDistanceFromEdge - needs to conform to boundaries so that it cannot leave the screen
     * @param numCollisionsToDisappear
     */
    public MockPaddle(Vector2 location, Vector2 size, Renderable image, UserInputListener inputListener,
                      Vector2 windowDimensions, GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge, int numCollisionsToDisappear) {
        super(location, size, image, inputListener, windowDimensions, minDistanceFromEdge);
        collisionsRemaining = numCollisionsToDisappear;
        isInstantiated = true;
        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (--collisionsRemaining <= 0) {
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }

    }
}
