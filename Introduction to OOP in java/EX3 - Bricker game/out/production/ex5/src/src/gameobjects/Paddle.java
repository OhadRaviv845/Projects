package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private float screenWidth;
    private final UserInputListener inputListener;
    private static final float MOVEMENT_SPEED = 300;
    private float MIN_DISTANCE_FROM_SCREEN_EDGE;
    /**
     * This class is responsible for generating the paddle for the game. it gets the location, the size,
     * the image to render and also a variable to receive input from the player. it creates an object of the paddle
     * and updates it continuously with the game.
     * @param location
     * @param size
     * @param image
     * @param inputListener
     */
    public Paddle(Vector2 location, Vector2 size, Renderable image,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(location, size, image);
        this.inputListener = inputListener;
        screenWidth = windowDimensions.x();
        MIN_DISTANCE_FROM_SCREEN_EDGE = minDistanceFromEdge;
    }

    // Method update has two implications - checking the paddle cannot exit the screen (cannot get out of the borders)
    // and also to receive the input from the player and react accordingly.
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().x() < MIN_DISTANCE_FROM_SCREEN_EDGE) {
            setTopLeftCorner(new Vector2(MIN_DISTANCE_FROM_SCREEN_EDGE, getTopLeftCorner().y()));
        }
        if (getTopLeftCorner().x() > screenWidth - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x()) {
            setTopLeftCorner(new Vector2(screenWidth - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x(),
                    getTopLeftCorner().y()));
        }
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
