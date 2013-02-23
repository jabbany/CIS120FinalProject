package renderer;

import geom3d.Model2d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * This is a very naive 3d renderer, there are a few bugs. It is not fast, but
 * it gets the job done.
 * 
 * Faces support for solids is not nearly as good as edges support and there is
 * no vision field ray tracing so processing power is wasted in redraws of
 * covered faces. Also, you cannot control the order the faces are drawn other
 * than re-arranging their order in the array.
 * 
 * There is also a 2-d context mode which can overlay a 2d context and either
 * keep the 3d scene rendered or stop rendering it. This is mainly for the
 * tutorial etc.
 * 
 * @author Jim
 * 
 */
@SuppressWarnings("serial")
public class Field3d extends Component implements MouseDragCatcher {
	/**
	 * 3D Playing Field
	 */
	private RenderableField field;
	private Image buffer;
	private Graphics buffer_context;
	private Model2d context_2d;
	private Field3dMouseHandler mouse_handler;

	private static double DEG2RAD = Math.PI / 180.0;
	
	private int azimuth = 0, elevation = 130, rotz = 0;
	private double[] viewport = new double[] { 1000.0, 0.0, 0.0 };
	public int dx = 0, dy = 0;
	private Color background = Color.BLACK;

	private int cut(int a, int min, int max) {
		return Math.min(Math.max(a, min), max);
	}

	private double dotProduct(double[] parr1, int[] parr2) {
		double dotProd = 0;
		for (int i = 0; i < 3; i++) {
			dotProd += parr1[i] * parr2[i];
		}
		return dotProd;
	}

	public Field3d(RenderableField f) {
		field = f;
		mouse_handler = new Field3dMouseHandler(this);
		addMouseListener(mouse_handler);
		addMouseMotionListener(mouse_handler);
	}

	public void init() {
		buffer = createImage(getWidth(), getHeight());
		buffer_context = buffer.getGraphics();
	}

	/**
	 * Set the 2d context that overlays this 3d field Pass in null to clear the
	 * 2d context and resume with 3d painting
	 * 
	 * @param model
	 *            - 2d context
	 */
	public void set2dContext(Model2d model) {
		context_2d = model;
	}

	/**
	 * Get the 2d graphics context of the 3d field
	 * 
	 * @return the drawable 2d context for this 2d field
	 */
	public Model2d get2dContext() {
		return context_2d;
	}

	public void setRotation(int z) {
		rotz = z % 360;
	}

	public int getRotation() {
		return rotz;
	}

	public void setElevation(int elev) {
		elevation = cut(elev, 90, 180);
	}

	public int getElevation() {
		return elevation;
	}

	public RenderableField getField() {
		return field;
	}

	private void drawField(Graphics g) {
		if (context_2d != null && context_2d.standalone()) {
			context_2d.paint(g);
			return;
		}
		g.setColor(background);
		int width = getWidth(), height = getHeight();
		g.fillRect(0, 0, width, height);

		Edge[] edges = field.getEdges();
		Point3d[] vertices = field.getVertices();
		Face[] faces = field.getFaces();

		double theta = DEG2RAD * azimuth;
		double phi = DEG2RAD * elevation;
		float cosT = (float) Math.cos(theta), sinT = (float) Math.sin(theta);
		float cosP = (float) Math.cos(phi), sinP = (float) Math.sin(phi);
		float cosTcosP = cosT * cosP, cosTsinP = cosT * sinP, sinTcosP = sinT
				* cosP, sinTsinP = sinT * sinP;
		
		Point[] points = new Point[vertices.length];
		boolean[] rPoint = new boolean[vertices.length];
		int j;
		int scaleFactor = width / 100;
		float near = 100;
		float nearToObj = 1.5f;
		for (j = 0; j < points.length; ++j) {
			// initialize all points as lazy render
			rPoint[j] = false;
			double[] p = vertices[j].getPrecision();
			double x0 = p[0] + dx;
			double y0 = p[1] + dy;
			double z0 = p[2];
			
			// rotation of points in view
			double xn = x0 * Math.cos(rotz * Math.PI / 180) - y0
					* Math.sin(rotz * Math.PI / 180);
			double yn = y0 * Math.cos(rotz * Math.PI / 180) + x0
					* Math.sin(rotz * Math.PI / 180);
			
			// compute an orthographic projection
			double x1 = cosT * xn + sinT * z0;
			double y1 = -sinTsinP * xn + cosP * yn + cosTsinP * z0;
			
			// now adjust things to get a perspective projection
			double z1 = cosTcosP * z0 - sinTcosP * xn - sinP * yn;

			x1 = x1 * near / (z1 + near + nearToObj);
			y1 = y1 * near / (z1 + near + nearToObj);

			// This if is a hack to prevent overflow rendering...
			if (Math.abs(x1) * scaleFactor < width
					&& Math.abs(y1) * scaleFactor < height) {
				// the 0.5 is to round off when converting to int
				points[j] = new Point(
						(int) (width / 2 + scaleFactor * x1 + 0.5),
						(int) (height / 2 - scaleFactor * y1 + 0.5));
			}
		}

		/** NOTE: Points may be null! **/
		/** TODO: Add BETTER support for faces **/
		for (j = 0; j < field.countFaces(); ++j) {
			// See if face needs to be rendered
			if (faces[j].getNormal() != null
					&& dotProduct(viewport, faces[j].getNormal()) >= 0)
				continue; // Don't render this face
			int[] facePoints = faces[j].getPoints();
			int[] xpoints = new int[facePoints.length];
			int[] ypoints = new int[facePoints.length];
			int npoints = 0;
			for (int x = 0; x < facePoints.length; x++) {
				if (points[facePoints[x]] != null) {
					rPoint[facePoints[x]] = true;
					xpoints[npoints] = points[facePoints[x]].x;
					ypoints[npoints] = points[facePoints[x]].y;
					npoints++;
				}
			}
			g.setColor(faces[j].getColor());
			g.fillPolygon(xpoints, ypoints, npoints);
		}

		for (j = 0; j < field.countEdges(); ++j) {
			/** Do not draw a dependant edge of a face if the face was not drawn **/
			if (edges[j].dep && !(rPoint[edges[j].start] && rPoint[edges[j].end]))
				continue;
			g.setColor(edges[j].color);
			if (points[edges[j].start] != null && points[edges[j].end] != null)
				g.drawLine(points[edges[j].start].x, points[edges[j].start].y,
						points[edges[j].end].x, points[edges[j].end].y);
			//char[] a = vertices[edges[j].end].toString().toCharArray();
			//g.drawChars(a, 0, a.length, points[edges[j].end].x, points[edges[j].end].y);
		}

		/** Draws the models on top of the field **/
		for (j = field.countFaces(); j < faces.length; ++j) {
			if (faces[j].getNormal() != null
					&& dotProduct(viewport, faces[j].getNormal()) >= 0){
				continue; // Don't render this face
			}
			int[] facePoints = faces[j].getPoints();
			int[] xpoints = new int[facePoints.length];
			int[] ypoints = new int[facePoints.length];
			int npoints = 0;
			for (int x = 0; x < facePoints.length; x++) {
				if (points[facePoints[x]] != null) {
					xpoints[npoints] = points[facePoints[x]].x;
					ypoints[npoints] = points[facePoints[x]].y;
					npoints++;
				}
			}
			g.setColor(faces[j].getColor());
			g.fillPolygon(xpoints, ypoints, npoints);
		}

		for (j = field.countEdges(); j < edges.length; ++j) {
			if (edges[j].dep && !(rPoint[edges[j].start] && rPoint[edges[j].end]))
				continue;
			g.setColor(edges[j].color);
			
			if (points[edges[j].start] != null && points[edges[j].end] != null)
				g.drawLine(points[edges[j].start].x, points[edges[j].start].y,
						points[edges[j].end].x, points[edges[j].end].y);
		}

		if (context_2d != null && !context_2d.standalone()) {
			context_2d.paint(g);
		}
	}

	public void redrawBuffer() {
		drawField(buffer_context);
		field.onRender();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(640, 480);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		if (buffer != null)
			g.drawImage(buffer, 0, 0, this);
		else {
			init();
			drawField(buffer_context);
			update(g);
		}
	}

	@Override
	public void onDrag(int dx, int dy) {
		rotz -= dx * 90 / 160;
		elevation += dy * 90 / 160;
		rotz = rotz % 360;
		elevation = cut(elevation, 100, 180);
		// Recalculate the viewport
		viewport[0] = -1000 * Math.cos((elevation - 90) * DEG2RAD) * Math.sin(rotz * DEG2RAD);
		viewport[1] = -1000 * Math.cos((elevation - 90) * DEG2RAD) * Math.cos(rotz * DEG2RAD);
		viewport[2] = -1000 * Math.sin((elevation - 90) * DEG2RAD);
		if (buffer != null && buffer_context != null)
			drawField(buffer_context);
		repaint();
	}
}
