package src.brick_strategies;

import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import src.BrickerGameManager;
import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.util.Counter;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

/**
 * This class extends the decorator and adds to the bricks the functionalaity of the moving camera once the brick is
 * smashed. It implements that using options of the brickergamemanager which extends from danogl game manager.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {
    private final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private BrickerGameManager brickerGameManager;
    private WindowController controller;

    // The class constructor conforms to the ex reqs.
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        brickerGameManager = gameManager;
        controller = windowController;
    }

    // The onCollision is where the magic happens. Using the function of setCamera we send the variables which were
    // given on campus to create the change in the game's camera mode.
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (otherObj != brickerGameManager.ball)
            return;
        brickerGameManager.setCamera(
                new Camera(
                        brickerGameManager.ball, 			//object to follow
                        Vector2.ZERO, 	//follow the center of the object
                        controller.getWindowDimensions().mult(1.2f),  //widen the frame a bit
                        controller.getWindowDimensions()   //share the window dimensions
                )
        );
        getGameObjectCollection().addGameObject(new BallCollisionCountdownAgent(brickerGameManager.ball, this,
                NUM_BALL_COLLISIONS_TO_TURN_OFF));
    }

    // A void method used to turn off the change in the camera.
    public void turnOffCameraChange() {
        brickerGameManager.setCamera(null);
    }
}
