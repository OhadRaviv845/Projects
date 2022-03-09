package src.brick_strategies;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Puck;

/**
 * This class extends the decorator and is used for the puck class - through here the pucks accept their functionality.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator {
    private final int NUM_PUCKS = 3;
    Renderable puckRenderable;
    Sound colSound;

    // The class constructor conforms to the ex reqs.
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                        SoundReader soundReader) {
        super(toBeDecorated);
        puckRenderable = imageReader.readImage("assets/mockBall.png", true);
        colSound = soundReader.readSound("assets/blop_cut_silenced.wav");
    }

    // In the onCollision method we initiate new objects of puck, giving them all the necessary functionalities.
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        float puckWidth = thisObj.getDimensions().x() / NUM_PUCKS;
        Vector2 puckDim = new Vector2(puckWidth, puckWidth);
        for (int i = 0; i < NUM_PUCKS; i++) {
            Puck puck = new Puck(new Vector2(thisObj.getTopLeftCorner().x() + i * puckWidth,
                    thisObj.getTopLeftCorner().y()), puckDim, puckRenderable, colSound);
            getGameObjectCollection().addGameObject(puck);
        }


    }
}
