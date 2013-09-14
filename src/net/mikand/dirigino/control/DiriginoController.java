package net.mikand.dirigino.control;

import java.io.IOException;

public class DiriginoController {
	
	private static final int MAX_ATTEMPTS = 5;
	private SerialInterface connection;
	
	public DiriginoController(SerialInterface conn) {
		connection = conn;
	}
	
	public void connect() {
//		int attempt = 0;
//		while (attempt < MAX_ATTEMPTS) {
			try {
				connection.write("MASTER");
			}	
			catch (IOException e) {
				e.printStackTrace();
			}	
//		}
	}
	
	public void setTilt(double degrees) {
		int tlt = (int)Math.round(degrees);
		try {
			connection.write("TILT " + Integer.toString(tlt));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSpeed(double percentage) {
		int perc = (int)Math.round(percentage);
		try {
			connection.write("SPEED " + Integer.toString(perc));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
