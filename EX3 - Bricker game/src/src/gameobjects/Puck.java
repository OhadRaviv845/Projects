package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.util.Random;

public class Puck extends Ball {
    private float PUCK_SPEED = 250;
    private Sound collisionSound;
    Random rand = new Random();
    /**
     * Similar to paddle only in here we create a grey ball which doesn't count for the life in the game.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, null);
        initPuck();
    }

    private void initPuck() {
        float puckVelX = PUCK_SPEED;
        float puckVelY = PUCK_SPEED;
        if (rand.nextBoolean())
            puckVelX *= -1;
        if (rand.nextBoolean())
            puckVelY *= -1;
        setVelocity(new Vector2(puckVelX, puckVelY));
    }
}

