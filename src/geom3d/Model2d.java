package geom3d;

import java.awt.Graphics;

/**
 * Model 2d is a 2-dimensional context model, which will draw itself on the
 * canvas.
 * 
 * @author Jim
 * 
 */
public abstract class Model2d implements AnimatedItem {
	/**
	 * The paint method. Instances of this class need to define how to draw on
	 * the canvas. Note that if standalone() is false, then there may already be
	 * data on the canvas, and the graphics context will not be the starting
	 * one.
	 * 
	 * @param g
	 */
	public abstract void paint(Graphics g);

	/**
	 * Override this to animate the 2d graphics field
	 */
	@Override
	public void animate() {

	}

	/**
	 * Defines whether this context limits the drawing of the 3d contents
	 * Override in subclass to change.
	 * 
	 * @return
	 */
	public boolean standalone() {
		return true;
	}
}
