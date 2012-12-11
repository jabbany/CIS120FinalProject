package renderer;

import geom3d.Model2d;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ListIterator;

import renderer.guitoolkit.RenderElement;

/**
 * This is the 2d game overlay used to display dialogs and other information
 * about the game.
 * 
 * @author Jim
 * 
 */

public class GameOverlay extends Model2d implements MouseClickCatcher {
	private boolean standalone = false, showStats = true;
	private LinkedList<RenderElement> drawingStack = new LinkedList<RenderElement>();
	private String gameStats = "";
	
	public void setGameStats(String text) {
		showStats = text.length() != 0;
		gameStats = text;
	}
	
	public void setStandalone(boolean standalone){
		this.standalone = standalone;
	}
	
	public void displayDialog(int x, int y, int w, int h, String content) {
		final String c = content;
		final int spx = x, spy = y, width = w, height = h;
		drawingStack.add(new RenderElement() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(spx, spy, width, height);
				g.setColor(Color.WHITE);
				g.drawRect(spx, spy, width, height);
				g.drawRect(spx + 4, spy + 4, width - 8, height - 8);
				int defTx = spx + 18, defTy = spy + 28;
				String[] m = c.split("\\^");
				for (int i = 0; i < m.length; i++) {
					g.drawChars(m[i].toCharArray(), 0, m[i].length(), defTx,
							defTy + i * 18);
				}
				String f = "(Press Any Key To Close)";
				g.drawChars(f.toCharArray(), 0, f.length(), defTx + width / 2
						- 90, defTy + height - 48);
			}
		});
	}

	public void clearLastDialog() {
		if (!drawingStack.isEmpty())
			drawingStack.removeLast();
	}

	public void displayTutorial(String text) {
		final String t = text;
		drawingStack.add(new RenderElement() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(40, 360, 600, 100);
				g.setColor(Color.WHITE);
				g.drawRect(40, 360, 600, 100);
				g.drawRect(44, 364, 600, 92);
				String[] m = t.split("\\^");
				if (m.length > 0) {
					for (int i = 0; i < m.length; i++)
						g.drawChars(m[i].toCharArray(), 0, m[i].length(), 58,
								390 + 18 * i);
				}
				String f = "(Press Any Key To Dismiss)";
				g.drawChars(f.toCharArray(), 0, f.length(), 248, 440);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		ListIterator<RenderElement> iterator = drawingStack.listIterator();
		while (iterator.hasNext()) {
			iterator.next().paint(g);
		}
		if(showStats){
			g.setColor(Color.BLACK);
			g.fillRect(8, 8, 160, 16);
			g.setColor(Color.WHITE);
			g.drawRect(8, 8, 160, 16);
			g.drawChars(gameStats.toCharArray(), 0, gameStats.length(), 10, 20);
		}
	}

	@Override
	public boolean standalone() {
		return standalone;
	}

	@Override
	public void onClick(int x, int y) {

	}
}
