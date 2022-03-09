package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * As learned in the TA, the decorator is the best solution for the design of this part of the ex. all the classes
 * which are added in this part - with all the different functionalities, extend this decorator to assign their own
 * functionality to the bricks that they create. This is a smart and efficient way to perform the ex requirements.
 */
public abstract class RemoveBrickStrategyDecorator extends java.lang.Object implements CollisionStrategy {
    protected CollisionStrategy decorated;
    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated) {
        decorated = toBeDecorated;
    }

    public GameObjectCollection getGameObjectCollection() {
        return decorated.getGameObjectCollection();
    }

    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        decorated.onCollision(thisObj, otherObj, counter);
    }
}
