package net.mikand.dirigino.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.mikand.dirigino.utils.MathUtils;

public class ControlFrame extends JFrame {

	private static final long serialVersionUID = 4935754001900434045L;

	private JComboBox comboPort;
	private JTextField txtLog;
	private JButton btnConnect;
	private JPanel panelTop;
	private ThrustPanel thrustPanel;
	
	public ControlFrame() {
		setSize(640, 480);
		setTitle("Dirigino Control Panel");
		setLayout(new BorderLayout());
		
		panelTop = new JPanel();
		panelTop.setLayout(new FlowLayout());
		add(panelTop, BorderLayout.NORTH);
		
		comboPort = new JComboBox(new String[]{"A", "B", "C"});
		panelTop.add(comboPort);
		
		btnConnect = new JButton("Connect");
		panelTop.add(btnConnect);
		
		txtLog = new JTextField(20);
		txtLog.setEditable(false);
		add(txtLog, BorderLayout.EAST);
		
		thrustPanel = new ThrustPanel();
		thrustPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				Point center = thrustPanel.getCenter();
				
				double a = (double)(p.x - center.x);
				double b = (double)(-1.0 * (p.y - center.y));
				double c = center.distance(p);
				
				double b1 = b/c;
				double a1 = a/c;
								
				double angle = Math.acos(a1);
				if (b1 < 0) {
					angle = (2 * Math.PI) - angle;
				}
				
				double power = (100.0 * c) / (thrustPanel.getWidth() / 2);
				power = Math.min(100.0, power);
				
				thrustPanel.setThrust(MathUtils.rad2grad(angle), power);
			}
		});
		add(thrustPanel, BorderLayout.CENTER);
	}
	
}
