package playground;

import java.awt.Color;

public class PowerStrip {
	private PowerStripModel[] elements;
	private int[][] locations;

	public PowerStrip(int[][] locations) {
		this.locations = locations;
		elements = new PowerStripModel[locations.length];
		for (int i = 0; i < locations.length; i++) {
			elements[i] = new PowerStripModel(locations[i][0], locations[i][1],
					new Color(128, 128, 255));
		}
	}

	public int indexPowerStrip(int x, int y) {
		for (int n = 0; n < locations.length; n++) {
			if (locations[n][0] == x && locations[n][1] == y)
				return n;
		}
		return -1;
	}

	public PowerStripModel getModel(int x, int y) {
		int pos = indexPowerStrip(x, y);
		if (pos == -1)
			return null;
		return elements[pos];
	}
	
	public int[][] getPositions(){
		return locations;
	}
	
	public PowerStripModel[] getElements() {
		return elements;
	}

	public void removeElement(int x, int y) {
		int pos = indexPowerStrip(x, y);
		if (pos != -1) {
			elements[pos] = null;
		}
	}
	
	public int remainingElements() {
		int x = 0;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null)
				x++;
		}
		return x;
	}
}
