package geom3d.primitives;

import java.awt.Color;

/**
 * Interface for representing a 3d "face" such as a triangle or quadrangle
 * Points must be in-order of rendering.
 * 
 * @author Jim
 * 
 */
public interface Face {
	/**
	 * Returns number of points (vertices) the face has
	 * 
	 * @return vertices
	 */
	public int pointCount();

	/**
	 * Returns an array of the index of those points (vertices)
	 * 
	 * @return integer array
	 */
	public int[] getPoints();

	/**
	 * Returns the color this face should be rendered in
	 * 
	 * @return Color
	 */
	public Color getColor();

	/**
	 * Clones a Face into another indexing aspect.
	 * 
	 * @param index
	 *            - where the new indexing starts
	 * @return
	 */
	public Face cloneTo(int index);
}
