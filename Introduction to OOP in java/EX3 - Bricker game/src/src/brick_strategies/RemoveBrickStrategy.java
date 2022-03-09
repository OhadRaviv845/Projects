package src.brick_strategies;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * When a brick is being removed from the game it happens through this class, by this stage the brick already has
 * a certain functionality which was assigned to it when it was created and should now work. Here we only remove
 * the brick and update the counter which checks weather the game has ended. This is the concrete class which implements
 * the api CollisionStrategy.
 */
public class RemoveBrickStrategy implements CollisionStrategy {
    protected GameObjectCollection gameObjects;

    public RemoveBrickStrategy(GameObjectCollection objects) {
        gameObjects = objects;
    }
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        if (gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS))
            bricksCounter.decrement();
    }
    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }
}
