package geom3d;

import geom3d.primitives.Edge;

import java.awt.Color;

/**
 * Tile is an abstract representation of a flat tile on the playing field. It
 * contains data about what type of tile it is.
 * 
 * @author Jim
 * 
 */
public class Tile {
	/**
	 * These are public since they are meant to be mutable
	 * and adding getter/setter methods will be too much 
	 * hassle for no real advantage.
	 */
	public Edge[] edges;
	public int type = 0;
	
	public Tile(int type, Object reference){
		this.edges = new Edge[]{};
		this.type = type;
	}
	
	public Tile(Edge[] edges, int type) {
		this(edges);
		this.type = type;
	}

	public Tile(Edge[] edges) {
		this.edges = edges;
	}

	public void setColor(Color color) {
		for (int i = 0; i < edges.length; i++) {
			edges[i].color = color;
		}
	}
	
	public Color getColor() {
		if (edges != null && edges.length > 0) {
			return edges[1].color;
		}
		return null;
	}
}
