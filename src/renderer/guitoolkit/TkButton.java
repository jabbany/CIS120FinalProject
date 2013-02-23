package renderer.guitoolkit;

import java.awt.Color;
import java.awt.Graphics;

public class TkButton implements TkComponent {
	private int x = 0, y = 0;
	private int height = 0, width = 0;
	private String text = "";
	/**
	 * Button mode: 0 - Normal 1 - Hover 2 - Click
	 */
	private int mode = 0;

	public TkButton(String text) {
		this.text = text;
	}

	public TkButton(String text, int x, int y) {
		this(text);
		this.x = x;
		this.y = y;
	}

	@Override
	public void paint(Graphics g) {
		int widthText = g.getFontMetrics().charsWidth(text.toCharArray(), 0,
				text.length());
		if (width == 0)
			width = widthText + 4;
		g.setColor(mode == 0 ? Color.BLACK : Color.WHITE);
		g.fillRect(x, y, width, height);
		g.setColor(mode == 0 || mode == 1 ? Color.WHITE : Color.BLACK);
		g.drawRect(x, y, width, height);
		if (mode == 1)
			g.setColor(Color.GRAY);
		g.drawChars(text.toCharArray(), 0, text.length(), x
				+ (width - widthText) / 2, y + 2);
	}

	@Override
	public void setX(int newX) {
		this.x = newX;
	}

	@Override
	public void setY(int newY) {
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

	@Override
	public void dispatchEvent(TkEvent e) {

	}

	@Override
	public void addEventListener(String eventListener, TkEventListener listener) {

	}

}
