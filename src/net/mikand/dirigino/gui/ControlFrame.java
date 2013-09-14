package net.mikand.dirigino.gui;

import gnu.io.NoSuchPortException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.mikand.dirigino.control.DiriginoController;
import net.mikand.dirigino.control.SerialInterface;
import net.mikand.dirigino.control.SerialListener;
import net.mikand.dirigino.utils.MathUtils;

public class ControlFrame extends JFrame {

	private static final long serialVersionUID = 4935754001900434045L;

	private JComboBox comboPort;
	private JTextArea txtLog;
	private JButton btnConnect;
	private JPanel panelTop;
	private ThrustPanel thrustPanel;
	
	private SerialInterface connection;
	private DiriginoController controller;
	
	public ControlFrame() {
		setSize(640, 480);
		setTitle("Dirigino Control Panel");
		setLayout(new BorderLayout());
		
		connection = new SerialInterface();
		controller = new DiriginoController(connection);
		
		panelTop = new JPanel();
		panelTop.setLayout(new FlowLayout());
		add(panelTop, BorderLayout.NORTH);
		
		comboPort = new JComboBox(SerialInterface.getAvailablePorts());
		panelTop.add(comboPort);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String portName = comboPort.getSelectedItem().toString();
				try {
					connection.connect(portName);
				} catch (NoSuchPortException e) {
					e.printStackTrace();
				}
			}
		});
		panelTop.add(btnConnect);
		
		txtLog = new JTextArea(25, 25);
		txtLog.setEditable(false);
		connection.addListener(new SerialListener() {
			@Override
			public void writePerformed(String written) {
				txtLog.append("-> " + written);
			}
			
			@Override
			public void readPerformed(String message) {
				txtLog.append("<- " + message + " \n");
			}
		});
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
				
				angle = angle + (Math.PI / 2.0);
				if (angle > (2.0 * Math.PI)) {
					angle -= (2.0 * Math.PI);
				}
				
				double power = (100.0 * c) / (thrustPanel.getWidth() / 2);
				power = Math.min(100.0, power);
				
				thrustPanel.setThrust(MathUtils.rad2grad(angle), power);
				controller.setSpeed(power);
				controller.setTilt(MathUtils.rad2grad(angle));
			}
		});
		add(thrustPanel, BorderLayout.CENTER);
	}
	
	@Override
	protected void finalize() throws Throwable {
		connection.close();
		super.finalize();
	}
	
}
