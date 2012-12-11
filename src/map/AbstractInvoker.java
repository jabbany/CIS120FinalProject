package map;
/**
 * AbstractInvoker. An abstract invoker invokes the addition of item(s)
 * or even more invokers to the field. Invokers are only invoked when an
 * item at a position is taken off the field.
 * 
 * @author Jim
 *
 */
public interface AbstractInvoker {
	public GameMapDataCell[] invoke(int x, int y);
}
