package renderer;

import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;

public interface RenderableField {
	/**
	 * Returns all vertices in a field that need to be rendered This may or may
	 * not return the full set of vertices depending on the implementation of
	 * the field.
	 * 
	 * It is guaranteed to return enough vertices.
	 * 
	 * @return An array of Point3d
	 */
	public Point3d[] getVertices();

	/**
	 * Returns all edges in a field that need to be rendered This may or may not
	 * return the full set of edges in the field.
	 * 
	 * It is guaranteed to return enough edges. All edges returned will have
	 * vertices in the vertex list.
	 * 
	 * @return An array of Edge
	 */
	public Edge[] getEdges();

	/**
	 * Returns all faces in a field that need to be rendered This may or may not
	 * return the full set of faces in the field.
	 * 
	 * It is guaranteed to return enough faces. All faces returned will have
	 * corresponding vertices in the vertex list.
	 * 
	 * @return An array of Face
	 */
	public Face[] getFaces();

	/**
	 * Returns the number of edges to be rendered
	 * 
	 * @return number of edges
	 */
	public int countEdges();

	/**
	 * Returns the number of vertices to be rendered
	 * 
	 * @return number of vertices
	 */
	public int countVertices();

	/**
	 * Returns the number of faces to be rendered
	 * 
	 * @return number of faces.
	 */
	public int countFaces();

	/**
	 * This method is called each time a field has been completely rendered.
	 */
	public void onRender();
	
	/**
	 * Clear all existing models and visual elements from the field.
	 */
	public void clear();
}
