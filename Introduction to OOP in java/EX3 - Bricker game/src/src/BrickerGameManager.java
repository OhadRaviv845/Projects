package src;
import src.brick_strategies.BrickStrategyFactory;
import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import src.gameobjects.*;


import java.util.Random;
//The class BrickerGameManager inherits from GameManager.
public class BrickerGameManager extends GameManager {

    // All constants for the class are self explenatory by name.
    public static final float BORDER_WIDTH = 10;
    private static final float BALL_SPEED = 200;
    private static final int BRICKS_PER_LINE = 8;
    private static final int BRICK_LINES = 8;
    private static final int BRICK_GAP = 1;
    private static final int BRICK_HEIGHT = 15;
    private static final int LIFE = 3;
    private static final Vector2 NUMERIC_LIFE_LOCATION =
            new Vector2(50, 50 + (BRICK_HEIGHT+BRICK_GAP) * BRICK_LINES);
    private static final float NUMERIC_LIFE_WIDTH = 25;
    private static final float NUMERIC_LIFE_HEIGHT = 25;
    private static final float HEART_WIDTH = 50;
    private static final float HEART_HEIGHT = 30;
    private Counter lifeCount = new Counter(LIFE);
    private Counter bricksCount = new Counter(1);     // Start with 1 so we don't win right away.
    public Ball ball;
    private Vector2 windowDimensions;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 5;
    private BrickStrategyFactory brickStrategyFactory;
    // Constructor of the class, setting the window title and dimensions.
    public BrickerGameManager(String windowTitle, Vector2 windowDimension) {
        super(windowTitle, windowDimension);
    }

    /**
     * Overriding the method from the original class of danogl, this method starts the game.
     * It starts by setting window dimensions, then rendering background, rendering ball and calls upon the method
     * init ball whis is initiating it, rendering a paddle, rendering borders - 3 walls
     * (from both sides and from the top). It then continues to render the bricks, numeric life counter and of course
     * the graphic life counter.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        windowDimensions = windowController.getWindowDimensions();

        brickStrategyFactory = new BrickStrategyFactory(gameObjects(), this, imageReader, soundReader,
                inputListener, windowController, windowDimensions);

        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        gameObjects().addGameObject(background, Layer.BACKGROUND);


        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);


        //creating ball
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage, collisionSound);

        initBall();
        gameObjects().addGameObject(ball);

        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);

        //create user paddle
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(150, 20), paddleImage,
                inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        paddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);

        gameObjects().addGameObject(
                new GameObject(
                        //anchored at top-left corner of the screen
                        Vector2.ZERO,
                        //height of border is the height of the screen
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        //this game object is invisible; it doesn’t have a Renderable
                        null
                )
        );
        gameObjects().addGameObject(
                new GameObject(
                        //anchored at top of the screen
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        //height of border is the height of the screen
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        //this game object is invisible; it doesn’t have a Renderable
                        null
                )
        );
        gameObjects().addGameObject(
                new GameObject(
                        //anchored at top-right corner of the screen
                        Vector2.ZERO,
                        //width of border is the width of the screen
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        //this game object is invisible; it doesn’t have a Renderable
                        null
                )
        );

        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        initializeBricks(windowDimensions, brickImage);

        NumericLifeCounter numericLifeCounter = new NumericLifeCounter( lifeCount, NUMERIC_LIFE_LOCATION,
                new Vector2(NUMERIC_LIFE_WIDTH, NUMERIC_LIFE_HEIGHT), gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);

        ImageRenderable heartImage = imageReader.readImage("assets/heart.png",true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(new Vector2(NUMERIC_LIFE_LOCATION.x(), NUMERIC_LIFE_LOCATION.y() + NUMERIC_LIFE_HEIGHT + 5),
                new Vector2(HEART_WIDTH, HEART_HEIGHT), lifeCount, heartImage, gameObjects(), LIFE);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    /**
     * A method which is used to initalize bricks. It runs iteratively by lines number and then within each line
     * by bricks per line to create as many that are set prior to the beginning of the game. it calculates the bricks
     * size according to the amount of bricks and the window dimensions, it also calculates the location the same way.
     * bricksCount is a counter being used to measure weather the game has ended - initiated with value of 0, it is
     * incremented with each brick being created and decremented by 1 in the end so we can later write an easy finish
     * condition for the game.
     * @param windowDimensions
     * @param brickImage
     */
    private void initializeBricks(Vector2 windowDimensions, Renderable brickImage) {
        Vector2 brickDimensions = new Vector2((windowDimensions.x() - 2*BORDER_WIDTH) / BRICKS_PER_LINE - BRICK_GAP,
                BRICK_HEIGHT);
        for (int i = 0; i < BRICK_LINES; i++) {
            for (int j = 0; j < BRICKS_PER_LINE; j++) {
                Brick brick = new Brick(new Vector2(BORDER_WIDTH + j * (brickDimensions.x() + BRICK_GAP),
                        50 + i * (BRICK_HEIGHT + BRICK_GAP)), brickDimensions, brickImage,
                        brickStrategyFactory.getStrategy(), bricksCount);
                bricksCount.increment();
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
        bricksCount.decrement();     // Remove the initial 1.
    }

    // A method to initiate the ball, using random twice to choose which of the four directions it will start moving.
    private void initBall() {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    // This update runs with the game, updating the counter of lifeCount according to what is happening in the game.
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            //we lose a life
            lifeCount.decrement();
            if (lifeCount.value() <= 0) {
                System.out.println("we lose :(");
                System.exit(0);
            }
            initBall();

        }
        if (bricksCount.value() <= 0) {
            //we win
            System.out.println("We win!!!");
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
    }
}
