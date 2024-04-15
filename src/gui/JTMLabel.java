package gui;

import java.awt.Color;

import javax.swing.JLabel;

public class JTMLabel extends JLabel implements JTMComponent{

	public JTMLabel(String name) {
		super(name);
	}
	
	public JTMLabel(String name, int posX, int posY, int width, int height) {
		super(name);
		this.setBounds(posX,posY,width, height);
	}
	
	public void setup() {
		this.setForeground(Color.WHITE);
		this.setVisible(true);
	}
}
