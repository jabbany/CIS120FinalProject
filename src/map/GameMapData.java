package map;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * This is the abstract representation of all the things in the game!
 * Have fun playing~ 
 * 
 * @author Jim
 *
 */
public class GameMapData {
	private int type = 0;
	private int widthx = 0, widthy = 0;
	private int sx = 0, sy = 0;
	private String name;
	private int[][] tiles;
	private ArrayList<GameMapDataCell> cells = new ArrayList<GameMapDataCell>();
	private ArrayList<AbstractInvoker> invokers = new ArrayList<AbstractInvoker>();
	private GameMapDataCell working = null;
	
	public GameMapData(int fieldType, String name){
		type = fieldType;
		this.name = name;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public void setDimensions(int widthx, int widthy){
		this.widthx = widthx;
		this.widthy = widthy;
		tiles = new int[widthx][widthy];
		if(type == 0)
			fillAllTiles();
	}
	
	public void setStartingPoint(int x, int y){
		this.sx = x;
		this.sy = y;
	}
	
	public void fillAllTiles(){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j] = 0;
			}
		}
	}
	
	public void setTile(int x, int y, int type){
		if(x>=0 && y>=0 && x < widthx && y < widthy)
			tiles[x][y] = type;
	}
	
	public int[][] getTiles(){
		return tiles;
	}
	
	public int getY(){
		return widthy;
	}
	
	public int getX(){
		return widthx;
	}
	
	public int getStartX(){
		return sx;
	}
	
	public int getStartY(){
		return sy;
	}
	
	public String getName(){
		return name;
	}
	
	public void addInvoker(AbstractInvoker i){
		this.invokers.add(i);
	}
	
	public void addCell(int x, int y, int type){
		this.cells.add(new GameMapDataCell(x, y, type));
	}
	
	public void addCell(int x, int y, int type, Object data){
		final Object d = data;
		final int t = type;
		this.cells.add(new GameMapDataCell(x,y, type, new AbstractGameItem(){
			@Override
			public Object getItem() {
				return d;
			}

			@Override
			public int getType() {
				return t;
			}
			
		}));
	}
	
	public void loadCell(int x, int y){
		for(int i = 0; i < cells.size(); i++){
			if(cells.get(i).getX() == x && cells.get(i).getY() == y){
				this.working = cells.get(i);
				return;
			}
		}
		this.working = null;
	}
	
	public boolean hasCell(int x, int y){
		if(working == null || working.getX() != x || working.getY() != y)
			loadCell(x,y);
		return this.working != null;
	}
	
	public int cellType(int x, int y){
		if(hasCell(x,y))
			return working.getType();
		return -1;
	}
	
	public AbstractGameItem cellData(int x, int y){
		if(!hasCell(x,y))
			return null;
		return working.getObject();
	}
	
	public void moveCell(int x, int y, int nx, int ny){
		if(hasCell(x,y))
			working.setXY(nx,ny);
	}
	
	public void removeCell(int x, int y){
		if(hasCell(x,y)){
			cells.remove(working);
			working = null;
			for(int i = 0; i < invokers.size(); i++){
				GameMapDataCell[] c = invokers.get(i).invoke(x, y);
				if(c != null && c.length > 0){
					for(int j = 0; j < c.length; j++){
						this.cells.add(c[j]);
					}
					invokers.remove(i);
					break;
				}
			}
		}
	}
	
	public AbstractGameItem[] getAllObjects(){
		HashSet<Object> objs = new HashSet<Object>(); // This set only retains references
		ArrayList<AbstractGameItem> objects = new ArrayList<AbstractGameItem>();
		for(int i = 0; i < cells.size(); i++){
			AbstractGameItem o = cells.get(i).getObject();
			if(!objs.contains(o.getItem())){
				objects.add(o);
				objs.add(o.getItem());
			}
		}
		return objects.toArray(new AbstractGameItem[objects.size()]); 
	}
	
	@SuppressWarnings("unchecked")
	public GameMapData clone(){
		GameMapData gd = new GameMapData(type, name);
		/** Deep Clones **/
		gd.cells = new ArrayList<GameMapDataCell>();
		for(int i = 0; i < this.cells.size(); i++){
			gd.cells.add(this.cells.get(i).clone());
		}
		gd.invokers = (ArrayList<AbstractInvoker>) this.invokers.clone();
		gd.sx = sx;
		gd.sy = sy;
		gd.widthx = widthx;
		gd.widthy = widthy;
		gd.tiles = this.tiles;
		return gd;
	}
}
