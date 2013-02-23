package geom3d.format;

import geom3d.*;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;

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
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<Face> faces = new ArrayList<Face>();
	
	/**
	 * Finds how many indices to skip
	 * @param type - Type of object
	 * @return Number of indices that the current object covers
	 */
	private int findSize(int type){
		int size = 1;
		int edgeCount;
		if(type % 2 == 0) {
			edgeCount = 3;
		}else{ 
			edgeCount = 4;
		}
		size += edgeCount;
		if((type / 2) % 2 == 1) size += 1; /* Material */
		if((type / 4) % 2 == 1) size += 1; /* Face UV */
		if((type / 8) % 2 == 1) size += edgeCount; /* Face Vertex UV */
		if((type / 16) % 2 == 1) size += 1; /* Face normal */
		if((type / 32) % 2 == 1) size += edgeCount; /* Face vertex normal */
		if((type / 64) % 2 == 1) size += 1; /* Face color*/
		if((type / 128) % 2 == 1) size += edgeCount; /* Face vertex color */
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
					while(ptr < readin.length){
						int type = Integer.parseInt(readin[ptr]);
						if(type % 2 == 0){
							int a = Integer.parseInt(readin[ptr + 1]);
							int b = Integer.parseInt(readin[ptr + 2]);
							int c = Integer.parseInt(readin[ptr + 3]);
							/* Triangle */
							edges.add(new Edge(a, b));
							edges.add(new Edge(b, c));
							edges.add(new Edge(c, a));
							ptr += findSize(type);
						}else{
							/* Quad */
							int a = Integer.parseInt(readin[ptr + 1]);
							int b = Integer.parseInt(readin[ptr + 2]);
							int c = Integer.parseInt(readin[ptr + 3]);
							int d = Integer.parseInt(readin[ptr + 4]);
							edges.add(new Edge(a, b));
							edges.add(new Edge(b, c));
							edges.add(new Edge(c, d));
							edges.add(new Edge(d, a));
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
								(int) (y * 10000), (int) (z * 10000), -10000);
						vertices.add(p);
					}
				}
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

}
