package geom3d.primitives;

import java.awt.Color;

public class Edge {
	public int start;
	public int end;
	public Color color;
	public Edge(int startId, int endId, Color c){
		start = startId;
		end = endId;
		color = c;
	}
	
	public Edge(int startId, int endId){
		start = startId;
		end = endId;
		color = Color.WHITE;
	}
	
	public String toString(){
		return "Edge (" + start + ", " + end + ")";
	}
}
