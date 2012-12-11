package playground.snake;

import geom3d.Model3d;
import geom3d.primitives.Edge;
import geom3d.primitives.Face;
import geom3d.primitives.Point3d;
import geom3d.primitives.face.FaceQuad;

import java.awt.Color;

public class SnakeBodyModel extends Model3d implements SnakeBodyPart {
	private int x = 0, y = 0;
	private int nx = 0, ny = 0;
	private int hitCounter = 0, direction = 1, step = 0;
	private Face[] faces;
	private Color facesColor = new Color(200, 0, 0);

	public SnakeBodyModel(int x, int y, int dir) {
		this(new Edge[] { new Edge(1, 5, Color.red), new Edge(2, 6, Color.red),
				new Edge(3, 7, Color.red), new Edge(0, 2, Color.red),
				new Edge(2, 1, Color.red), new Edge(1, 3, Color.red),
				new Edge(3, 0, Color.red),
				/*
				 * new Edge(4, 6, Color.red), new Edge(7, 4, Color.red), new
				 * Edge(0, 4, Color.red),
				 */
				new Edge(5, 7, Color.red), new Edge(6, 5, Color.red), },
				new Point3d[] { new Point3d(4, 1, 3), new Point3d(4, 1, 5),
						new Point3d(2, 1, 4), new Point3d(6, 1, 4),
						new Point3d(4, 7, 3), new Point3d(4, 7, 5),
						new Point3d(2, 7, 4), new Point3d(6, 7, 4) });
		faces = new Face[] { new FaceQuad(0, 2, 1, 3, facesColor),
				new FaceQuad(4, 6, 5, 7, facesColor),
				new FaceQuad(0, 4, 6, 2, facesColor),
				new FaceQuad(1, 5, 7, 3, facesColor),
				new FaceQuad(3, 7, 4, 0, facesColor),
				new FaceQuad(2, 6, 5, 1, facesColor),
				/* These faces are for binding purposes */
				new FaceQuad(0, 3, 7, 4, facesColor),
				new FaceQuad(2, 0, 4, 6, facesColor),
				new FaceQuad(1, 2, 6, 5, facesColor),
				new FaceQuad(3, 1, 5, 7, facesColor),

		};
		this.setDirection(dir);
		this.setXY(x, y);
	}

	private SnakeBodyModel(Edge[] edges, Point3d[] points) {
		super(edges, points);
	}

	public void setChildModel(Model3d model) {
		if (getChildModel() != null
				&& !(getChildModel() instanceof SnakeTailModel)) {
			getChildModel().setChildModel(model);
		} else if (getChildModel() == null) {
			/** This is adding the tail **/
			if (model instanceof SnakeTailModel) {
				SnakeTailModel t = (SnakeTailModel) model;
				t.setXY(t.getLastGridX(), t.getLastGridY());
				t.setNext(getGridX(), getGridY());
				int mt = t.getMicrotime();
				t.setMicrotime(0);
				t.setMicrotime(mt);
				super.setChildModel(model);
			}
		} else {
			/** Add the model before the tail **/
			SnakeBodyPart m = (SnakeBodyPart) model;
			SnakeTailModel t = (SnakeTailModel) getChildModel();
			m.setXY(t.getGridX(), t.getGridY());
			m.setDirection(t.getDirection());
			m.setNext(getGridX(), getGridY());
			m.setMicrotime(t.getMicrotime());
			model.setChildModel(t); // Adds the tail
			super.setChildModel(model);
		}
	}

	@Override
	public Face[] getFaces() {
		Face[] f = faces;
		if (getChildModel() != null) {
			Face[] childFaces = getChildModel().getFaces();
			if (childFaces != null) {
				f = new Face[childFaces.length + faces.length];
				for (int i = 0; i < faces.length; i++) {
					f[i] = faces[i];
				}
				int index = 8;
				for (int i = 0; i < childFaces.length; i++) {
					f[i + faces.length] = childFaces[i].cloneTo(index);
				}
			}
		}
		return f;
	}

	private void turn() {
		switch (this.direction) {
		case 0:
			getVertex(0).apply(1, 4, 3);
			getVertex(1).apply(1, 4, 5);
			getVertex(2).apply(1, 2, 4);
			getVertex(3).apply(1, 6, 4);
			getVertex(4).apply(7, 4, 3);
			getVertex(5).apply(7, 4, 5);
			getVertex(6).apply(7, 2, 4);
			getVertex(7).apply(7, 6, 4);
			break;
		case 1:
			getVertex(0).apply(4, 1, 3);
			getVertex(1).apply(4, 1, 5);
			getVertex(2).apply(2, 1, 4);
			getVertex(3).apply(6, 1, 4);
			getVertex(4).apply(4, 7, 3);
			getVertex(5).apply(4, 7, 5);
			getVertex(6).apply(2, 7, 4);
			getVertex(7).apply(6, 7, 4);
			break;
		case 2:
			getVertex(0).apply(7, 4, 3);
			getVertex(1).apply(7, 4, 5);
			getVertex(2).apply(7, 2, 4);
			getVertex(3).apply(7, 6, 4);
			getVertex(4).apply(1, 4, 3);
			getVertex(5).apply(1, 4, 5);
			getVertex(6).apply(1, 2, 4);
			getVertex(7).apply(1, 6, 4);
			break;
		case 3:
			getVertex(0).apply(4, 7, 3);
			getVertex(1).apply(4, 7, 5);
			getVertex(2).apply(2, 7, 4);
			getVertex(3).apply(6, 7, 4);
			getVertex(4).apply(4, 1, 3);
			getVertex(5).apply(4, 1, 5);
			getVertex(6).apply(2, 1, 4);
			getVertex(7).apply(6, 1, 4);
			break;
		}
		translate(0, 0, -1);
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

	private void goForward() {
		goForward(1);
	}

	@Override
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
		if (getChildModel() != null)
			((SnakeBodyPart) getChildModel()).backtrack();
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
			if (getChildModel() != null) {
				((SnakeBodyPart) getChildModel()).setNext(x, y);
			}
		} else {
			updateDirection();
			goForward();
			step = 1;
		}
		if (getChildModel() != null) {
			getChildModel().animate();
		}
		if (hitCounter > 0) {
			if (hitCounter % 10 > 4) {
				for (int i = 0; i < getEdgesSize(); i++) {
					getEdge(i).color = Color.WHITE;
				}
			} else {
				for (int i = 0; i < getEdgesSize(); i++) {
					getEdge(i).color = Color.RED;
				}
			}
			hitCounter--;
		}
	}

	public void onHit() {
		hitCounter = 60;
		if (getChildModel() != null)
			((SnakeBodyPart) getChildModel()).onHit();
	}

	@Override
	public int getGridX() {
		return x;
	}

	@Override
	public int getGridY() {
		return y;
	}

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
	public void setNext(int x, int y) {
		this.nx = x;
		this.ny = y;
	}

	@Override
	public int getMicrotime() {
		return this.step;
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
		((FaceQuad) faces[6]).a = d;
		((FaceQuad) faces[6]).b = c;
		((FaceQuad) faces[7]).a = b;
		((FaceQuad) faces[7]).b = d;
		((FaceQuad) faces[8]).a = a;
		((FaceQuad) faces[8]).b = b;
		((FaceQuad) faces[9]).a = c;
		((FaceQuad) faces[9]).b = a;
	}

	@Override
	public int[] cornerBindings() {
		return null;
	}
}
