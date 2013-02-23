package geom3d.format;

import geom3d.*;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;
import geom3d.primitives.face.FaceTri;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Three.js formatted 3d mesh importer. Just here for fun. It feels good to be
 * able to import a generic 3d mesh!
 * 
 * @author Jim
 * 
 */

public class ThreeJSFormat {
	private ArrayList<Point3d> vertices = new ArrayList<Point3d>();
	private ArrayList<Point3d> normals = new ArrayList<Point3d>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<Face> faces = new ArrayList<Face>();

	private class FacesModel extends Model3d {
		private Face[] faces;

		public FacesModel(Edge[] edges, Point3d[] points, Face[] faces) {
			super(edges, points);
			this.faces = faces;
		}

		@Override
		public Face[] getFaces() {
			return faces;
		}
	}
	
	private int[] combineNormals(int[] al){
		int[] normal = new int[]{0,0,0};
		for(int i = 0; i < al.length; i++){
			Point3d p = normals.get(al[i]);
			normal[0] += p.x;
			normal[1] += p.y;
			normal[2] += p.z;
		}
		return normal;
	}
	
	private int faceNormal(int type){
		int size = 0;
		int edgeCount;
		if (type % 2 == 0) {
			edgeCount = 3;
		} else {
			edgeCount = 4;
		}
		size += edgeCount;
		if ((type / 2) % 2 == 1)
			size += 1; /* Material */
		if ((type / 4) % 2 == 1)
			size += 1; /* Face UV */
		if ((type / 8) % 2 == 1)
			size += edgeCount; /* Face Vertex UV */
		if ((type / 16) % 2 == 1)
			return size + 1; /* Face normal */
		return -1;
	}
	
	private int[] vertexNormal(int type){
		int size = 0;
		int edgeCount;
		if (type % 2 == 0) {
			edgeCount = 3;
		} else {
			edgeCount = 4;
		}
		size += edgeCount;
		if ((type / 2) % 2 == 1)
			size += 1; /* Material */
		if ((type / 4) % 2 == 1)
			size += 1; /* Face UV */
		if ((type / 8) % 2 == 1)
			size += edgeCount; /* Face Vertex UV */
		if ((type / 16) % 2 == 1)
			size += 1; /* Face normal */
		if ((type / 32) % 2 == 1){
			int[] vn = new int[edgeCount];
			for(int j = 0; j < edgeCount; j++){
				vn[j] = size + 1 + j;
			}
			return vn;
		}
		return null;
	}
	
	/**
	 * Finds how many indices to skip
	 * 
	 * @param type
	 *            - Type of object
	 * @return Number of indices that the current object covers
	 */
	private int findSize(int type) {
		int size = 1;
		int edgeCount;
		if (type % 2 == 0) {
			edgeCount = 3;
		} else {
			edgeCount = 4;
		}
		size += edgeCount;
		if ((type / 2) % 2 == 1)
			size += 1; /* Material */
		if ((type / 4) % 2 == 1)
			size += 1; /* Face UV */
		if ((type / 8) % 2 == 1)
			size += edgeCount; /* Face Vertex UV */
		if ((type / 16) % 2 == 1)
			size += 1; /* Face normal */
		if ((type / 32) % 2 == 1)
			size += edgeCount; /* Face vertex normal */
		if ((type / 64) % 2 == 1)
			size += 1; /* Face color */
		if ((type / 128) % 2 == 1)
			size += edgeCount; /* Face vertex color */
		return size;
	}

	public ThreeJSFormat(String filename) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(filename));
			String s = r.readLine();
			while (s != null) {
				String[] spl = s.split(":");
				switch (spl[0]) {
				case "faces": {
					String[] readin = spl[1].split(",");
					int ptr = 0;
					while (ptr < readin.length) {
						int type = Integer.parseInt(readin[ptr]);
						if (type % 2 == 0) {
							int a = Integer.parseInt(readin[ptr + 1]);
							int b = Integer.parseInt(readin[ptr + 2]);
							int c = Integer.parseInt(readin[ptr + 3]);
							/* Triangle */
							edges.add(new Edge(a, b, true));
							edges.add(new Edge(b, c, true));
							edges.add(new Edge(c, a, true));
							/* Add face */
							int fn = faceNormal(type);
							int[] vn = vertexNormal(type);
							if(fn > 0){
								int[] normal = new int[3];
								Point3d p = normals.get(Integer.parseInt(readin[ptr + fn]));
								normal[0] = p.x;
								normal[1] = p.y;
								normal[2] = p.z;
								faces.add(new FaceTri(a, b, c, normal, Color.YELLOW));
							}else if(vn != null){
								for(int i = 0; i < vn.length; i++){
									vn[i] = Integer.parseInt(readin[ptr + vn[i]]);
								}
								int[] normal = combineNormals(vn);
								faces.add(new FaceTri(a, b, c, normal, Color.YELLOW));
							}else{
								faces.add(new FaceTri(a, b, c, Color.YELLOW));
							}

							ptr += findSize(type);
						} else {
							/* Quad */
							int a = Integer.parseInt(readin[ptr + 1]);
							int b = Integer.parseInt(readin[ptr + 2]);
							int c = Integer.parseInt(readin[ptr + 3]);
							int d = Integer.parseInt(readin[ptr + 4]);
							/* Add Edges */
							edges.add(new Edge(a, b, true));
							edges.add(new Edge(b, c, true));
							edges.add(new Edge(c, d, true));
							edges.add(new Edge(d, a, true));
							
							/* Add face */
							int fn = faceNormal(type);
							int[] vn = vertexNormal(type);
							if(fn > 0){
								int[] normal = new int[3];
								Point3d p = normals.get(Integer.parseInt(readin[ptr + fn]));
								normal[0] = p.x;
								normal[1] = p.y;
								normal[2] = p.z;
								faces.add(new FaceQuad(a, b, c, d, normal, Color.YELLOW));
							}else if(vn != null){
								for(int i = 0; i < vn.length; i++){
									vn[i] = Integer.parseInt(readin[ptr + vn[i]]);
								}
								int[] normal = combineNormals(vn);
								faces.add(new FaceQuad(a, b, c, d, normal, Color.YELLOW));
							}else{
								faces.add(new FaceQuad(a, b, c, d, Color.YELLOW));
							}
							ptr += findSize(type);
						}
					}
				}
					break;
				case "vertices": {
					String[] readin = spl[1].split(",");
					for (int i = 0; i < readin.length / 3; i++) {
						float x = Float.parseFloat(readin[3 * i]);
						float y = Float.parseFloat(readin[3 * i + 1]);
						float z = Float.parseFloat(readin[3 * i + 2]);
						Point3d p = new Point3d((int) (x * 10000),
								(int) (y * 10000), (int) (z * 10000), -500);
						vertices.add(p);
					}
				}
					break;
				case "normals": {
					String[] readin = spl[1].split(",");
					for (int i = 0; i < readin.length / 3; i++) {
						float x = Float.parseFloat(readin[3 * i]);
						float y = Float.parseFloat(readin[3 * i + 1]);
						float z = Float.parseFloat(readin[3 * i + 2]);
						Point3d p = new Point3d((int) (x * 10000),
								(int) (y * 10000), (int) (z * 10000));
						normals.add(p);
					}
				}
					break;
				default:
					/** This is not supported by the renderer so meh **/
				}
				s = r.readLine();
			}
			r.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not Found");
		} catch (IOException e) {
			System.out.println("What did you do again!?");
		}
	}

	/**
	 * Turns this file into a Model3d to use in the game. NOTE: This only
	 * changes it into a general Model3d.
	 * 
	 * @return Model3d mesh model
	 */
	public Model3d toModel3d() {
		return new Model3d(0, 0, 0, edges.toArray(new Edge[edges.size()]),
				vertices.toArray(new Point3d[vertices.size()]));
	}

	public Model3d toFaceModel3d() {
		return new FacesModel(edges.toArray(new Edge[edges.size()]),
				vertices.toArray(new Point3d[vertices.size()]),
				faces.toArray(new Face[faces.size()]));
	}
}
