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
		
		int shiftx = 0;
		int shifty = 0;
		
		if (w > h) {
			shiftx = (w - h) / 2;
			w = h;
		}
		else {
			shifty = (h - w) / 2;
			h = w;
		}
		
		g.setColor(Color.GREEN);
		g.fillOval(shiftx, shifty, w, h);
		
		g.setColor(Color.BLACK);
		g.fillOval(shiftx + w/2 - POINT_RADIUS, shifty + h/2 - POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
		
		g.drawLine(shiftx, shifty + h/2, shiftx + w, shifty + h/2);
		g.drawLine(shiftx + w/2, shifty, shiftx + w/2, shifty + h);
		
		if (power > 0) {
			double s = Math.sin(MathUtils.grad2rad(angle));
			double c = Math.cos(MathUtils.grad2rad(angle));
			
			double scalex = ((w / 2) * c) * (power / 100);
			double scaley = ((h / 2) * s) * (power / 100);
			
			int x = ((int)Math.round(scalex)) + (w/2);
			int y = ((int)Math.round(-1* scaley)) + (h/2);
			
			g.fillOval(shiftx + x - POINT_RADIUS, shifty + y - POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
			
			g.setColor(Color.BLUE);
			g.drawLine(getCenter().x, getCenter().y, shiftx + x, shifty + y);
		}
	}
	
	public double getAngleFromPoint(Point p) {
		Point center = getCenter();

		double a = (double) (p.x - center.x);
		double b = (double) (-1.0 * (p.y - center.y));
		double c = center.distance(p);

		double b1 = b / c;
		double a1 = a / c;

		double angle = Math.acos(a1);
		if (b1 < 0) {
			angle = (2 * Math.PI) - angle;
		}

		angle = angle + (Math.PI / 2.0);
		if (angle > (2.0 * Math.PI)) {
			angle -= (2.0 * Math.PI);
		}

		return  MathUtils.rad2grad(angle);
	}
	
	
	public double getPowerFromPoint(Point p) {
		Point center = getCenter();

		double radius = Math.min(getWidth(), getHeight()) / 2.0;
		double c = center.distance(p);

		double power = (100.0 * c) / radius;
		power = Math.min(100.0, power);
		
		return power;
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
		this.angle = angle - 90;
		this.power = power;
		this.repaint();
	}	
	
	
}
