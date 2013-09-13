package net.mikand.dirigino.control;

public interface SerialListener {
	
	public void writePerformed(String written);

	public void readPerformed(String message);
	
}
