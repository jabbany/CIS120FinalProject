package renderer;

import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;

/**
 * Unlimited field - A field that has no end and is dynamically generated
 * 
 * @author Jim
 * 
 */
public class FieldDataUnlimited implements RenderableField {
	private Point3d camera;
	private int[] facing;
	/**
	 * Sets the viewport of the unlimited field
	 * 
	 * @param camera
	 *            - This is where the camera is located
	 * @param vector
	 *            - This is where the the camera is facing. The array expects 3 elements.
	 */
	public void setViewport(Point3d camera, int[] vector) {
		if(vector.length != 3)
			return;
		this.camera = camera;
		this.facing = vector;
	}

	@Override
	public Point3d[] getVertices() {
		return null;
	}

	@Override
	public Edge[] getEdges() {
		return null;
	}

	@Override
	public Face[] getFaces() {
		return null;
	}

	@Override
	public int countEdges() {
		return 0;
	}

	@Override
	public int countVertices() {
		return 0;
	}

	@Override
	public int countFaces() {
		return 0;
	}

	@Override
	public void onRender() {
		
	}

}
