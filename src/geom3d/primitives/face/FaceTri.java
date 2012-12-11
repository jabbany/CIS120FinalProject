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

	public FaceTri(int p1, int p2, int p3) {
		a = p1;
		b = p2;
		c = p3;
		color = Color.WHITE;
	}

	public FaceTri(int p1, int p2, int p3, Color col) {
		a = p1;
		b = p2;
		c = p3;
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
		return new FaceTri(a + index, b + index, c + index, color);
	}
}
