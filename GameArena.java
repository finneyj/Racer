import java.util.*;
import java.util.Collections;
import java.util.concurrent.locks.*;
import javafx.scene.input.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.JFXPanel;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.awt.event.WindowEvent;

/**
 * This class provides a simple window in which grahical objects can be drawn. 
 *
 * Instances of the Ball and Rectangle classes can be added to an instance of this class to
 * draw and animate simple shapes on the screen. 
 *
 * @see Ball
 * @see Rectangle
 *
 * @author Joe Finney
 */
public class GameArena 
{
	//Colours to chose from
	public static final String BLACK = "#000000";
 	public static final String BLUE = "#0000ff";
 	public static final String CYAN = "#00ffff";
	public static final String DARKGREY = "#a9a9a9";
 	public static final String GREY = "#808080";
 	public static final String GREEN = "#008000";
 	public static final String LIGHTGREY = "#d3d3d3";
 	public static final String MAGENTA = "#ff00ff";
 	public static final String ORANGE = "#ffa500";
 	public static final String PINK = "#ffc0cb";
 	public static final String RED = "#ff0000";
	public static final String WHITE = "#ffffff";
	public static final String YELLOW = "#ffff00";
	
	// Size of window
	private int arenaWidth;
	private int arenaHeight;
    private JFrame window;

	private boolean exiting = false;
    private final static int MAXIMUM_OBJECTS = 100000;

    // Collections of primitives. These now relate 1:1 to JavaFX Nodes, since moving from AWT.
    private List<Object> addList = new ArrayList<Object>();
    private List<Object> removeList = new ArrayList<Object>();
    private Map<Ball, javafx.scene.shape.Circle> balls = new HashMap<>();
    private Map<Rectangle, javafx.scene.shape.Rectangle> rectangles = new HashMap<>();
    private int objectCount;

    // Basic button state
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;

    // JavaFX containers
    private Scene scene;
    private Group root;
    private JFXPanel jfxPanel;

    // Lock used to reduce flicker when rendering of large numbers of objects.
    private ReentrantLock renderLock;
    private boolean initialised;

	/**
     * Constructor. Creates an instance of the GameArena class, and displays a window on the
     * screen upon which shapes can be drawn.
     *
     * @param width The width of the window, in pixels.
     * @param height The height of the window, in pixels.
	 */
	public GameArena(int width, int height)
	{   
        this(width, height, true);
    }

	/**
     * Constructor. Creates an instance of the GameArena class, and displays a window on the
     * screen upon which shapes can be drawn.
     *
     * @param width The width of the window, in pixels.
     * @param height The height of the window, in pixels.
     * @param createWindow Determines if a JFrame containing the GameArena should be created (true) or not (false).
	 */
	public GameArena(int width, int height, boolean createWindow)
	{   
        this.arenaWidth = width;
        this.arenaHeight = height;
        this.objectCount = 0;
        this.initialised = false;

        // Create a lock to reduce flicker on rendering
        renderLock = new ReentrantLock();

        // Create a JavaFX canvas as a Swing panel.
        jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new java.awt.Dimension(width, height));

        // Create a window, if necessary.
        if (createWindow)
        {
            window = new JFrame();
            window.setTitle("Let's Play!");
            window.setContentPane(jfxPanel);
            window.setResizable(false);
            window.pack();
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        root = new Group();
        scene = new Scene(root, arenaWidth, arenaHeight, Color.BLACK);

        renderLock.lock();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX();
            }
        });
	}

   private void initFX() {

        EventHandler<KeyEvent> keyDownHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) 
                    up = true;
                if (keyEvent.getCode() == KeyCode.DOWN) 
                    down = true;
                if (keyEvent.getCode() == KeyCode.LEFT) 
                    left = true;
                if (keyEvent.getCode() == KeyCode.RIGHT) 
                    right = true;
            }
        };

        EventHandler<KeyEvent> keyUpHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) 
                    up = false;
                if (keyEvent.getCode() == KeyCode.DOWN) 
                    down = false;
                if (keyEvent.getCode() == KeyCode.LEFT) 
                    left = false;
                if (keyEvent.getCode() == KeyCode.RIGHT) 
                    right = false;
            }
        };

        scene.setOnKeyPressed(keyDownHandler);
        scene.setOnKeyReleased(keyUpHandler);

        jfxPanel.setScene(scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                frameUpdate();
            }
        }.start();
    }

	/**
	 * Close this GameArena window.
	 * 
	 */
	public void exit()
	{
		this.exiting = true;
	}

	/**
	 * A method called by the operating system to draw onto the screen - <p><B>YOU DO NOT (AND SHOULD NOT) NEED TO CALL THIS METHOD.</b></p>
	 */
	private void frameUpdate ()
    {
        if (!this.initialised)
            return;

        if (this.exiting)
        {
            addList.clear();
            removeList.clear();
            rectangles.clear();
            balls.clear();
            objectCount = 0;
            initialised = false;

            root.getChildren().clear();

            if (window != null)
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

            this.exiting = false;

            return;
        }

        // Optimise for the case where we have no data to render
        if(objectCount == 0)
            return;

        // Remove any deleted objects from the scene.
        renderLock.lock();

        for (Object o: removeList)
        {
            if (o instanceof Ball)
            {
                Ball b = (Ball) o;
                javafx.scene.shape.Circle c = balls.get(b);
                root.getChildren().remove(c);

                balls.remove(b);
            }

            if (o instanceof Rectangle)
            {
                Rectangle r = (Rectangle) o;
                javafx.scene.shape.Rectangle rectangle = rectangles.get(r);
                root.getChildren().remove(rectangle);

                rectangles.remove(r);
            }
        }

        removeList.clear();

        // Add any new objects to the scene.
        for (Object o: addList)
        {
            if (o instanceof Ball)
            {
                Ball b = (Ball) o;
                javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(0,0,b.getSize());
                root.getChildren().add(c);
                balls.put(b, c);
            }

            if (o instanceof Rectangle)
            {
                Rectangle r = (Rectangle) o;
                javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle(0, 0, r.getWidth(), r.getHeight());
                root.getChildren().add(rectangle);
                rectangles.put(r, rectangle);
            }
        }

        addList.clear();

        for(Map.Entry<Ball, javafx.scene.shape.Circle> entry : balls.entrySet())
        {
            Ball b = entry.getKey();
            javafx.scene.shape.Circle c = entry.getValue();

            c.setRadius(b.getSize());
            c.setTranslateX(b.getXPosition());
            c.setTranslateY(b.getYPosition());
            c.setFill(getColourFromString(b.getColour()));
        }

        for(Map.Entry<Rectangle, javafx.scene.shape.Rectangle> entry : rectangles.entrySet())
        {
            Rectangle r = entry.getKey();
            javafx.scene.shape.Rectangle rectangle = entry.getValue();
            rectangle.setWidth(r.getWidth());
            rectangle.setHeight(r.getHeight());
            rectangle.setTranslateX(r.getXPosition() - r.getWidth()/2);
            rectangle.setTranslateY(r.getYPosition() - r.getHeight()/2);
            rectangle.setFill(getColourFromString(r.getColour()));
        }

        renderLock.unlock();
    }

	//
    // Derive a Color object from a given string representation
	// 
	private Color getColourFromString(String col)
	{
		Color colour = Color.web(col);
		return colour;
	}
	
	/**
	 * Adds a given Ball to the GameArena. 
	 * Once a Ball is added, it will automatically appear on the window. 
	 *
	 * @param b the ball to add to the GameArena.
	 */
	public void addBall(Ball b)
	{
		synchronized (this)
		{
			if (objectCount > MAXIMUM_OBJECTS)
			{
				System.out.println("\n\n");
				System.out.println(" ********************************************************* ");
				System.out.println(" ***** Only 100000 Objects Supported per Game Arena! ***** ");
				System.out.println(" ********************************************************* ");
				System.out.println("\n");
				System.out.println("-- Joe\n\n");

                System.exit(0);
			}

            // Add this ball to the draw list. Initially, with a null JavaFX entry, which we'll fill in later to avoid cross-thread operations...
            removeList.remove(b);
            addList.add(b);
            objectCount++;
		}
	}

	/**
	 * Remove a Ball from the GameArena. 
	 * Once a Ball is removed, it will no longer appear on the window. 
	 *
	 * @param b the ball to remove from the GameArena.
	 */
	public void removeBall(Ball b)
	{
		synchronized (this)
		{
            addList.remove(b);
            removeList.add(b);
            objectCount--;
		}
	}

	/**
	 * Adds a given rectangle to the GameArena. 
	 * Once a Rectangle is added, it will automatically appear on the window. 
	 *
	 * @param r the rectangle to add to the GameArena.
	 */
	public void addRectangle(Rectangle r)
	{
		synchronized (this)
		{
			if (objectCount > MAXIMUM_OBJECTS)
			{
				System.out.println("\n\n");
				System.out.println(" ********************************************************* ");
				System.out.println(" ***** Only 100000 Objects Supported per Game Arena! ***** ");
				System.out.println(" ********************************************************* ");
				System.out.println("\n");
				System.out.println("-- Joe\n\n");

                System.exit(0);
			}

            // Add this ball to the draw list. Initially, with a null JavaFX entry, which we'll fill in later to avoid cross-thread operations...
            removeList.remove(r);
            addList.add(r);
            objectCount++;
		}
	}

	/**
	 * Remove a Rectangle from the GameArena. 
	 * Once a Rectangle is removed, it will no longer appear on the window. 
	 *
	 * @param r the rectangle to remove from the GameArena.
	 */
	public void removeRectangle(Rectangle r)
	{
		synchronized (this)
		{
            addList.remove(r);
            removeList.add(r);
            objectCount--;
		}
	}

	/**
	 * Pause for a 1/50 of a second. 
	 * This method causes your program to delay for 1/50th of a second. You'll find this useful if you're trying to animate your application.
	 */
	public void pause()
	{
        this.initialised = true;
        renderLock.unlock();

		try { Thread.sleep(18); }
		catch (Exception e) {};

        renderLock.lock();
	}

	/** 
	 * Gets the width of the GameArena window, in pixels.
	 * @return the width in pixels
	 */
	public int getArenaWidth()
	{
		return arenaWidth;
	}

	/** 
	 * Gets the height of the GameArena window, in pixels.
	 * @return the height in pixels
	 */
	public int getArenaHeight()
	{
		return arenaHeight;
	}

	/** 
	 * Determines if the user is currently pressing the cursor up button.
	 * @return true if the up button is pressed, false otherwise.
	 */
	public boolean upPressed()
	{
		return up;
	}

	/** 
	 * Determines if the user is currently pressing the cursor down button.
	 * @return true if the down button is pressed, false otherwise.
	 */
	public boolean downPressed()
	{
		return down;
	}

	/** 
	 * Determines if the user is currently pressing the cursor left button.
	 * @return true if the left button is pressed, false otherwise.
	 */
	public boolean leftPressed()
	{
		return left;
	}

	/** 
	 * Determines if the user is currently pressing the cursor right button.
	 * @return true if the right button is pressed, false otherwise.
	 */
	public boolean rightPressed()
	{
		return right;
	}

    /** 
     * Acquires the JPanel containing this GameArena.
     * @return The JPanel object containing this GameArena.
     */
    public JFXPanel getPanel()
    {
        return jfxPanel;
    }
}
