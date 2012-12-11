package geom3d.primitives.face;

import geom3d.primitives.Face;

import java.awt.Color;
/**
 * 3D quadrangle face
 * 
 * @author Jim
 *
 */
public class FaceQuad implements Face {
	public int a, b, c, d;
	public Color color;

	public FaceQuad(int p1, int p2, int p3, int p4) {
		a = p1;
		b = p2;
		c = p3;
		d = p4;
		color = new Color(140,10,140);
	}

	public FaceQuad(int p1, int p2, int p3, int p4, Color col) {
		a = p1;
		b = p2;
		c = p3;
		d = p4;
		color = col;
	}

	public int pointCount() {
		return 4;
	}

	public int[] getPoints() {
		return new int[] { a, b, c, d };
	}
	
	public Color getColor(){
		return color;
	}
	
	public Face cloneTo(int index){
		return new FaceQuad(a + index,b + index,c + index,d + index, color);
	}
}
