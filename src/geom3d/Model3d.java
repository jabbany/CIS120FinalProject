package geom3d;

import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;

import java.util.ArrayList;

/**
 * Model3d is a 3d mesh that is self contained in its own coordinate system.
 * 
 * The mesh also retains info on where in the scene it is placed.
 * 
 * @author Jim
 * 
 */
public class Model3d implements AnimatedItem {
	private ArrayList<Edge> edges;
	private ArrayList<Point3d> vertices;
	private Model3d child;
	private int x = 0, y = 0, z = 0;

	public Model3d(int x0, int y0, int z0, Edge[] edges, Point3d[] points) {
		this(edges, points);
		x = x0;
		y = y0;
		z = z0;
	}

	public Model3d(Edge[] edges, Point3d[] points) {
		this.edges = new ArrayList<Edge>();
		vertices = new ArrayList<Point3d>();
		for (int b = 0; b < edges.length; b++) {
			this.edges.add(edges[b]);
		}
		for (int a = 0; a < points.length; a++) {
			vertices.add(points[a]);
		}
	}

	public Edge[] getEdges() {
		if (child == null)
			return edges.toArray(new Edge[edges.size()]);
		Edge[] edgeChild = child.getEdges();
		Edge[] mine = edges.toArray(new Edge[edges.size()]);
		Edge[] ret = new Edge[mine.length + edgeChild.length];
		for (int r = 0; r < mine.length; r++) {
			ret[r] = mine[r];
		}
		for (int m = 0; m < edgeChild.length; m++) {
			ret[m + mine.length] = new Edge(edgeChild[m].start
					+ vertices.size(), edgeChild[m].end + vertices.size(),
					edgeChild[m].color);
		}
		return ret;
	}

	public Point3d[] getVertices() {
		if (child == null)
			return vertices.toArray(new Point3d[vertices.size()]);
		Point3d[] ptChild = child.getGlobalVertices();
		Point3d[] mine = vertices.toArray(new Point3d[vertices.size()]);
		Point3d[] ret = new Point3d[ptChild.length + mine.length];
		for (int r = 0; r < mine.length; r++) {
			ret[r] = mine[r];
		}
		for (int m = 0; m < ptChild.length; m++) {
			ret[m + mine.length] = new Point3d(ptChild[m]);
		}
		return ret;
	}

	/**
	 * Model3d does not support this, it may be supported in sub classes though,
	 * because faces are a pain to render
	 * 
	 * @return array of faces in object
	 */
	public Face[] getFaces() {
		return null;
	}

	/**
	 * Gets a vertex for use with self editing
	 * 
	 * @param id
	 *            vertex id
	 * @return Point3d
	 */
	public Point3d getVertex(int id) {
		return vertices.get(id);
	}

	/**
	 * Gets an edge for use with self editing
	 * 
	 * @param index
	 * @return
	 */
	public Edge getEdge(int index) {
		return edges.get(index);
	}

	/**
	 * Adds an vertex and returns its index
	 * 
	 * @param p
	 *            - Point3d Vertex
	 * @return index of vertex
	 */
	public int addVertex(Point3d p) {
		vertices.add(p);
		return vertices.size() - 1;
	}

	/**
	 * Adds an edge and returns its index
	 * 
	 * @param edge
	 * @return index of edge
	 */
	public int addEdge(Edge edge) {
		edges.add(edge);
		return edges.size() - 1;
	}

	/**
	 * Removes an edge from the model
	 * 
	 * @param index
	 */
	public void removeEdge(int index) {
		edges.remove(index);
	}

	/**
	 * Removes a vertex from the model NOTE: This is UNSAFE! It may create
	 * unconnected edges and cause ArrayBounds Exceptions later on!
	 * 
	 * @param index
	 */
	public void removeVertex(int index) {
		vertices.remove(index);
	}

	/**
	 * Safely removes a vertex from the model
	 * 
	 * @param index
	 */
	public void safeRemoveVertex(int index) {
		for (int i = edges.size() - 1; i >= 0; i--) {
			if (edges.get(i).start == index || edges.get(i).end == index) {
				removeEdge(i);
				safeRemoveVertex(index);
				return;
			}
		}
		removeVertex(index);
	}

	public Point3d[] getGlobalVertices() {
		Point3d[] v = vertices.toArray(new Point3d[vertices.size()
				+ (child != null ? child.getVertexCount() : 0)]);
		for (int i = 0; i < vertices.size(); i++) {
			Point3d p = new Point3d(v[i]);
			p.translate(x, y, z);
			v[i] = p;
		}
		if (child != null) {
			Point3d[] vc = child.getGlobalVertices();
			for (int i = 0; i < vc.length; i++) {
				v[i + vertices.size()] = vc[i];
			}
		}
		return v;
	}

	public int getVertexCount() {
		return vertices.size() + (child != null ? child.getVertexCount() : 0);
	}

	public int getEdgeCount() {
		return edges.size() + (child != null ? child.getEdgeCount() : 0);
	}

	public int getVerticesSize() {
		return vertices.size();
	}

	public int getEdgesSize() {
		return edges.size();
	}

	public void setChildModel(Model3d child) {
		this.child = child;
	}

	public Model3d getChildModel() {
		return child;
	}

	public void moveTo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void move(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void translate(int x, int y, int z) {
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).translate(x, y, z);
		}
	}

	public void rotateZ(int rotz) {
		/* Do rotation on z axis for some degrees */
		for (int i = 0; i < vertices.size(); i++) {
			int vx = vertices.get(i).x;
			int vy = vertices.get(i).y;
			int vz = vertices.get(i).z;
			double xn = Math.round(vx * Math.cos(rotz * Math.PI / 180) - vy
					* Math.sin(rotz * Math.PI / 180));
			double yn = Math.round(vy * Math.cos(rotz * Math.PI / 180) + vx
					* Math.sin(rotz * Math.PI / 180));
			vertices.get(i).apply((int) xn, (int) yn, vz);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public void animate() {
		// Does nothing, override to animate
	}
}
