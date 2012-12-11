package playground;

import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;
import geom3d.primitives.face.FaceTri;

import java.awt.Color;
import java.util.LinkedList;

/**
 * An edible pellet Model3d for the snake. In theory eating 2 of these would
 * yield 1 growth in length.
 * 
 * @author Jim
 * 
 */
public class PelletModel extends Model3d implements GridFieldItem {
	private int x = 0, y = 0;
	private Color faceColor = Color.ORANGE;
	private int spawnCount = 1, spawnMax = 1;
	private LinkedList<int[]> spawnLocations = new LinkedList<int[]>();
	private Face[] faces = new Face[] {
			new FaceQuad(1,2,4,3,faceColor),
			new FaceQuad(7,8,10,9,faceColor),
			new FaceQuad(1,7,9,3,faceColor),
			new FaceQuad(2,8,10,4,faceColor),
			new FaceQuad(0,6,7,1,faceColor),
			new FaceQuad(2,8,6,0,faceColor),
			new FaceQuad(5,11,9,3,faceColor),
			new FaceQuad(4,10,11,5,faceColor),
			new FaceTri(3,4,5,faceColor),
			new FaceTri(0,2,1,faceColor),
			new FaceTri(7,6,8,faceColor),
			new FaceTri(9,10,11,faceColor),
	};

	private PelletModel(Edge[] edges, Point3d[] points) {
		super(edges, points);
	}
	
	public PelletModel(int[][] spawn, int spawnTimes) {
		this(spawn, spawnTimes, Color.YELLOW);
	}
	
	public PelletModel(int[][] spawn, int spawnTimes, Color c) {
		this(c);
		this.spawnMax = spawnTimes;
		for(int i = 0; i < spawn.length; i++){
			spawnLocations.add(spawn[i]);
		}
		if(spawn.length > 0){
			respawn();
		}
	}

	public PelletModel(Color c) {
		super(new Edge[] { new Edge(0, 1, c), new Edge(0, 2, c),
				new Edge(1, 3, c), new Edge(2, 4, c), new Edge(3, 5, c),
				new Edge(4, 5, c), new Edge(6, 7, c), new Edge(6, 8, c),
				new Edge(7, 9, c), new Edge(8, 10, c), new Edge(9, 11, c),
				new Edge(10, 11, c), new Edge(6, 0, c), new Edge(7, 1, c),
				new Edge(8, 2, c), new Edge(9, 3, c), new Edge(10, 4, c),
				new Edge(11, 5, c), }, new Point3d[] {
				new Point3d(400, 300, 200, -100),
				new Point3d(200, 300, 300, -100),
				new Point3d(600, 300, 300, -100),
				new Point3d(200, 300, 500, -100),
				new Point3d(600, 300, 500, -100),
				new Point3d(400, 300, 600, -100),
				new Point3d(400, 500, 200, -100),
				new Point3d(200, 500, 300, -100),
				new Point3d(600, 500, 300, -100),
				new Point3d(200, 500, 500, -100),
				new Point3d(600, 500, 500, -100),
				new Point3d(400, 500, 600, -100) });
	}

	@Override
	public Face[] getFaces() {
		return faces;
	}

	public int getGridX() {
		return x;
	}

	public int getGridY() {
		return y;
	}
	
	public boolean canRespawn(){
		return spawnCount <= spawnMax || spawnMax == -1;
	}
	
	public int[] respawn(){
		if(canRespawn()){
			int[] location = spawnLocations.pop();
			this.setXY(location[0], location[1]);
			spawnLocations.add(location);
			spawnCount++;
			return location;
		}else{
			return null;
		}
	}
	
	@Override
	public void animate() {
		/* Turns pellets around a center axis */
		translate(-4, -4, 0);
		rotateZ(9);
		translate(4, 4, 0);
	}

	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		this.moveTo(10 * x, 10 * y, 0);
	}
}
