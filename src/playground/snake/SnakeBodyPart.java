package playground.snake;

import playground.GridFieldItem;

/**
 * This interface defines items considered as Body Parts for a snake The snake
 * only has one head, multiple body-models and one tail.
 * 
 * @author Jim
 * 
 */
public interface SnakeBodyPart extends GridFieldItem {
	/**
	 * Set which Grid cell the body part should appear in next This is to
	 * propagate changes of state in the whole snake
	 * 
	 * @param x
	 * @param y
	 */
	public void setNext(int x, int y);

	/**
	 * Set the 'step' value for the item and update its position
	 * 
	 * @param mt
	 */
	public void setMicrotime(int mt);

	/**
	 * Get the 'step' value for the item
	 * 
	 * @return step micro time
	 */
	public int getMicrotime();

	/**
	 * Returns the last grid position calculated from facing direction
	 * 
	 * @return
	 */
	public int getLastGridX();

	/**
	 * Return the last grid position y coordinate calculated from facing
	 * direction
	 * 
	 * @return
	 */
	public int getLastGridY();

	/**
	 * Set the facing direction for the current SnakeBodyPart
	 * 
	 * @param direction
	 */
	public void setDirection(int direction);

	/**
	 * Bind corners, top-left, bottom-left, top-right, bottom-right Attach the
	 * child model to the parent one. Note that this is unsafe.
	 * 
	 * @param a
	 *            top-left
	 * @param b
	 *            bottom-left
	 * @param c
	 *            top-right
	 * @param d
	 *            bottom-right
	 */
	public void bindCorners(int a, int b, int c, int d);

	/**
	 * Corner Bindings, returns which corners can be bound. This used in
	 * accordance with bindCorners allows dynamic creation of "connected" models
	 * 
	 * @return
	 */
	public int[] cornerBindings();

	/**
	 * Forces the snake to back up because of an incorrect step (eg a wall hit)
	 */
	public void backtrack();

	/**
	 * Called when hit. This is passed on to the tail
	 */
	public void onHit();
}
