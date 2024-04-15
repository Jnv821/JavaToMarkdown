package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


import javax.swing.BorderFactory;
import javax.swing.JButton;

public class JTMButton extends JButton implements JTMComponent {

	private int [] radius = {5,5};
	private int [] position = {0,0}; 
	private int [] dimension = {200,50};
	private String name;
	
	public JTMButton(String name, int radiusX, int radiusY) {
		super(name);
		this.name = name;
		this.radius[0] = radiusX;
		this.radius[1] = radiusY; 
	}
	
	public JTMButton(String name, int radius) {
		super(name);
		this.name = name;
		this.radius[0] = radius;
		this.radius[1] = radius;
	}
	
	public JTMButton(String name, int width, int height, int radius) {
		super(name);
		this.name = name;
		this.dimension[0] = width;
		this.dimension[1] = height;
		this.radius[0] = radius;
		this.radius[1] = radius;
	}
	
	public JTMButton(String name, int positionX, int positionY, int width, int height, int radius) {
		super(name);
		this.name = name;
		// Dimension
		this.dimension[0] = width;
		this.dimension[1] = height;
		// Position
		this.position[0] = positionX;
		this.position[1] = positionY;
		// Radius
		this.radius[0] = radius;
		this.radius[1] = radius;
	
	}
	
	
	public int[] getRadius() {
		return radius;
	}

	public void setRadius(int[] radius) {
		this.radius = radius;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int[] getDimension() {
		return dimension;
	}

	public void setDimension(int[] dimension) {
		this.dimension = dimension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setup() {
		this.validate();
		this.setBounds(this.position[0], this.position[1], this.dimension[0], this.dimension[1]);
		this.setBackground(this.getParent().getBackground().darker());
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, this.getBackground().brighter()));
		this.setFocusPainted(false);
		this.setVisible(true);	
		this.setContentAreaFilled(false);
	}
	
	@Override
	public void paintComponent(Graphics graphics){
		Graphics2D g2 = (Graphics2D) graphics;
		// Base
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	
		
		if(getModel().isPressed()) {
			g2.setColor(this.getParent().getBackground().brighter());
		} else {			
			g2.setColor(this.getParent().getBackground().darker());
		}
		
		
		g2.fillRoundRect(0, 0, this.dimension[0], this.dimension[1] , this.radius[0], this.radius[1]);
		// Text
		FontMetrics metrics = g2.getFontMetrics();
		if(!getModel().isEnabled()) {
			g2.setColor(this.getParent().getBackground());
		} else {			
			g2.setColor(Color.white);
		}
		g2.drawString(this.name, (this.dimension[0]/2 - metrics.stringWidth(this.name)/2), ((this.dimension[1]/2 - metrics.getHeight()/2) + metrics.getAscent()) );
	
	}
	
}
