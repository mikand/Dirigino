package net.mikand.dirigino.utils;

public class MathUtils {

	public static double rad2grad(double angle) {
		return (180 * angle) / Math.PI; 
	}
	
	public static double grad2rad(double angle) {
		return (angle * Math.PI) / 180; 
	}
	
	
}
