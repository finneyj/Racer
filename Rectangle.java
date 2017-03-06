/**
 * Models a simple, solid rectangle. 
 * This class represents a Rectabgle object. When combined with the GameArena class,
 * instances of the Rectangle class can be displayed on the screen.
 */
public class Rectangle 
{
	// The following instance variables define the
	// information needed to represent a Rectangle
	// Feel free to more instance variables if you think it will 
	// support your work... 
	
	private double xPosition;			// The X coordinate of centre of this Rectangle
	private double yPosition;			// The Y coordinate of centre of this Rectangle
	private double width;				// The width of this Rectangle
	private double height;				// The height of this Rectangle
	private String colour = "WHITE";	// The colour of this Rectangle

										// Permissable colours are 8 bit hexadecimal 
                                        // RGB values in the format #RRGGBB. e.g.
                                        //
                                        // Pure red is FF0000
                                        // Pure lime is 00FF00
                                        // Pure blue is 0000FF

	/**
	 * Obtains the current position of this Rectangle.
	 * @return the X coordinate of this Rectangle within the GameArena.
	 */
	public double getXPosition()
	{
		return xPosition;
	}

	/**
	 * Obtains the current position of this Rectangle.
	 * @return the Y coordinate of this Rectangle within the GameArena.
	 */
	public double getYPosition()
	{
		return yPosition;
	}

	/**
	 * Moves the current position of this Rectangle to the given X co-ordinate
	 * @param x the new x co-ordinate of this Rectangle
	 */
	public void setXPosition(double x)
	{
		this.xPosition = x;
	}

	/**
	 * Moves the current position of this Rectangle to the given Y co-ordinate
	 * @param y the new y co-ordinate of this Rectangle
	 */
	public void setYPosition(double y)
	{
		this.yPosition = y;
	}

	/**
	 * Obtains the width of this Rectangle.
	 * @return the width of this Rectangle,in pixels.
	 */
	public double getWidth()
	{
		return width;
	}

    /**
     * Defines a new width for this Rectangle.
     * @param width The new width of this rectangle, in pixels.
	 */
	public void setWidth(double width)
	{
		this.width = width;
	}

	/**
	 * Obtains the height of this Rectangle.
	 * @return the height of this Rectangle,in pixels.
	 */
	public double getHeight()
	{
		return height;
	}

    /**
     * Defines a new width for this Rectangle.
     * @param height The new height of this rectangle, in pixels.
	 */
	public void setHeight(double height)
	{
		this.height = height;
	}

	/**
	 * Obtains the colour of this Rectangle.
	 * @return a textual description of the colour of this Rectangle.
	 */
	public String getColour()
	{
		return colour;
	}
	
	/**
	 * Changes the colour of this Rectangle
	 * @param input the new colour of this Rectangle
	 */
	
	public void setColour(String input)
	{
		this.colour = input;
	}

	public Rectangle(double x, double y, double w, double h, String col)
	{
		xPosition = x;
		yPosition = y;
		width = w;
		height = h;
		colour = col;
	}	

    public boolean isTouching(Rectangle r)
    {
        return (xPosition - width/2 < r.getXPosition() + r.getWidth()/2 &&		
                xPosition + width/2 > r.getXPosition() - r.getWidth()/2 &&
                yPosition - height/2 < r.getYPosition() + r.getHeight()/2 &&
                yPosition + height/2 > r.getYPosition() - r.getHeight()/2);
    }


}
