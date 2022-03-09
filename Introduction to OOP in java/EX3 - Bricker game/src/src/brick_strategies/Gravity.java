package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import java.util.Random;

/**
 * This functionality is the one i chose to implement further than the requirements, it adds gravity to the ball in a
 * random direction. When it occurs an arrow appears for the time of the occurance - 4 ball collisions - in the center
 * of the screen. The class extends from the decorator so the functionality can be added to the bricks.
 */
public class Gravity extends RemoveBrickStrategyDecorator {
    private Ball myBall;
    private WindowController controller;
    private BrickerGameManager brickerGameManager;
    private Vector2 dimensions;
    private Renderable arrowImage;
    private int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private int ARROW_SIZE = 60;
    private static Gravity activeGravity = null;
    private Random rand = new Random();
    private GravityLogo arrowObj;
    private float defaultSpeed;
    private class GravityLogo extends GameObject {
        private int target;

        // Using a subclass to create a new arrow Object.
        public GravityLogo() {
            super(new Vector2((dimensions.x() - ARROW_SIZE) / 2,( dimensions.y() - ARROW_SIZE) / 2),
                    new Vector2(ARROW_SIZE, ARROW_SIZE), arrowImage);
            target = myBall.getCollisionCount() + NUM_BALL_COLLISIONS_TO_TURN_OFF;
        }

        // Through the update we perform the check of the ball collisions - if we exceed 4 then the object will be
        // removed using the method of turnOffGravity.
        @Override
        public void update(float deltaTime) {
            if (myBall.getCollisionCount() >= target) {
                turnOffGravity();
            }
            else {
                renderer().setOpaqueness((0f + target - myBall.getCollisionCount()) / NUM_BALL_COLLISIONS_TO_TURN_OFF);
            }
        }
    }

    // The constructor of the class Gravity accepts all the variables it needs to take in order to operate according
    // to the given requirements.
    public Gravity(CollisionStrategy toBeDecorated, ImageReader imageReader, WindowController windowController,
                   BrickerGameManager gameManager) {
        super(toBeDecorated);
        myBall = gameManager.ball;
        controller = windowController;
        dimensions = windowController.getWindowDimensions();
        arrowImage = imageReader.readImage("assets/gravity.png", true);
        brickerGameManager = gameManager;
    }

    // In the onCollision we set the gravity and direction.
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        final Vector2[] accel = {Vector2.UP, Vector2.LEFT, Vector2.DOWN, Vector2.RIGHT};
        if (activeGravity != null)
            activeGravity.turnOffGravity();
        arrowObj = new GravityLogo();
        getGameObjectCollection().addGameObject(arrowObj, Layer.FOREGROUND);
        int i = rand.nextInt(4);
        arrowObj.renderer().setRenderableAngle(90 * i);
        defaultSpeed = myBall.getVelocity().magnitude();
        myBall.transform().setAcceleration(accel[i]);
        activeGravity = this;
    }

    // The method is used to turn off the prior Gravity if a new one is being initiated.
    public void turnOffGravity() {
        getGameObjectCollection().removeGameObject(arrowObj, Layer.FOREGROUND);
        myBall.transform().setAcceleration(Vector2.ZERO);
        myBall.setVelocity(myBall.getVelocity().normalized().mult(defaultSpeed));
        if (activeGravity == this)
            activeGravity = null;
    }
}