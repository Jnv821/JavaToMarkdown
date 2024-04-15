package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements ActionListener{

	
	public MainFrame() {
		OptionsPanel optPanel = new OptionsPanel();
		//StatusPanel statusPanel = new StatusPanel();
		ImageIcon imageIcon = new ImageIcon("jtmdLogo.png");
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JavaToMarkdown");
		this.setResizable(false);
		this.setSize(305, 512); // Actual size with status panel should be 768
		this.setVisible(true);		
		this.getContentPane().setBackground(new Color(35,35,35));
		this.setIconImage(imageIcon.getImage());
		//this.add(statusPanel);
		//statusPanel.setup();
		this.add(optPanel);
		optPanel.setup();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
