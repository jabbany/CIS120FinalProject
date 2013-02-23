package geom3d.primitives;

import java.awt.Color;

public class Edge {
	public int start;
	public int end;
	public Color color;
	public boolean dep = false;
	
	public Edge(int startId, int endId, Color c){
		start = startId;
		end = endId;
		color = c;
	}
	
	public Edge(int startId, int endId){
		this(startId, endId, Color.WHITE);
	}
	
	public Edge(int startId, int endId, boolean dep){
		this(startId, endId);
		this.dep = dep;
	}
	
	public String toString(){
		return "Edge (" + start + ", " + end + ")";
	}
}
