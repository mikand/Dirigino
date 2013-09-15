package net.mikand.dirigino.gui;

import gnu.io.NoSuchPortException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import net.mikand.dirigino.control.DiriginoController;
import net.mikand.dirigino.control.SerialInterface;
import net.mikand.dirigino.control.SerialListener;

public class ControlFrame extends JFrame {

	private static final long serialVersionUID = 4935754001900434045L;

	private JComboBox comboPort;
	private JTextArea txtLog;
	private JButton btnConnect;
	private JPanel panelTop;
	private ThrustPanel thrustPanel;
	private JPanel panelCenter;
	private JPanel logPanel;
	private JScrollPane scrollLog;
	private JPanel controlPanel;
	private JButton btnReset;
	
	private SerialInterface connection;
	private DiriginoController controller;

	private boolean connected;

	private JLabel lblStatus;

	private JButton btnStatus;

	private JButton btnStop;

	public ControlFrame() {
		connected = false;

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
				if (!connected) {
					String portName = comboPort.getSelectedItem().toString();
					try {
						connection.connect(portName);
					} catch (NoSuchPortException e) {
						e.printStackTrace();
					}

					setContainerEnabled(panelCenter, true);
					setContainerEnabled(controlPanel, true);
					connected = true;
					btnConnect.setText("Disconnect");
					lblStatus.setText("Connected");
				} else {
					connection.close();
					setContainerEnabled(panelCenter, false);
					setContainerEnabled(controlPanel, false);
					connected = false;
					btnConnect.setText("Connect");
					lblStatus.setText("Not Connected");
				}
			}
		});
		panelTop.add(btnConnect);

		logPanel = new JPanel();
		logPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		logPanel.add(new JLabel("Log:"), c);
		
		txtLog = new JTextArea(25, 15);
		txtLog.setLineWrap(true);
		txtLog.setWrapStyleWord(true);
		txtLog.setEditable(false);
		DefaultCaret caret = (DefaultCaret)txtLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.5;
		scrollLog = new JScrollPane(txtLog);
		scrollLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logPanel.add(scrollLog, c);
		
		add(logPanel, BorderLayout.EAST);
		
		lblStatus = new JLabel("Not Connected");
		add(lblStatus, BorderLayout.SOUTH);

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());

		thrustPanel = new ThrustPanel();
		thrustPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (thrustPanel.isEnabled()) {
					Point p = e.getPoint();
					
					double angle = thrustPanel.getAngleFromPoint(p);
					double power = thrustPanel.getPowerFromPoint(p);
					
					thrustPanel.setThrust(angle, power);
					
					controller.setSpeed(power);
					controller.setTilt(angle);
				}
			}
		});
		panelCenter.add(thrustPanel, BorderLayout.CENTER);
		setContainerEnabled(panelCenter, false);

		add(panelCenter, BorderLayout.CENTER);
		
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.reset();
				thrustPanel.setThrust(90, 0);
			}
		});
		
		GridBagConstraints cp = new GridBagConstraints();
		cp.gridx = 0;
		cp.gridy = 0;
		controlPanel.add(btnReset, cp);
		
		btnStatus = new JButton("Status");
		btnStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.queryStatus();
			}
		});
		
		cp.gridx = 0;
		cp.gridy = 1;
		controlPanel.add(btnStatus, cp);
		
		
		btnStop = new JButton("Stop Power");
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setSpeed(0);
				thrustPanel.setThrust(thrustPanel.getAngle(), 0);
			}
		});
		
		cp.gridx = 0;
		cp.gridy = 2;
		controlPanel.add(btnStop, cp);

		setContainerEnabled(controlPanel, false);
		
		add(controlPanel, BorderLayout.WEST);
	}

	private void setContainerEnabled(Container panel, boolean enabled) {
		for (Component c : panel.getComponents()) {
			c.setEnabled(enabled);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (connected) {
			connection.close();
		}

		super.finalize();
	}

}
