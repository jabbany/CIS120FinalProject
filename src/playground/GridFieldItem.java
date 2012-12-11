package playground;

/**
 * GridFieldItem is an item rendered in the abstract grid coordinate system of
 * the game.
 * 
 * @author Jim
 * 
 */
public interface GridFieldItem {
	/**
	 * Resets the x,y coords of the item, and forces a move to that coordinate
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public void setXY(int x, int y);

	/**
	 * Returns x coordinate
	 * 
	 * @return x coordinate of the item in the grid
	 */
	public int getGridX();

	/**
	 * Returns y coordinate
	 * 
	 * @return y coordinate of the item in the grid
	 */
	public int getGridY();
}
