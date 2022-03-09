package src.gameobjects;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.rendering.TextRenderable;

/**
 *
 * This class is in charge of creating the text display of how many lives are left for the player. It uses the type
 * TextRenderable (which is imported from the danogl package) to render the text. It is being called by the
 * BrickerGameManager to create a game ocject which displays numerically how many lives are left.
 */
public class NumericLifeCounter extends GameObject {
    private Counter lifeCounter;
    private TextRenderable textRendrer;


    private NumericLifeCounter(Vector2 location, Vector2 dimensions, TextRenderable r) {
        super(location, dimensions, r);
        textRendrer = r;
    }

    public NumericLifeCounter(Counter count, Vector2 location, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        this(location, dimensions, new TextRenderable(""));
        lifeCounter = count;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRendrer.setString(Integer.toString(lifeCounter.value()));
    }
}
