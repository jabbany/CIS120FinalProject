package renderer;
import geom3d.Model3d;
import geom3d.Tile;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;

import java.awt.Color;
import java.util.ArrayList;

import playground.GridFieldItem;

public class FieldData implements RenderableField{
	/*
	 * This contains a mapping of the 3d scene.
	 */
	private ArrayList<Edge> edges;
	private ArrayList<Point3d> vertices;
	private ArrayList<Face> faces;
	private ArrayList<Model3d> models;
	private Tile[][] tiles;
	private ArrayList<Tile> highlighted;
	private int widthx, widthy;

	public FieldData() {
		edges = new ArrayList<Edge>();
		vertices = new ArrayList<Point3d>();
		faces = new ArrayList<Face>();
		models = new ArrayList<Model3d>();
		highlighted = new ArrayList<Tile>();
	}

	public void importModel(Model3d m) {
		models.add(m);
	}
	
	public Model3d findModelByCoords(int x, int y){
		for(int i = 0; i < models.size(); i++){
			Model3d m = models.get(i);
			if(m instanceof GridFieldItem){
				GridFieldItem g = (GridFieldItem) m;
				if(g.getGridX() == x && g.getGridY() == y)
					return m;
			}
		}
		return null;
	}
	
	public void removeModel(Model3d m) {
		models.remove(m);
	}
	
	public void removeModel(int index){
		models.remove(index);
	}
	
	public void createPlayingField(int widthx, int widthy, int[][] field) {
		this.widthx = widthx;
		this.widthy = widthy;
		tiles = new Tile[widthx][widthy];
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++) {
				if(field[i][j] == -1){
					createSingleBlock(i * 10, j * 10, Color.GREEN);
				}else if(field[i][j] == -2){
					tiles[i][j] = null;
				}else if(field[i][j] == 0){
					createSingleTile(i * 10, j * 10);
				}else if(field[i][j] > 0){
					createSpecialTile(i * 10, j * 10, field[i][j]);
				}
			}
	}
	
	public void onRender() {
		for (int h = 0; h < highlighted.size(); h++) {
			Color c = highlighted.get(h).getColor();
			int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
			Color n = new Color((int) Math.ceil((255f - r) / 20) + r,
					(int) Math.ceil((255f - g) / 12) + g,
					(int) Math.ceil((255f - b) / 12) + b);
			highlighted.get(h).setColor(n);
			if (n.equals(Color.white))
				highlighted.remove(h);
		}
	}

	public boolean setHighlighted(int x, int y) {
		if(hasTile(x,y)) {
			tiles[x][y].setColor(Color.RED);
			highlighted.add(tiles[x][y]);
			return true;
		}
		return false;
	}
	
	public boolean hasTile(int x, int y){
		if (x >= 0 && y >= 0 && x < widthx && y < widthy && tiles[x][y] != null)
			return true;
		return false;
	}
	
	public int typeTile(int x, int y){
		if (x >= 0 && y >= 0 && x < widthx && y < widthy && tiles[x][y] != null)
			return tiles[x][y].type;
		return -1;
	}
	
	public Tile getTile(int x, int y){
		if (x >= 0 && y >= 0 && x < widthx && y < widthy && tiles[x][y] != null)
			return tiles[x][y];
		return null;
	}
	
	public void setTile(int x, int y, Tile t){
		if (x >= 0 && y >= 0 && x < widthx && y < widthy){
			tiles[x][y] = t;
		}
	}
	
	public void clear(){
		this.edges = new ArrayList<Edge>();
		this.faces = new ArrayList<Face>();
		this.highlighted = new ArrayList<Tile>();
		this.models = new ArrayList<Model3d>();
		this.tiles = new Tile[this.widthx][this.widthy];
		this.vertices = new ArrayList<Point3d>();
	}
	
	/**
	 * Creates a speed-up or slow-down tile
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @param type : 1 speed-up, 2 slow-down, 3 booster-row
	 */
	private void createSpecialTile(int x, int y, int type){
		/* Creates a special tile that forces speedup or slowdown */
		int index = vertices.size();
		vertices.add(new Point3d(x, y, 0));
		vertices.add(new Point3d(x, y + 8, 0));
		vertices.add(new Point3d(x + 8, y, 0));
		vertices.add(new Point3d(x + 8, y + 8, 0));
		
		if(type == 1 || type == 2 || type == 3){
			vertices.add(new Point3d(x + 1, y + 1, 0));
			vertices.add(new Point3d(x + 1, y + 7, 0));
			vertices.add(new Point3d(x + 7, y + 1, 0));
			vertices.add(new Point3d(x + 7, y + 7, 0));
		}
		
		edges.add(new Edge(index, index + 1));
		edges.add(new Edge(index, index + 2));
		edges.add(new Edge(index + 1, index + 3));
		edges.add(new Edge(index + 2, index + 3));
		
		index += 4;

		Color c;
		if(type == 1)
			c = Color.green;
		else if(type == 2)
			c = Color.red;
		else if(type == 3){
			c = Color.blue;
		}else{
			index = edges.size();
			tiles[(x + widthx * 5) / 10][(y + widthy * 5) / 10] = new Tile(
					new Edge[] { edges.get(index - 4), edges.get(index - 3),
							edges.get(index - 2), edges.get(index - 1) }, type);
			return;
		}
		edges.add(new Edge(index, index + 1, c));
		edges.add(new Edge(index, index + 2, c));
		edges.add(new Edge(index + 1, index + 3, c));
		edges.add(new Edge(index + 2, index + 3, c));
		
		index = edges.size() - 4;
		tiles[(x) / 10][(y) / 10] = new Tile(
				new Edge[] { edges.get(index - 4), edges.get(index - 3),
						edges.get(index - 2), edges.get(index - 1) }, type);
	}
	
	private void createSingleTile(int x, int y) {
		/* Creates a single tile in the grid */
		int index = vertices.size();
		vertices.add(new Point3d(x, y, 0));
		vertices.add(new Point3d(x, y + 8, 0));
		vertices.add(new Point3d(x + 8, y, 0));
		vertices.add(new Point3d(x + 8, y + 8, 0));
		edges.add(new Edge(index, index + 1));
		edges.add(new Edge(index, index + 2));
		edges.add(new Edge(index + 1, index + 3));
		edges.add(new Edge(index + 2, index + 3));
		/* Inserts it into the tiles table */
		index = edges.size();
		tiles[(x) / 10][(y) / 10] = new Tile(
				new Edge[] { edges.get(index - 4), edges.get(index - 3),
						edges.get(index - 2), edges.get(index - 1) });
	}

	private void createSingleBlock(int x, int y, Color c) {
		int index = vertices.size();
		vertices.add(new Point3d(x, y, 0));
		vertices.add(new Point3d(x, y + 8, 0));
		vertices.add(new Point3d(x + 8, y, 0));
		vertices.add(new Point3d(x + 8, y + 8, 0));

		vertices.add(new Point3d(x, y, 8));
		vertices.add(new Point3d(x, y + 8, 8));
		vertices.add(new Point3d(x + 8, y, 8));
		vertices.add(new Point3d(x + 8, y + 8, 8));
		
		faces.add(new FaceQuad(index, index + 2, index + 3, index + 1));
		faces.add(new FaceQuad(index, index + 1, index + 5, index + 4));
		faces.add(new FaceQuad(index, index + 2, index + 6, index + 4));
		faces.add(new FaceQuad(index + 7, index + 6, index + 2, index + 3));
		faces.add(new FaceQuad(index + 7, index + 5, index + 4, index + 6));
		faces.add(new FaceQuad(index + 7, index + 5, index + 1, index + 3));
		
		edges.add(new Edge(index, index + 1, c));
		edges.add(new Edge(index, index + 2, c));
		edges.add(new Edge(index + 1, index + 3, c));
		edges.add(new Edge(index + 2, index + 3, c));

		edges.add(new Edge(index + 4, index + 5, c));
		edges.add(new Edge(index + 4, index + 6, c));
		edges.add(new Edge(index + 5, index + 7, c));
		edges.add(new Edge(index + 6, index + 7, c));

		edges.add(new Edge(index, index + 4, c));
		edges.add(new Edge(index + 1, index + 5, c));
		edges.add(new Edge(index + 2, index + 6, c));
		edges.add(new Edge(index + 3, index + 7, c));

	}
	

	public int countEdges(){
		return edges.size();
	}

	public int countVertices(){
		return vertices.size();
	}
	
	public int countFaces(){
		return faces.size();
	}
	
	@SuppressWarnings("unchecked")
	public Edge[] getEdges() {
		ArrayList<Edge> edgeList = (ArrayList<Edge>) edges.clone();
		int index = vertices.size();
		for (int j = 0; j < models.size(); j++) {
			Edge[] e = models.get(j).getEdges();
			for (int i = 0; i < e.length; i++) {
				edgeList.add(new Edge(e[i].start + index, e[i].end + index,
						e[i].color));
			}
			index += models.get(j).getVertexCount();
		}
		return edgeList.toArray(new Edge[edgeList.size()]);
	}

	@SuppressWarnings("unchecked")
	public Point3d[] getVertices() {
		ArrayList<Point3d> vertexList = (ArrayList<Point3d>) vertices.clone();
		for (int j = 0; j < models.size(); j++) {
			Point3d[] e = models.get(j).getGlobalVertices();
			for (int i = 0; i < e.length; i++) {
				vertexList.add(e[i]);
			}
		}
		return vertexList.toArray(new Point3d[vertexList.size()]);
	}

	@SuppressWarnings("unchecked")
	public Face[] getFaces() {
		ArrayList<Face> faceList = (ArrayList<Face>) faces.clone();
		int index = vertices.size();
		for (int j = 0; j < models.size(); j++) {
			Face[] e = models.get(j).getFaces();
			if(e != null)
				for (int i = 0; i < e.length; i++) {
					faceList.add(e[i].cloneTo(index));
				}
			index += models.get(j).getVertexCount();
		}
		return faceList.toArray(new Face[faceList.size()]);
	}
	
	public int getWidthX(){
		return widthx;
	}
	
	public int getWidthY(){
		return widthy;
	}
	
	public boolean isUnlimitedSize() {
		return false;
	}
}
