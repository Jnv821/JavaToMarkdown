package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class JTMTextArea extends JTextArea implements JTMComponent{



	
	public JTMTextArea(){
		super();
	}
	
	public JTMTextArea(int posX,int posY, int width,int height) {
		super();
		this.setBounds(posX, posY, width, height);
	};
	
	public void setup() {
		this.setBounds(this.getParent().getX(), this.getParent().getY(), this.getParent().getWidth(), this.getParent().getHeight());
		this.setForeground(Color.WHITE);
		this.setForeground(new Color(35,35,35).brighter().brighter().brighter().brighter());
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getBackground()), BorderFactory.createEmptyBorder(1,1,1,1)));
		this.setEditable(false);
		this.setFocusable(false);
		this.setCursor(null);
		this.setVisible(true);
		
	};
	
	public void setup(int posX, int posY, int width, int heigth) {
		this.setBounds(posX, posY, width, heigth);
		this.setForeground(new Color(35,35,35).brighter().brighter().brighter().brighter());
		this.setBackground(new Color(35,35,35));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getBackground()), BorderFactory.createEmptyBorder(1,1,1,1)));
		this.setEditable(false);
		this.setFocusable(false);
		this.setCursor(null);
		this.setVisible(true);
	}
	
	
}
