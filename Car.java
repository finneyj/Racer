/**
 * Models a simple Car for the game Racer.
 *
 * @author Joe Finney (joe@comp.lancs.ac.uk)
 */
public class Car
{
    public static String CAR_COLOUR = "#FF0000";
    public static String WHEEL_COLOUR = "#404040";

    private Rectangle[] parts = new Rectangle[7];
    private double xPosition;
    private double yPosition;
    private GameArena arena;

    private double xSpeed;
    private double ySpeed;

    /**
     * Creates a new Car, at the given location.
     *
     * @param x The x position on the screen of the centre of the car.
     * @param y The y position on the screen of the centre of the car.
     * @param a The GameArena upon which to draw this car.
     */
    public Car(double x, double y, GameArena a)
    {
        parts[0] = new Rectangle(10, 20, 10, 20, WHEEL_COLOUR);
        parts[1] = new Rectangle(10, 80, 10, 20, WHEEL_COLOUR);
        parts[2] = new Rectangle(50, 20, 10, 20, WHEEL_COLOUR);
        parts[3] = new Rectangle(50, 80, 10, 20, WHEEL_COLOUR);
        parts[4] = new Rectangle(30, 50, 40, 70, CAR_COLOUR);
        parts[5] = new Rectangle(15, 18, 5, 5, "WHITE");
        parts[6] = new Rectangle(45, 18, 5, 5, "WHITE");

        arena = a;
        this.setXPosition(x);
        this.setYPosition(y);

        for (int i=0; i < parts.length; i++)
            arena.addRectangle(parts[i]);
    }

    /**
     * Changes the position of this car to the given location
     *
     * @param x The new x positition of this car on the screen.
     */
    public void setXPosition(double x)
    {
        double dx = x - xPosition;

        for (int i=0; i < parts.length; i++)
            parts[i].setXPosition(parts[i].getXPosition() + dx);

        xPosition = x;
    }

    /**
     * Changes the position of this car to the given location
     *
     * @param y The new y positition of this car on the screen.
     */
    public void setYPosition(double y)
    {
        double dy = y - yPosition;
        for (int i=0; i < parts.length; i++)
            parts[i].setYPosition(parts[i].getYPosition() + dy);

        yPosition = y;
    }

    /**
     * Determines the position of this car on the screen
     *
     * @return The x position of the centre of this car on the screen.
     */
    public double getXPosition()
    {
        return xPosition;
    }

    /**
     * Determines the position of this car on the screen
     *
     * @return The y co-ordinate of the centre of this car on the screen.
     */
    public double getYPosition()
    {
        return yPosition;
    }

    /**
     * Sets the speed of this car in the X axis - i.e. the number of pixels it moves in the X axis every time move() is called.
     *
     * @param s The new speed of this car in the x axis
     */
    public void setXSpeed(double s)
    {
        xSpeed = s;
    }

    /**
     * Sets the speed of this car in the Y axis - i.e. the number of pixels it moves in the Y axis every time move() is called.
     *
     * @param s The new speed of this car in the y axis
     */
    public void setYSpeed(double s)
    {
        ySpeed = s;
    }

    /**
     * Updates the position of this car by a small amount, depending upon its speed.
     * see setXSpeed() and setYSpeed() methods.
     */
    public void move()
    {
        this.setXPosition(xPosition + xSpeed);
        this.setYPosition(yPosition + ySpeed);
    }

    /**
     * Determines if this car is touching the given RoadSegment.
     *
     * @param s The segment of road to test against.
     * @return true of this car is touching the given road segment, false otherwise.
     */
    public boolean isTouching(RoadSegment s)
    {
        Rectangle[] roadParts = s.getParts();

        for (int i=0; i < parts.length; i++)
            for (int j=0; j < roadParts.length; j++)
                if(parts[i].isTouching(roadParts[j]))
                    return true;

        return false;

    }
}
