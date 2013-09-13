package net.mikand.dirigino.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import net.mikand.dirigino.utils.MathUtils;

public class ThrustPanel extends JPanel {

	private static final long serialVersionUID = 7116866631102278117L;
	private final int POINT_RADIUS = 3;

	private double angle;
	private double power;
	
	public ThrustPanel() {
		angle = 0;
		power = 0;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int w = getWidth();
		int h = getHeight();
		
		g.setColor(Color.GREEN);
		g.fillOval(0, 0, w, h);
		
		g.setColor(Color.BLACK);
		g.fillOval(w/2 - POINT_RADIUS, h/2 - POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
		
		g.drawLine(0, h/2, w, h/2);
		g.drawLine(w/2, 0, w/2, h);
		
		if (power > 0) {
			double s = Math.sin(MathUtils.grad2rad(angle));
			double c = Math.cos(MathUtils.grad2rad(angle));
			
			double scalex = ((w / 2) * c) * (power / 100);
			double scaley = ((h / 2) * s) * (power / 100);
			
			int x = ((int)Math.round(scalex)) + (w/2);
			int y = ((int)Math.round(-1* scaley)) + (h/2);
			
			g.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
		}
	}


	public Point getCenter() {
		return new Point(getWidth()/2, getHeight()/2);
	}

	public double getAngle() {
		return angle;
	}
	
	public double getPower() {
		return power;
	}

	public void setThrust(double angle, double power) {
		this.angle = angle;
		this.power = power;
		this.repaint();
	}	
	
	
}
