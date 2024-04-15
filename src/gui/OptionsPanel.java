package gui;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import processor.Parser;
import processor.Writer;


public class OptionsPanel extends JPanel implements ActionListener, JTMComponent {
	private JTMButton InputFilesButton; 
	private JTMButton OutputDirectoryButton;
	private JTMButton ConvertButton;
	private JTMButton flushFilesButton;
	private JTMLabel OutputLabel;
	private JTMLabel FilesLabel;
	private JTMFilesScrollPane outputScrollPane;
	private JTMFilesScrollPane fileScrollPane;
	private ArrayList<File> files;
	private Parser mainParser;
	private Writer mainWriter;
	
	private File OutputDirectory;

	public OptionsPanel() {
		this.InputFilesButton =  new JTMButton("Choose Files", 10, 10 ,130, 35, 10);
		this.OutputDirectoryButton = new JTMButton("Choose Output Dir", 150, 10, 130, 35, 10);
		this.ConvertButton = new JTMButton("Convert to MD", 10, 428 ,128,35,10);
		this.flushFilesButton = new JTMButton("Reset Files", 150, 428 ,128,35,10);
		this.OutputLabel = new JTMLabel("Output Directory",10,340,280,35);
		this.FilesLabel =  new JTMLabel("Files: 0", 10,40,280,30);
		this.fileScrollPane = new JTMFilesScrollPane(new JTMTextArea(),10, 70, 270, 270);
		this.outputScrollPane = new JTMFilesScrollPane(new JTMTextArea(), 10, 370, 270, 50);
		this.files = new ArrayList<File>();
		this.mainParser = new Parser();
		this.mainWriter = new Writer(mainParser, OutputDirectory);
	}
	
	public void setup() {
		this.setLayout(null);
		this.setBounds(0, 0, this.getParent().getWidth(), this.getParent().getHeight());
		this.setBackground(this.getParent().getBackground().brighter());
		this.addJTMComponents(OutputLabel, FilesLabel, InputFilesButton, OutputDirectoryButton, ConvertButton, flushFilesButton, fileScrollPane, outputScrollPane);
		this.setupButtons(InputFilesButton, OutputDirectoryButton, ConvertButton, flushFilesButton);
		this.setupLabels(FilesLabel, OutputLabel);
		this.setupScrollPanes(fileScrollPane, outputScrollPane);
		this.ConvertButton.setEnabled(false);
		this.flushFilesButton.setEnabled(false);
	}
	
	
	
	
	public File getOutputDirectory() {
		return OutputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		OutputDirectory = outputDirectory;
	}

	private void addJTMComponents(JTMComponent ... components) {
		for (int i = 0; i < components.length; i++) {
			this.add((Component)components[i]);
		}
	}
	
	private void setupScrollPanes(JTMFilesScrollPane ... filesScrollPanes) {
		for (int i = 0; i < filesScrollPanes.length; i++) {
			filesScrollPanes[i].setup();
		}
	};
	
	private void setupLabels(JTMLabel ... jtmLabels) {
		for (int i = 0; i < jtmLabels.length; i++) {
			jtmLabels[i].setup();
		}
	}
	private void setupButtons(JTMButton ... buttons) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setup();
			buttons[i].addActionListener(this);
		}
	}

	private void enableConvertButton(JTMButton convertButton) {
		if(this.OutputDirectory != null && this.files.size() > 0) {
			convertButton.setEnabled(true);
		}
	}
	
	private void enableResetButton(JTMButton resetButton) {
		if(this.files.size() == 0) {
			resetButton.setEnabled(false);
		} else {
			resetButton.setEnabled(true);
		}
	}
	
	private void chooseFiles(boolean multiSelection, int mode) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(multiSelection);
		fileChooser.setFileSelectionMode(mode);
		
		int response = fileChooser.showOpenDialog(null);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			populateFiles(fileChooser.getSelectedFiles());
		}
		
		this.enableConvertButton(this.ConvertButton);
		this.enableResetButton(flushFilesButton);
		this.FilesLabel.setText("Files: " + files.size());
	}
	
	private void chooseOutputDirectory(boolean multiSelection, int mode) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(multiSelection);
		fileChooser.setFileSelectionMode(mode);
		
		int response = fileChooser.showOpenDialog(null);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			populateOutputDirectory(fileChooser.getSelectedFile());
			this.setOutputDirectory(fileChooser.getSelectedFile());
		}
		this.enableConvertButton(this.ConvertButton);
		this.enableResetButton(flushFilesButton);
	}
	
	private void populateFiles(File[] selectedFiles) {
		for (int i = 0; i < selectedFiles.length; i++) {
			if(files.contains(selectedFiles[i])) {
				System.out.println("Skipping this file");
			} else {
				files.add(selectedFiles[i]);
				this.fileScrollPane.fillTextArea(files);
			}
		}
	}
	
	private void populateOutputDirectory(File outputDir) {
		this.OutputDirectory = outputDir;
		this.outputScrollPane.fillTextArea(OutputDirectory);
	}
	
	private void resetFiles(ArrayList<File> files, JTMButton resetButton) {
		this.files.removeAll(files);
		this.FilesLabel.setText("Files: " + files.size());
		this.fileScrollPane.fillTextArea(files);
		this.enableResetButton(resetButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == InputFilesButton) {
			//Probably better to flush the ArrayList of files before doing anityh
			if(!this.files.isEmpty()) {
				this.files.removeAll(this.files);
				this.mainParser.setFilesToParse(this.files);
				this.chooseFiles(true, JFileChooser.FILES_ONLY);
				this.mainParser.setFilesToParse(this.files);
				this.files.forEach(s -> System.out.println(s.getAbsolutePath()));
			} else {
				this.chooseFiles(true,  JFileChooser.FILES_ONLY);
				this.mainParser.setFilesToParse(this.files);
				this.files.forEach(s -> System.out.println(s.getAbsolutePath()));
			}
			
		}
		
		if(e.getSource() == OutputDirectoryButton) { 
			this.chooseOutputDirectory(false, JFileChooser.DIRECTORIES_ONLY);
			this.mainWriter.setOutputDirectory(OutputDirectory);
		}
		
		if(e.getSource() == this.ConvertButton) {
			System.out.println(mainWriter.getOutputDirectory().getAbsolutePath());
			this.mainParser.parse();
			this.mainWriter.setParser(mainParser);
			mainWriter.setFilesToWrite(mainWriter.convertFilesToOutput(this.files));
			this.mainWriter.getFilesToWrite().forEach(f -> System.out.println("file: " + f.getAbsolutePath()));
			this.mainWriter.writeFiles();
		}
		
		if(e.getSource() == this.flushFilesButton) {
			this.resetFiles(this.files, this.flushFilesButton);;
		}
	}

}
