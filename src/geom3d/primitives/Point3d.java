package geom3d.primitives;

/**
 * Native 3d Point/Vector. Can be up-scaled/down-scaled to provide higher
 * rendering precision.
 * 
 * @author Jim
 * 
 */
public class Point3d {
	public int x, y, z;
	public int scale = 1;
	
	public Point3d(int x, int y, int z, int scale) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.scale = scale;
	}

	public Point3d(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3d(Point3d p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
		this.scale = p.scale;
	}

	public double[] getPrecision() {
		if (scale == 0)
			return new double[] { 0, 0, 0 };
		if (scale > 0)
			return new double[] { (double) this.x * scale,
					(double) this.y * scale, (double) this.z * scale };
		else
			return new double[] { (double) this.x / -scale,
					(double) this.y / -scale, (double) this.z / -scale };
	}

	public void translate(int x, int y, int z) {
		if (scale == 0)
			return;
		if (scale < 0) {
			this.x += x * -scale;
			this.y += y * -scale;
			this.z += z * -scale;
		} else {
			this.x += x / scale;
			this.y += y / scale;
			this.z += z / scale;
		}
	}

	public void apply(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
