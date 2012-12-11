package playground.snake;

import java.awt.Color;

import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceTri;
import playground.GridFieldItem;

public class SnakeTailModel extends Model3d implements GridFieldItem,
		SnakeBodyPart {
	private int x = 0, y = 0;
	private int nx = 0, ny = 0;
	private int direction = 1, step = 0, hitCounter = 0;

	private static Color edgeColor = Color.red;
	private static Color faceColor = new Color(200, 0, 0);
	private static Edge[] edges = new Edge[] { new Edge(0, 1, edgeColor),
			new Edge(0, 2, edgeColor), new Edge(0, 3, edgeColor),
			new Edge(0, 4, edgeColor), new Edge(1, 3, edgeColor),
			new Edge(3, 2, edgeColor), new Edge(2, 4, edgeColor),
			new Edge(4, 1, edgeColor) };
	private static Point3d[] points = new Point3d[] { new Point3d(4, 0, 4),
			new Point3d(4, 7, 3), new Point3d(4, 7, 5), new Point3d(2, 7, 4),
			new Point3d(6, 7, 4) };
	private Face[] faces = new Face[] { new FaceTri(0, 1, 3, faceColor),
			new FaceTri(0, 1, 4, faceColor), new FaceTri(0, 2, 4, faceColor),
			new FaceTri(0, 2, 3, faceColor), };

	public SnakeTailModel(int x, int y, int dir) {
		this();
		this.setDirection(dir);
		this.setXY(x, y);
	}

	public SnakeTailModel() {
		super(edges, points);
	}

	@Override
	public Face[] getFaces() {
		return faces;
	}

	public void backtrack() {
		switch (direction) {
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
	}

	private void turn() {
		switch (this.direction) {
		case 0: {
			points[0].apply(0, 4, 4);
			points[1].apply(7, 4, 3);
			points[2].apply(7, 4, 5);
			points[3].apply(7, 2, 4);
			points[4].apply(7, 6, 4);
		}
			break;
		case 1: {
			points[0].apply(4, 0, 4);
			points[1].apply(4, 7, 3);
			points[2].apply(4, 7, 5);
			points[3].apply(2, 7, 4);
			points[4].apply(6, 7, 4);
		}
			break;
		case 2: {
			points[0].apply(8, 4, 4);
			points[1].apply(1, 4, 3);
			points[2].apply(1, 4, 5);
			points[3].apply(1, 2, 4);
			points[4].apply(1, 6, 4);
		}
			break;
		case 3: {
			points[0].apply(4, 8, 4);
			points[1].apply(4, 1, 3);
			points[2].apply(4, 1, 5);
			points[3].apply(2, 1, 4);
			points[4].apply(6, 1, 4);
		}
			break;
		}
		translate(0, 0, -1);
	}

	private void updateDirection() {
		if (x == nx) {
			if (ny > y) {
				direction = 1;
				turn();
			} else if (ny < y) {
				direction = 3;
				turn();
			} else {
				// No change.
			}
		} else if (y == ny) {
			if (nx > x) {
				direction = 0;
				turn();
			} else if (nx < x) {
				direction = 2;
				turn();
			}
		}
	}

	private void goForward() {
		goForward(1);
	}

	private void goForward(int speed) {
		switch (direction) {
		case 0:
			move(speed, 0, 0);
			break;
		case 1:
			move(0, speed, 0);
			break;
		case 2:
			move(-speed, 0, 0);
			break;
		case 3:
			move(0, -speed, 0);
			break;
		}
	}

	@Override
	public void animate() {
		if (step < 10 && step != 5) {
			goForward();
			step++;
		} else if (step == 5) {
			switch (direction) {
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
			goForward();
			step++;
		} else {
			updateDirection();
			goForward();
			step = 1;
		}
		if (hitCounter > 0) {
			if (hitCounter % 10 > 4) {
				for (int i = 0; i < edges.length; i++) {
					edges[i].color = Color.WHITE;
				}
			} else {
				for (int i = 0; i < edges.length; i++) {
					edges[i].color = Color.RED;
				}
			}
			hitCounter--;
		}
	}

	public int getDirection() {
		return direction;
	}

	/**
	 * Micro timing finds out how many steps this block has deviated from a
	 * default position.
	 * 
	 * @return
	 */
	public int getMicrotime() {
		return step;
	}

	@Override
	public void setMicrotime(int mt) {
		if (step < mt) {
			if(mt > 5)
				goForward(-10);
			for (int a = step; a < mt; a++) {
				goForward();
			}
		}
		this.step = mt;
	}

	@Override
	public void onHit() {
		hitCounter = 60;
	}

	@Override
	public int getGridX() {
		return x;
	}

	@Override
	public int getGridY() {
		return y;
	}

	@Override
	public void setNext(int x, int y) {
		nx = x;
		ny = y;
	}

	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		this.moveTo(10 * x, 10 * y, 0);
	}

	@Override
	public void setDirection(int direction) {
		this.direction = direction;
		turn();
	}

	@Override
	public void bindCorners(int a, int b, int c, int d) {

	}

	@Override
	public int[] cornerBindings() {
		return null;
	}

	@Override
	public int getLastGridX() {
		switch (direction) {
		case 0:
			return x - 1;
		case 2:
			return x + 1;
		default:
			return x;
		}
	}

	@Override
	public int getLastGridY() {
		switch (direction) {
		case 1:
			return y - 1;
		case 3:
			return y + 1;
		default:
			return y;
		}
	}
}
