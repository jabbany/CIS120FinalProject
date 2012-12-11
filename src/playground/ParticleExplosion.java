package playground;

import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Point3d;
/**
 * Particle Explosion Effect for when the snake hits some item that can 
 * explode (eg power strip, breakable tile)
 * 
 * @author Jim
 *
 */
public class ParticleExplosion extends Model3d implements GridFieldItem {

	public ParticleExplosion(Edge[] edges, Point3d[] points) {
		super(edges, points);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void animate(){
		
	}
	
	@Override
	public int getGridX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGridY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setXY(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
