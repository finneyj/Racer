/**
 * Models a simple slice of road, a kerb and offroad grass for the game Racer.
 *
 * @author Joe Finney (joe@comp.lancs.ac.uk)
 */
public class RoadSegment
{
    public static String ROUGH_COLOUR = "#00FF00";
    public static String KERB_COLOUR = "#808080";
    public static double KERB_WIDTH = 10;

    private Rectangle[] parts = new Rectangle[4];
    private double xPosition;
    private double yPosition;
    private double width;
    private double height;
    private GameArena arena;

    private double xSpeed;
    private double ySpeed;

    /**
     * Creates a new RoadSegment, at the given location and size.
     *
     * @param x The x position on the screen of the road segment.
     * @param y The y position on the screen of the road segment.
     * @param width The width of this road segment (in pixels)
     * @param height The height of this road segment (in pixels)
     * @param a The GameArena upon which to draw this road segment.
     */
    public RoadSegment(double x, double y, double width, double height, GameArena a)
    {
        this.width = width;
        this.height = height;
        arena = a;

        double roughWidth1 = x - width/2;
        double roughWidth2 = arena.getArenaWidth() - (x + width/2);

        double roughX1 = -width/2 - roughWidth1/2;
        double roughX2 =  width/2 + roughWidth2/2;

        parts[0] = new Rectangle(roughX1, height/2, roughWidth1, height, ROUGH_COLOUR);
        parts[1] = new Rectangle(roughX2, height/2, roughWidth2, height, ROUGH_COLOUR);
        parts[2] = new Rectangle(-width/2-KERB_WIDTH/2, height/2, KERB_WIDTH, height, KERB_COLOUR);
        parts[3] = new Rectangle(width/2+KERB_WIDTH/2, height/2, KERB_WIDTH, height, KERB_COLOUR);

        this.setXPosition(x);
        this.setYPosition(y);

        for (int i=0; i < parts.length; i++)
            arena.addRectangle(parts[i]);
    }

    /**
     * Changes the position of this RoadSegment to the given location
     *
     * @param x The new x positition of this RoadSegment on the screen.
     */
    public void setXPosition(double x)
    {
        double dx = x - xPosition;

        for (int i=0; i < parts.length; i++)
            parts[i].setXPosition(parts[i].getXPosition() + dx);

        xPosition = x;
    }

    /**
     * Changes the position of this RoadSegment to the given location
     *
     * @param y The new y positition of this RoadSegment on the screen.
     */
    public void setYPosition(double y)
    {
        double dy = y - yPosition;
        for (int i=0; i < parts.length; i++)
            parts[i].setYPosition(parts[i].getYPosition() + dy);

        yPosition = y;
    }

    /**
     * Determines the position of this RoadSegment on the screen
     *
     * @return The x position of the centre of this RoadSegment on the screen.
     */
    public double getXPosition()
    {
        return xPosition;
    }

    /**
     * Determines the position of this RoadSegment on the screen
     *
     * @return The y position of the centre of this RoadSegment on the screen.
     */
    public double getYPosition()
    {
        return yPosition;
    }

    /**
     * Provides a list (array) containing all the GameArena Rectangle objects that
     * make up this road segment.
     *
     * @return The list of Rectangles that makes up this RoadSegment 
     */
    public Rectangle[] getParts()
    {
        return parts;
    }

    /**
     * Sets the speed of this RoadSegment in the X axis - i.e. the number of pixels it moves in the X axis every time move() is called.
     *
     * @param s The new speed of this RoadSegment in the x axis
     */
    public void setXSpeed(double s)
    {
        xSpeed = s;
    }

    /**
     * Sets the speed of this RoadSegment in the Y axis - i.e. the number of pixels it moves in the Y axis every time move() is called.
     *
     * @param s The new speed of this RoadSegment in the y axis
     */
    public void setYSpeed(double s)
    {
        ySpeed = s;
    }

    /**
     * Updates the position of this RoadSegment by a small amount, depending upon its speed.
     * see setXSpeed(0 and setYSpeed() methods.
     */
    public void move()
    {
        this.setXPosition(xPosition + xSpeed);
        this.setYPosition(yPosition + ySpeed);
    }

    /**
     * Delete all the Rectangles that make up this RoadSegment from the screen
     */
    public void remove()
    {
        for (int i=0; i < parts.length; i++)
            arena.removeRectangle(parts[i]);
    }
}
