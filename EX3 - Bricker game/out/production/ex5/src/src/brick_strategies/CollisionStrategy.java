package src.brick_strategies;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * This interface is pretty straightforward, it is a general type which is being used as part of the decorator pattern
 * which we use.
 */
public interface CollisionStrategy {
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);
    GameObjectCollection getGameObjectCollection();
}
