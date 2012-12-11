package renderer;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.peer.MouseInfoPeer;

public class Field3dMouseHandler implements MouseListener, MouseMotionListener,
		MouseInfoPeer {
	private int mx, my;
	private MouseDragCatcher field;
	private MouseClickCatcher gui;
	
	public Field3dMouseHandler(MouseDragCatcher c){
		field = c;
	}
	
	@Override
	public int fillPointWithCoords(Point arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isWindowUnderMouse(Window arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(gui == null || !gui.standalone()){
			int new_mx = e.getX();
			int new_my = e.getY();
			field.onDrag(new_mx - mx, new_my - my);
			mx = new_mx;
			my = new_my;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getX() == mx && e.getY() == my){
			if(gui != null && gui.standalone())
				gui.onClick(mx, my);
		}
	}
}
