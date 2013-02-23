package renderer.guitoolkit;

public class TkEvent {
	private String eventName;
	private Object payload;
	public TkEvent(){
		this("");
	}
	
	public TkEvent(String eventName){
		this.eventName = eventName;
	}
	
	public TkEvent(String eventName, Object payload){
		this(eventName);
		this.payload = payload;
	}
	
	public String getType(){
		return eventName;
	}
	
	public Object getPayload(){
		return payload;
	}
}
