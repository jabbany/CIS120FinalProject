package map;

public class GameMapDataCell {
	public static int TYPE_PELLET = 3;
	public static int TYPE_POWERUP = 4;
	public static int TYPE_TUTORIAL = 5;
	private int type = 0;
	private AbstractGameItem data;
	private int x = 0, y = 0;

	public GameMapDataCell(int x, int y, int type, AbstractGameItem data) {
		this(x,y,type);
		this.data = data;
	}
	
	public GameMapDataCell(int x, int y, int type) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.data = null;
	}

	public int getType() {
		return type;
	}
	
	public AbstractGameItem getObject(){
		return data;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public GameMapDataCell clone(){
		GameMapDataCell newCell = new GameMapDataCell(x, y, type, data);
		return newCell;
	}
}
