package renderer.guitoolkit;

public interface TkEventDispatcher {
	public void dispatchEvent(TkEvent event);
	public void addEventListener(String eventListener, TkEventListener listener);
}
