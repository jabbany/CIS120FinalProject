package renderer.guitoolkit;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class TkHGroup implements RenderElement, TkComponent {
	private int x = 0, y = 0, width = 0, height = 0;
	private LinkedList<TkComponent> components = new LinkedList<TkComponent>();
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
		
	}

	@Override
	public void setHeight(int h) {
		
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
	
	public void insertElement(TkComponent c){
		int sx = x, sy = y;
		this.width = 0;
		this.height = 0;
		Iterator<TkComponent> it = this.components.iterator();
		while(it.hasNext()){
			sx += it.next().getWidth();
			this.width += it.next().getWidth();
			this.height = Math.max(this.height, it.next().getHeight());
		}
		c.setX(sx);
		c.setY(sy);
		this.width += c.getWidth();
		this.height = Math.max(this.height, c.getHeight());
		this.components.add(c);
	}
	
	@Override
	public void paint(Graphics g) {
		Iterator<TkComponent> iterator = components.iterator();
		while(iterator.hasNext()){
			iterator.next().paint(g);
		}
	}

}
