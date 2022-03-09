package src.gameobjects;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 *
 * This class is in charge of creatic the graphic display of how many lives are left for the player.
 * It renders the picture provided in assets to display each unit of life, and is being used through the
 * BrickerGameManager which creates a gama object to display the lives left.
 */
public class GraphicLifeCounter extends GameObject {
    private static class MultiImageRenderer implements Renderable {
        private int maxCount;
        private Counter lifeCounter;
        private Renderable image;
        public MultiImageRenderer(Counter count, Renderable image, int numOfLives) {
            lifeCounter = count;
            maxCount = numOfLives;
            this.image = image;
        }
        @Override
        public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions, double degreesCounterClockwise,
                           boolean isFlippedHorizontally, boolean isFlippedVertically, double opaqueness) {
            float width1 = dimensions.x() / maxCount;

            for (int i = 0; i < lifeCounter.value(); i++) {
                image.render(g, new Vector2(topLeftCorner.x() + i*width1, topLeftCorner.y()),
                        new Vector2(width1, dimensions.y()), degreesCounterClockwise,
                        isFlippedHorizontally, isFlippedVertically, opaqueness);
            }
        }
    }

    public GraphicLifeCounter(Vector2 location, Vector2 dimensions, Counter count, Renderable image,
                              GameObjectCollection gameObjectCollection, int numOfLives) {
        super(location, dimensions, new MultiImageRenderer(count, image, numOfLives));
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
