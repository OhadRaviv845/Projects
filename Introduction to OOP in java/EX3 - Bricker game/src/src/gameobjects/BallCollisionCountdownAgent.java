package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

public class BallCollisionCountdownAgent extends GameObject {
    ChangeCameraStrategy changeCameraStrategy;
    int target;
    private Ball myBall;

    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        target = ball.getCollisionCount() + countDownValue;
        myBall = ball;
        changeCameraStrategy = owner;
    }

    @Override
    public void update(float deltaTime) {
        if (myBall.getCollisionCount() >= target) {
            changeCameraStrategy.turnOffCameraChange();
            changeCameraStrategy.getGameObjectCollection().removeGameObject(this);
        }
    }
}
