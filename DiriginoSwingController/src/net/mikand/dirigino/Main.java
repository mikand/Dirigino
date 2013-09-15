package net.mikand.dirigino;

import javax.swing.JFrame;

import net.mikand.dirigino.gui.ControlFrame;

public class Main {

	public static void main(String[] args) {
		ControlFrame cf = new ControlFrame();
		cf.setVisible(true);
		
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
