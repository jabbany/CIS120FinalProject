package playground.snake;

import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;
import geom3d.primitives.face.FaceTri;

import java.awt.Color;

import playground.GridFieldItem;

/**
 * Model Data for the snake You can only make one instance of it in the game!
 * The vertices are shared if otherwise.
 * 
 * @author Jim
 * 
 */
public class SnakeModel extends Model3d implements GridFieldItem {
	/**
	 * Facing = 0 : Facing x axis, Facing = 1 : facing y axis, Facing = 2 : Anti
	 * x-axis Facing = 3 : Anti y-axis
	 **/
	private int facing = 1, nextFacing = 1;
	private int x = 0, y = 0;
	private int moved = 0;
	private int hitCounter = 0;

	private static final Edge[] modelEdges = new Edge[] {
	/* new Edge(0, 4, Color.RED), */
	new Edge(0, 1, Color.RED), new Edge(0, 2, Color.RED),
			new Edge(1, 3, Color.RED), new Edge(2, 3, Color.RED),
			new Edge(1, 4, Color.RED), new Edge(2, 4, Color.RED),
			new Edge(3, 4, Color.RED) };
	private static final Point3d[] modelPoints = new Point3d[] {
			new Point3d(4, 0, 1), new Point3d(7, 0, 3), new Point3d(1, 0, 3),
			new Point3d(4, 0, 5), new Point3d(4, 8, 3) };

	private static final Face[] modelFaces = new Face[] {
			new FaceQuad(0, 1, 3, 2, new Color(200, 0, 0)),
			new FaceTri(0, 4, 2, new Color(200, 0, 0)),
			new FaceTri(1, 3, 4, new Color(200, 0, 0)),
			new FaceTri(0, 1, 4, new Color(200, 0, 0)),
			new FaceTri(2, 3, 4, new Color(200, 0, 0)) };

	public SnakeModel() {
		this(0, 0);
	}

	public SnakeModel(int x, int y) {
		super(modelEdges, modelPoints);
		this.setXY(x, y);
		grow();
	}

	public Face[] getFaces() {
		if (getChildModel() == null || getChildModel().getFaces() == null)
			return modelFaces;
		Face[] childFaces = getChildModel().getFaces();
		Face[] faces = new Face[modelFaces.length + childFaces.length];
		for (int i = 0; i < modelFaces.length; i++) {
			faces[i] = modelFaces[i];
		}
		/** NOTE: assuming no new points on the model **/
		for (int i = 0; i < childFaces.length; i++) {
			faces[i + modelFaces.length] = childFaces[i]
					.cloneTo(modelPoints.length);
		}
		return faces;
	}

	/**
	 * Make the snake turn, 1 is to the right -1 is to the left
	 * 
	 * @param direction
	 */
	public void turn(int direction) {
		nextFacing = (facing + direction + 4) % 4;
	}

	/**
	 * Moves the snake back one cell and called upon hitting
	 * 
	 */
	public void backtrack() {
		// Moves the snake back
		switch (facing) {
		case 0:
			x--;
			move(-10, 0, 0);
			break;
		case 1:
			y--;
			move(0, -10, 0);
			break;
		case 2:
			x++;
			move(10, 0, 0);
			break;
		case 3:
			y++;
			move(0, 10, 0);
			break;
		}
		if (getChildModel() != null)
			((SnakeBodyPart) getChildModel()).backtrack();
	}

	/**
	 * Make the snake grow one segment
	 * 
	 */
	public void grow() {
		if (getChildModel() == null) {
			SnakeTailModel tail = new SnakeTailModel(getLastGridX(),
					getLastGridY(), facing);
			tail.setXY(getLastGridX(), getLastGridY());
			tail.setNext(getGridX(), getGridY());
			int mt = this.moved;
			tail.setMicrotime(0);
			tail.setMicrotime(mt);
			this.setChildModel(tail);
		} else if (getChildModel() instanceof SnakeTailModel) {
			SnakeTailModel t = (SnakeTailModel) getChildModel();
			SnakeBodyModel body = new SnakeBodyModel(t.getGridX(), t.getGridY(), t.getDirection());
			body.setNext(getGridX(), getGridY());
			body.setMicrotime(moved);
			body.setChildModel(t);
			this.setChildModel(body);
		} else {
			SnakeBodyModel body = new SnakeBodyModel(getLastGridX(),
					getLastGridY(), facing);
			getChildModel().setChildModel(body);
		}
	}

	/**
	 * Make the snake shrink one element
	 */
	public void shrink() {
		if (getChildModel() != null) {
			if (getChildModel() instanceof SnakeTailModel)
				return; // Already tail!
			setChildModel(getChildModel().getChildModel());
		}
	}

	/**
	 * Delete all attached length from the snake
	 */
	public void shrinkAll() {
		setChildModel(null);
		grow();
	}

	/**
	 * Gets the facing direction's rotation on z-axis
	 * 
	 * @return degrees
	 */
	public int facingRotZ() {
		switch (facing) {
		case 0:
			return 270;
		case 1:
			return 180;
		case 2:
			return 90;
		case 3:
			return 0;
		default:
			return 0;
		}
	}

	public int getLastGridY() {
		switch (facing) {
		case 1:
			return y - 1;
		case 3:
			return y + 1;
		default:
			return y;
		}
	}

	public int getLastGridX() {
		switch (facing) {
		case 0:
			return x - 1;
		case 2:
			return x + 1;
		default:
			return x;
		}
	}

	public int getGridX() {
		return x;
	}

	public int getGridY() {
		return y;
	}

	public int faceDirection() {
		return facing;
	}

	public void animate(int speed) {
		for (int x = 0; x < speed; x++) {
			animate();
		}
	}
	
	public void onHit() {
		hitCounter = 60;
		if (getChildModel() != null)
			((SnakeBodyPart) getChildModel()).onHit();
	}

	/**
	 * Checks to see if the current gridXY is part of the snake's tail
	 * 
	 * @param x
	 *            grid coordinate
	 * @param y
	 *            grid coordinate
	 * @return true if is part of tail
	 */
	public boolean isTail(int x, int y) {
		SnakeBodyPart bodyPart = (SnakeBodyPart) getChildModel();
		while (bodyPart != null) {
			if (bodyPart.getGridX() == x && bodyPart.getGridY() == y) {
				return true;
			}
			bodyPart = (SnakeBodyPart) ((Model3d) bodyPart).getChildModel();
		}
		return false;
	}

	@Override
	public void animate() {
		switch (facing) {
		case 0:
			move(1, 0, 0);
			moved += 1;
			break;
		case 1:
			move(0, 1, 0);
			moved += 1;
			break;
		case 2:
			move(-1, 0, 0);
			moved += 1;
			break;
		case 3:
			move(0, -1, 0);
			moved += 1;
			break;
		}
		if (moved == 5) {
			switch (facing) {
			case 0:
				x++;
				break;
			case 1:
				y++;
				break;
			case 2:
				x--;
				break;
			case 3:
				y--;
				break;
			}
			if (getChildModel() != null) {
				((SnakeBodyPart) getChildModel()).setNext(x, y);
			}
		} else if (moved == 10) {
			moved = 0;
			facing = nextFacing;
			switch (facing) {
			case 0: {
				modelPoints[0].apply(0, 4, 1);
				modelPoints[1].apply(0, 1, 3);
				modelPoints[2].apply(0, 7, 3);
				modelPoints[3].apply(0, 4, 5);
				modelPoints[4].apply(8, 4, 4);
			}
				break;
			case 1: {
				modelPoints[0].apply(4, 0, 1);
				modelPoints[1].apply(7, 0, 3);
				modelPoints[2].apply(1, 0, 3);
				modelPoints[3].apply(4, 0, 5);
				modelPoints[4].apply(4, 8, 3);
			}
				break;
			case 2: {
				modelPoints[0].apply(8, 4, 1);
				modelPoints[1].apply(8, 1, 3);
				modelPoints[2].apply(8, 7, 3);
				modelPoints[3].apply(8, 4, 5);
				modelPoints[4].apply(0, 4, 4);
			}
				break;
			case 3: {
				modelPoints[0].apply(4, 8, 1);
				modelPoints[1].apply(7, 8, 3);
				modelPoints[2].apply(1, 8, 3);
				modelPoints[3].apply(4, 8, 5);
				modelPoints[4].apply(4, 0, 3);
			}
				break;
			}
		}
		if (getChildModel() != null) {
			getChildModel().animate();
		}
		/** Process hit effects **/
		if (hitCounter > 0) {
			if (hitCounter % 10 > 4) {
				for (int i = 0; i < modelEdges.length; i++) {
					modelEdges[i].color = Color.WHITE;
				}
			} else {
				for (int i = 0; i < modelEdges.length; i++) {
					modelEdges[i].color = Color.RED;
				}
			}
			hitCounter--;
		}
	}

	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		this.moveTo(10 * x, 10 * y, 0);
	}
}
