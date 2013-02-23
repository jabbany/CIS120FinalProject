package renderer;

import geom3d.Model2d;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ListIterator;

import renderer.guitoolkit.RenderElement;
import renderer.guitoolkit.TkDialog;

/**
 * This is the 2d game overlay used to display dialogs and other information
 * about the game.
 * 
 * This is a really basic gui toolkit built on top of 2d renderer
 * 
 * @author Jim
 * 
 */

public class GameOverlay extends Model2d implements MouseClickCatcher {
	private boolean standalone = false, showStats = true;
	private LinkedList<RenderElement> drawingQueue = new LinkedList<RenderElement>();
	private String gameStats = "";
	private int width = 640, height = 480;
	
	public void setGameStats(String text) {
		showStats = text.length() != 0;
		gameStats = text;
	}
	
	public void setStandalone(boolean standalone){
		this.standalone = standalone;
	}
	
	public void displayMenu(){
		this.showStats = false;
		
		drawingQueue.add(new RenderElement(){
			@Override
			public void paint(Graphics g) {
				
			}
		});
	}
	
	public void displayDialog(int x, int y, int w, int h, String content) {
		drawingQueue.add(new TkDialog(content, x, y, w, h));
	}
	
	public void displayCenteredDialog(int w, int h, String content){
		displayDialog((width - w)/2, (height - h)/2, w, h, content);
	}

	public void clearLastDialog() {
		if (!drawingQueue.isEmpty())
			drawingQueue.removeLast();
	}

	public void displayTutorial(String text) {
		final String t = text;
		drawingQueue.add(new RenderElement() {
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
		ListIterator<RenderElement> iterator = drawingQueue.listIterator();
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
