package playground;

public class TutorialInvoker {
	private String tutorialText = "";
	private int[][] positions;
	public TutorialInvoker(int[][] pos){
		positions = pos;
	}
	
	public TutorialInvoker(int xs, int xe, int ys, int ye){
		positions = new int[Math.abs(xe-xs) * Math.abs(ye-ys)][2];
		int counter = 0;
		for(int i = xs; i < xe; i++){
			for(int j = ys; j < ye; j++){
				positions[counter][0] = i;
				positions[counter][1] = j;
				counter++;
			}
		}
	}
	
	public void setText(String t){
		tutorialText = t;
	}
	
	public String getText(){
		return tutorialText;
	}
	
	public void setLocations(int[][] loc){
		positions = loc;
	}
	
	public int[][] getLocations(){
		return positions;
	}
}
