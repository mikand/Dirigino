package net.mikand.dirigino.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class SerialInterface implements SerialPortEventListener {
	
	@SuppressWarnings("rawtypes")
	public static String[] getAvailablePorts() {
		List<String> result = new LinkedList<String>();
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			result.add(currPortId.getName());
		}
		
		String[] r = new String[result.size()];
		int i = 0;
		for (String s : result) {
			r[i++] = s;
		}
		
		return r;
	}
	
	
	/** The port we're normally going to use. */
	private SerialPort serialPort;
	
	/** Listeners to this connection */
	private Collection<SerialListener> listeners;
	
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;

	/** The output stream to the port */
	private OutputStream output;
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	
	public SerialInterface() {
		listeners = new HashSet<SerialListener>();
		serialPort = null;
		input = null;
		output = null;
	}

	public void connect(String portName) throws NoSuchPortException {
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
		
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				if (inputLine.length() > 0) {
					for(SerialListener s : listeners) {
						s.readPerformed(inputLine);
					}
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	
	public void write(String message) throws IOException {
		if (!message.endsWith("\n")) {
			message += "\n";
		}
		
		if (output != null) {
			output.write(message.getBytes());
		}
		else {
			throw new IOException("Connection not initialized");
		}
		
		for(SerialListener s : listeners) {
			s.writePerformed(message);
		}
	}	
	
	
	public void addListener(SerialListener s) {
		listeners.add(s);
	}
	
	public void removeListener(SerialListener s) {
		listeners.remove(s);
	}
	
}