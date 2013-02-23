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

	public ThreeJSFormat(String filename) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(filename));
			String s = r.readLine();
			while (s != null) {
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
