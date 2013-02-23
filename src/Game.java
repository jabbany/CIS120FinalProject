import java.awt.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import map.GameMapLoader;

import renderer.Field3d;
import renderer.FieldData;
import renderer.RenderableField;

public class Game implements Runnable {
	public void run() {
		final RenderableField fieldData = new FieldData();
		final GameMapLoader gl = new GameMapLoader("gamedata.dat");
		
		final Field3d field = new Field3d(fieldData);
		
		final GameRunner gr = new GameRunner(field, fieldData, gl);
		
		final JFrame frame = new JFrame("3D Snakes (CIS-120 Final Project)");
		gr.setGameLevel(0);
		gr.initField();
		frame.add(field, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.addKeyListener(gr);
		/** Pack it **/
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		gr.start();
		frame.repaint();

	}

	/*
	 * Get the game started!
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}
