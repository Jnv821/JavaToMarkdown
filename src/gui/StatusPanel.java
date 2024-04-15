package gui;

import javax.swing.JPanel;


public class StatusPanel extends JPanel implements JTMComponent{

	public StatusPanel() {
		
	};
	
	public void setup() {
		this.setLayout(null);
		this.setBounds((int)(this.getParent().getWidth()/2.5), 0,  this.getParent().getWidth()-this.getParent().getWidth()/3, this.getParent().getHeight() );
		this.setBackground(this.getParent().getBackground().darker());
	}
	
}
