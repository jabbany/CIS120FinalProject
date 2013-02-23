package geom3d.primitives.face;

import geom3d.primitives.Face;

import java.awt.Color;
/**
 * 3d Triangle face
 * 
 * @author Jim
 *
 */
public class FaceTri implements Face {
	public int a, b, c;
	public Color color;
	public int[] normal;
	
	public FaceTri(int p1, int p2, int p3) {
		this(p1,p2,p3, Color.WHITE);
	}

	public FaceTri(int p1, int p2, int p3, Color col) {
		a = p1;
		b = p2;
		c = p3;
		color = col;
	}
	
	public FaceTri(int p1, int p2, int p3, int[] normal) {
		this(p1, p2, p3);
		this.normal = normal;
	}
	public FaceTri(int p1, int p2, int p3, int[] normal, Color col) {
		this(p1, p2, p3, normal);
		color = col;
	}

	public int pointCount() {
		return 3;
	}

	public int[] getPoints() {
		return new int[] { a, b, c };
	}
	
	public Color getColor(){
		return color;
	}
	
	public Face cloneTo(int index){
		return new FaceTri(a + index, b + index, c + index, normal, color);
	}
	
	public int[] getNormal(){
		return normal;
	}
}
