package playground;
import java.awt.Color;


import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;

/**
 * PowerStrip Spinning Item Model3d. This is for gaining points fast. Completing
 * a PowerStrip pushes up the point multiplier by x1. Eating a PowerStrip item
 * will never make the snake longer.
 * 
 * @author Jim
 * 
 */
public class PowerStripModel extends Model3d implements GridFieldItem {
	private int x = 0, y = 0;
	private Color color;
	private Face[] faces;

	public PowerStripModel(Color c) {
		super(new Edge[] { new Edge(0, 1, c), new Edge(0, 2, c),
				new Edge(1, 3, c), new Edge(2, 3, c) }, new Point3d[] {
				new Point3d(20, 40, 20, -10), new Point3d(60, 40, 20, -10),
				new Point3d(20, 40, 60, -10), new Point3d(60, 40, 60, -10) });
		color = c;
		faces = new Face[] { new FaceQuad(0, 1, 3, 2, color) };
	}

	public PowerStripModel(int x, int y, Color c) {
		this(c);
		this.setXY(x,y);
	}

	public Face[] getFaces() {
		return faces;
	}

	public void animate() {
		translate(-4, -4, 0);
		rotateZ(18);
		translate(4, 4, 0);
	}

	@Override
	public int getGridX() {
		return x;
	}

	@Override
	public int getGridY() {
		return y;
	}

	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		this.moveTo(10 * x, 10 * y, 0);
	}
}
