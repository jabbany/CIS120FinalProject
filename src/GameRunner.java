import geom3d.AnimatedItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Timer;

import map.AbstractGameItem;
import map.GameMapData;
import map.GameMapLoader;
import playground.PelletModel;
import playground.PowerStrip;
import playground.PowerStripModel;
import playground.TutorialInvoker;
import playground.snake.SnakeModel;
import renderer.Field3d;
import renderer.FieldData;
import renderer.GameOverlay;

/**
 * Game Runner - this is where all the gaming logic is
 * 
 * @author Jim
 * 
 */
public class GameRunner implements KeyListener {
	private Timer timer, renderTimer;
	private Field3d field;
	private GameOverlay gameOverlay;
	private FieldData fieldData;
	private SnakeModel snake;
	private ArrayList<AnimatedItem> animated;
	private GameMapData gameLevelData;
	private GameMapLoader gameLoader;
	
	private int rotz;
	private int snakeSpeed = 2;
	private int currentTileType = 0;
	private int lastX = 0, lastY = 0;
	private boolean fieldInited = false;
	private boolean autoTurn = true, tutorialMode = false;
	// private PowerStrip runnngPowerStrip = null;

	private int lives = 3, points = 0, multiplier = 1;

	public GameRunner(Field3d field) {
		this.rotz = 0;
		this.field = field;
		this.fieldData = field.getFieldData();
		this.animated = new ArrayList<AnimatedItem>();
		this.gameLevelData = new GameMapData(0, "Test");
		/** This is initting the GameLevelData **/

		this.gameOverlay = new GameOverlay();
		field.set2dContext(this.gameOverlay);

		/** Create Game Logic Timer **/
		timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});

		/** Create Rendering Timer **/
		final Field3d f = field;
		renderTimer = new Timer(25, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.repaint();
			}
		});
		renderTimer.start();

		/** Update display **/
		onStatsChange();
	}
	
	public void setGameMap(GameMapLoader loader){
		this.gameLoader = loader;
	}
	
	public void setGameLevel(int level) {
		if(this.gameLoader != null)
			gameLevelData = gameLoader.getLevel(level);
		else
			gameLevelData = null;
	}

	/**
	 * Initialize the playing field
	 */
	public void initField() {
		this.fieldData.clear();
		this.animated.clear();
		if (gameLevelData == null) {
			gameOverlay.displayDialog(10, 10, 620, 460,
					"[Error] Missing or illegal game data file.^"
							+ "Please provide a correct gamedata.dat file!");
			gameOverlay.setGameStats("");
			return;
		}
		this.fieldData.createPlayingField(gameLevelData.getX(),
				gameLevelData.getY(), gameLevelData.getTiles());

		snake = new SnakeModel(gameLevelData.getStartX(),
				gameLevelData.getStartY());
		this.fieldData.importModel(snake);
		/** This is terrible but it is run only once each game load **/
		AbstractGameItem[] allDataCells = gameLevelData.getAllObjects();
		for (AbstractGameItem allDataCell : allDataCells) {
			switch (allDataCell.getType()) {
			case 3:
				this.fieldData.importModel((PelletModel) allDataCell.getItem());
				this.animated.add((PelletModel) allDataCell.getItem());
				break;
			case 4:
				PowerStrip ps = (PowerStrip) allDataCell.getItem();
				PowerStripModel[] elems = ps.getElements();
				for (int j = 0; j < elems.length; j++) {
					this.fieldData.importModel(elems[j]);
					this.animated.add(elems[j]);
				}
				break;
			}
		}
		fieldInited = true;
		onStatsChange();
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	/**
	 * Decreases life count and processes deaths
	 */
	public void decreaseLife() {
		if(lives > 0){
			multiplier = 1;
			lives--;
			onStatsChange();
			if(lives == 0){
				points = 0;
				multiplier = 1;
				gameOverlay.displayDialog(100, 200, 440, 80, "Oh dear, You Died. Press any key to Restart Level.");
			}
			
		}
	}

	/**
	 * Increases the life count
	 */
	public void increaseLife() {
		lives++;
		onStatsChange();
	}
	
	public boolean isAlive(){
		return lives > 0;
	}
	
	public void turnField() {
		rotz = snake.facingRotZ();
		if (rotz != field.getRotation()) {
			int curRot = Math.abs(field.getRotation() - rotz) > 180 ? (360 + field
					.getRotation()) : field.getRotation();
			field.setRotation((int) Math.ceil((curRot - rotz) / 2) + rotz);
		}
	}

	/**
	 * Hit tracker, tracks the snake to see if it hast hit
	 * 
	 * @param x
	 *            - grid coordinate x of the snake
	 * @param y
	 *            - grid coordinate y of the snake
	 */
	public void hitTracker(int x, int y) {
		if (!fieldData.setHighlighted(x, y)) {
			int facing = snake.faceDirection();
			snake.backtrack();
			snake.shrinkAll();
			snake.onHit();
			decreaseLife();
			if (facing == 0 || facing == 2) {
				if (!fieldData.hasTile(x + (facing == 0 ? -1 : 1), y - 1)) {
					snake.turn(facing == 0 ? 1 : -1);
				} else
					snake.turn(facing == 0 ? -1 : 1);
			} else {
				if (!fieldData.hasTile(x - 1, y + (facing == 1 ? -1 : 1))) {
					snake.turn(facing == 1 ? -1 : 1);
				} else
					snake.turn(facing == 1 ? 1 : -1);
			}
		}

		if (snake.isTail(x, y)) {
			snake.backtrack();
			snake.shrinkAll();
			snake.onHit();
			decreaseLife();
			snake.turn(1);
		}
	}

	/**
	 * Tracks entry to special tiles
	 * 
	 * @param x
	 *            grid coordinate x of the snake
	 * @param y
	 *            grid coordinate y of the snake
	 */
	public void tileTracker(int x, int y) {
		int tileTypeNew = fieldData.typeTile(x, y);
		if (tileTypeNew != currentTileType) {
			switch (tileTypeNew) {
			case 1: {
				snakeSpeed = 4;
			}
				break;
			case 2: {
				snakeSpeed = 1;
			}
				break;
			case 0:
			default: {
				snakeSpeed = 2;
			}
			}
			currentTileType = tileTypeNew;
		}
		/** Now check if we are on top of an object **/
		if (gameLevelData.hasCell(x, y)) {
			int type = gameLevelData.cellType(x, y);
			switch (type) {
			case 3: {
				PelletModel p = (PelletModel) gameLevelData.cellData(x, y)
						.getItem();
				if (p.canRespawn()) {
					int[] pos = p.respawn();
					snake.grow();
					points += 10 * multiplier;
					onStatsChange();
					gameLevelData.moveCell(x, y, pos[0], pos[1]);
				} else {
					gameLevelData.removeCell(x, y);
					fieldData.removeModel(p);
					this.animated.remove(p);
					points += 10 * multiplier;
					snake.grow();
					onStatsChange();
				}
			}
				break;
			case 4: {
				PowerStrip ps = (PowerStrip) gameLevelData.cellData(x, y)
						.getItem();
				PowerStripModel pm = ps.getModel(x, y);
				if (pm != null) {
					this.animated.remove(pm);
					this.fieldData.removeModel(pm);
					ps.removeElement(x, y);
					points += 5 * multiplier;
					if (ps.remainingElements() == 0) {
						multiplier++;
						snake.grow();
					}
					onStatsChange();
				}
				gameLevelData.removeCell(x, y);
			}
				break;
			case 5: {
				TutorialInvoker s = (TutorialInvoker) gameLevelData.cellData(x,
						y).getItem();
				tutorialMode = true;
				gameOverlay.displayTutorial(s.getText());
				int[][] pos = s.getLocations();
				for (int i = 0; i < pos.length; i++) {
					gameLevelData.removeCell(pos[i][0], pos[i][1]);
				}
			}
			}
		}
	}

	/**
	 * Game Loop
	 */
	public void tick() {
		if (!this.fieldInited)
			return;
		
		if (autoTurn)
			turnField();

		snake.animate(snakeSpeed);

		/** Follow The Snake **/
		field.dx = 0 - snake.getX();
		field.dy = 0 - snake.getY();

		/** Animate Everything Else **/
		for (int x = 0; x < animated.size(); x++) {
			animated.get(x).animate();
		}
		int x = snake.getGridX(), y = snake.getGridY();
		if (lastX != x || lastY != y) {
			/** Decreases check load **/
			hitTracker(x, y);
			if(isAlive())
				tileTracker(x, y);
			lastX = x;
			lastY = y;
		}

		field.redrawBuffer();
		if (tutorialMode)
			timer.stop();
	}

	@Override
	public void keyPressed(KeyEvent k) {
		if (!fieldInited)
			return;
		if (tutorialMode) {
			gameOverlay.clearLastDialog();
			timer.start();
			tutorialMode = false;
			return;
		}
		if (k.getKeyCode() == KeyEvent.VK_SPACE) {
			if (timer.isRunning()) {
				timer.stop();
			} else {
				timer.start();
			}
		}
		/* Only allow controls if game not paused! * */
		if (timer.isRunning()) {
			if (k.getKeyCode() == KeyEvent.VK_LEFT
					|| k.getKeyCode() == KeyEvent.VK_KP_LEFT)
				snake.turn(-1);
			else if (k.getKeyCode() == KeyEvent.VK_RIGHT
					|| k.getKeyCode() == KeyEvent.VK_KP_RIGHT)
				snake.turn(1);
			else if (k.getKeyCode() == KeyEvent.VK_UP
					|| k.getKeyCode() == KeyEvent.VK_KP_UP) {
				if (currentTileType == 2)
					snakeSpeed = 2;
				else
					snakeSpeed = 4;
			} else if (k.getKeyCode() == KeyEvent.VK_DOWN
					|| k.getKeyCode() == KeyEvent.VK_KP_DOWN) {
				if (currentTileType == 1)
					snakeSpeed = 2;
				else
					snakeSpeed = 1;
			} else if (k.getKeyCode() == KeyEvent.VK_2) {
				field.setElevation(180);
			} else if (k.getKeyCode() == KeyEvent.VK_3) {
				field.setElevation(130);
			} else if (k.getKeyCode() == KeyEvent.VK_T) {
				autoTurn = !autoTurn;
			} else if (k.getKeyCode() == KeyEvent.VK_H) {
				gameOverlay
						.displayDialog(
								100,
								80,
								440,
								320,
								"Controls^"
										+ "================================^"
										+ "Left, Right : Turn the snake to the left/right direction.^"
										+ "Up, Down : Accelerate/Decelerate the snake.^"
										+ "Space : Pause the game.^^"
										+ "T : Toggle Auto-Turn field for the game. ^   Turning it off "
										+ "will prevent the field from turning along with the snake.^"
										+ "2, 3 : Swap to 2D/3D mode^"
										+ "H : Display this help.");
				tutorialMode = true;
			} else if (k.getKeyCode() == KeyEvent.VK_B) {
				snake.shrinkAll();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent k) {
		if (!fieldInited)
			return;
		if (!isAlive()){
			this.lives = 3;
			this.initField();
			gameOverlay.clearLastDialog();
		}
		if (k.getKeyCode() == KeyEvent.VK_UP
				|| k.getKeyCode() == KeyEvent.VK_KP_UP
				|| k.getKeyCode() == KeyEvent.VK_DOWN
				|| k.getKeyCode() == KeyEvent.VK_KP_DOWN) {
			if (currentTileType == 1)
				snakeSpeed = 4;
			else if (currentTileType == 2)
				snakeSpeed = 1;
			else
				snakeSpeed = 2;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	/**
	 * Updates game stats string
	 */
	public void onStatsChange() {
		gameOverlay.setGameStats("Lives: " + lives + "  Points: " + points
				+ " (x " + multiplier + ")");
	}
}
