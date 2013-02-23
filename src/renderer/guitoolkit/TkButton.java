package renderer.guitoolkit;

import java.awt.Color;
import java.awt.Graphics;

public class TkButton implements TkComponent {
	private int x = 0, y = 0;
	private int height = 0, width = 0;
	private String text = "";
	/**
	 * Button mode:
	 * 0 - Normal
	 * 1 - Hover
	 * 2 - Click
	 */
	private int mode = 0;
	
	public TkButton(String text){
		this.text = text;
	}
	
	public TkButton(String text, int x, int y){
		this(text);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
	}
	
	@Override
	public void setX(int newX){
		this.x = newX;
	}
	
	@Override
	public void setY(int newY){
		this.y = newY;
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
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
	
}
