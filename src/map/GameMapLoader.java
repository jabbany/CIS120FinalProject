package map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import playground.PelletModel;
import playground.PowerStrip;
import playground.TutorialInvoker;

public class GameMapLoader {
	private boolean isReady = false;
	private ArrayList<GameMapData> gmd = new ArrayList<GameMapData>();
	private GameMapData curLevel;

	public GameMapLoader(String filename) {
		BufferedReader f;
		try {
			f = new BufferedReader(new FileReader(filename));	
		} catch (FileNotFoundException e) {
			System.out.println("[Error] FileNotFound on " + filename);
			isReady = false;
			return;
		}
		String line = "<NOTHING_READ>";
		try {
			line = f.readLine();
			while (line != null) {
				if (line.startsWith("#") || line.trim().length() == 0){
					line = f.readLine();
					continue;
				}
				String[] data = line.split("\\|");
				for (int i = 0; i < data.length; i++) {
					/** Each line is a command **/
					String[] fn = data[i].split(":");
					if (fn.length < 1)
						continue;
					if (isNumber(fn[0])) {
						curLevel = new GameMapData(0, fn[1]);
						gmd.add(Integer.parseInt(fn[0]), curLevel);
					} else {
						String[] params = fn[1].split(",");
						switch (fn[0]) {
						case "TYPE":
							curLevel.setType(Integer.parseInt(params[0]));
							break;
						case "SIZE":
							curLevel.setDimensions(toInt(params[0]),
									toInt(params[1]));
							break;
						case "START":
							curLevel.setStartingPoint(toInt(params[0]),
									toInt(params[1]));
							break;
						case "B":
							curLevel.setTile(toInt(params[0]),
									toInt(params[1]), -1);
							break;
						case "H":
							curLevel.setTile(toInt(params[0]),
									toInt(params[1]), -2);
							break;
						case "SUP":
							curLevel.setTile(toInt(params[0]),
									toInt(params[1]), 1);
							break;
						case "SDN":
							curLevel.setTile(toInt(params[0]),
									toInt(params[1]), 2);
							break;
						case "PEL": {
							int respawn = toInt(params[0]);
							int points = toInt(params[1]);
							int[][] p = new int[points][2];
							for (int k = 0; k < points; k++) {
								p[k][0] = toInt(params[k * 2 + 2]);
								p[k][1] = toInt(params[k * 2 + 3]);
							}
							if (points > 0) {
								curLevel.addCell(p[0][0], p[0][1],
										GameMapDataCell.TYPE_PELLET,
										new PelletModel(p, respawn));
							}
						}
							break;
						case "TUT": {
							int xs = toInt(params[0]);
							int xe = toInt(params[1]);
							int ys = toInt(params[2]);
							int ye = toInt(params[3]);
							TutorialInvoker t = new TutorialInvoker(xs,xe,ys,ye);
							t.setText(params[4]);
							for(int j = xs; j < xe; j++){
								for(int k = ys; k < ye; k++){
									curLevel.addCell(j, k, GameMapDataCell.TYPE_TUTORIAL, t);
								}
							}
						}
							break;
						case "STP": {
							int points = toInt(params[0]);
							int[][] p = new int[points][2];
							for (int k = 0; k < points; k++) {
								p[k][0] = toInt(params[k * 2 + 1]);
								p[k][1] = toInt(params[k * 2 + 2]);
							}
							if (points > 0) {
								PowerStrip ps = new PowerStrip(p);
								for (int l = 0; l < points; l++) {
									curLevel.addCell(p[l][0], p[l][1],
											GameMapDataCell.TYPE_POWERUP, ps);
									curLevel.setTile(p[l][0], p[l][1], 3);
								}
							}
						}	break;
						case "INVOKE": {
							
						}
							break;
						}
					}
				}
				line = f.readLine();
			}
			f.close();
			isReady = true;
		} catch (IOException e) {
			isReady = false;
		} catch (Exception e) {
			System.out.println("Ill formed file!");
			System.out.println(line);
			isReady = false;
		} 
	}

	private boolean isNumber(String s) {
		return s.matches("^\\d+$");
	}

	private int toInt(String s) {
		return Integer.parseInt(s);
	}

	public GameMapData getLevel(int i) {
		if(!isReady)
			return null;
		if (i < gmd.size())
			return gmd.get(i).clone();
		return null;
	}

	public boolean isNormalCell() {
		return true;
	}
}
