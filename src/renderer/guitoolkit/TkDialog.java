package renderer.guitoolkit;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import renderer.guitoolkit.TkEventListener;

public class TkDialog implements TkComponent {
	private int x = 0, y = 0, width = 0, height = 0;
	private String text = "";
	private ArrayList<TkEventListener> listeners = new ArrayList<TkEventListener>();
	
	public TkDialog(String text){
		this.text = text;
	}
	
	public TkDialog(String text, int x, int y){
		this(text);
		this.x = x;
		this.y = y;
	}
	
	public TkDialog(String text, int x, int y, int w, int h){
		this(text, x, y);
		this.width = w;
		this.height = h;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
		g.drawRect(x + 4, y + 4, width - 8, height - 8);
		int defTx = x + 18, defTy = y + 28;
		String[] m = text.split("\\^");
		for (int i = 0; i < m.length; i++) {
			g.drawChars(m[i].toCharArray(), 0, m[i].length(), defTx,
					defTy + i * 18);
		}
		String f = "(Press Any Key To Close)";
		int fw = 
				g.getFontMetrics().charsWidth(
						f.toCharArray(), 0, f.length());
		g.drawChars(f.toCharArray(), 0, f.length(), (width - fw) / 2 + x, defTy + height - 48);
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setWidth(int w) {
		this.width = w;
	}

	@Override
	public void setHeight(int h) {
		this.height = h;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void dispatchEvent(TkEvent event) {
		if(event.getType() == "click"){
			for(int i = 0; i < listeners.size(); i++){
				listeners.get(i).onAction(event);
			}
		}
	}

	@Override
	public void addEventListener(String listenerType, TkEventListener listener) {
		if(listenerType == "click")
			listeners.add(listener);
	}
}
