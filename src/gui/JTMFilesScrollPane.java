package gui;



import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class JTMFilesScrollPane extends JScrollPane implements JTMComponent {

	private JTMTextArea filesTextArea;
	
	
	
	public JTMTextArea getFilesTextArea() {
		return filesTextArea;
	}

	public void setFilesTextArea(JTMTextArea filesTextArea) {
		this.filesTextArea = filesTextArea;
	}

	public JTMFilesScrollPane(Component component) {
		super(component);
		this.filesTextArea = (JTMTextArea) component;
	}
	
	public JTMFilesScrollPane(Component component, int posX, int posY, int width, int heigth ) {
		super(component);
		this.filesTextArea = (JTMTextArea) component;
		this.setBounds(posX, posY, width, heigth);
	}
	
	public void setup(int posX, int posY, int width, int height) {
		this.setBounds(posX, posY, width, height);
		this.filesTextArea.setup(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		this.setBackground(this.getParent().getBackground().darker());
		this.setForeground(this.getParent().getBackground().brighter());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setVisible(true);
	}
	
	public void setup() {
		this.filesTextArea.setup(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		this.setBackground(this.getParent().getBackground().darker());
		this.setForeground(this.getParent().getBackground().brighter());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setVisible(true);
	}
	
   public void fillTextArea(ArrayList<File> files) {
	   this.filesTextArea.setText("");
	   
	   for(int i = 0; i < files.size(); i++) {
		   this.filesTextArea.setText(this.filesTextArea.getText() + files.get(i).getPath() +"\n");
	   }
   }
   
   public void fillTextArea(File file) {
	   this.filesTextArea.setText(file.getAbsolutePath());
   }
}
