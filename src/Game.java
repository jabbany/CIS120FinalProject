import java.awt.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import map.GameMapLoader;

import renderer.Field3d;
import renderer.FieldData;

public class Game implements Runnable {
	public void run() {
		final Field3d field = new Field3d(new FieldData());
		final JFrame frame = new JFrame("3D Snakes (CIS-120 Final Project)");
		final GameRunner gr = new GameRunner(field);
		final GameMapLoader gl = new GameMapLoader("gamedata.dat");
		gr.setGameMap(gl);
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
