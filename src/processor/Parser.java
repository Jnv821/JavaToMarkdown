package processor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	private File fileToParse;
	private ArrayList<File> filesToParse;
	private ArrayList<Document> documents;
	
	public Parser(File file) {
		this.filesToParse = new ArrayList<File>(Arrays.asList(file));

		this.documents = new ArrayList<Document>();
	}

	public Parser(File... files) {
		this.filesToParse = new ArrayList<File>(Arrays.asList(files));

		this.documents = new ArrayList<Document>();
	}
	
	

	public ArrayList<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<Document> documents) {
		this.documents = documents;
	}

	public File getFileToParse() {
		return fileToParse;
	}

	public void setFileToParse(File fileToParse) {
		this.fileToParse = fileToParse;
	}

	public ArrayList<File> getFilesToParse() {
		return filesToParse;
	}

	public void setFilesToParse(ArrayList<File> filesToParse) {
		this.filesToParse = filesToParse;
	}



	private String formatMethodHeader(String string) {
		string = string.strip();
		return string.substring(0, string.length() - 1);
	}

	private ArrayList<String> formatMethodbody(ArrayList<String> body) {
		body.add(0, "{");
		;
		body.remove(body.size() - 1);
		body.set(body.size() - 1, body.get(body.size() - 1).strip());
		return body;
	}

	public ArrayList<Method> readMethod(File fileToParse, Document doc) {

		Method methodComponent = new Method();

		try (BufferedReader bffReader = new BufferedReader(new FileReader(fileToParse))) {
			String line = bffReader.readLine();
			boolean isMethod = false;
			while (line != null) {

				if (line.strip().equals("//@StartOfMethod")) {
					isMethod = true;
					line = bffReader.readLine();
					methodComponent = new Method();
					methodComponent.setHeader(this.formatMethodHeader(line));
				}

				if (line.strip().equals("//@EndOfMethod")) {
					isMethod = false;
					this.formatMethodbody(methodComponent.getBody());
					doc.getMethods().add(methodComponent);
				}

				line = bffReader.readLine();

				if (isMethod == true) {
					methodComponent.getBody().add(line);
				}
			}
			return doc.getMethods();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return doc.getMethods();
	}

	public ArrayList<Parameter> readParameter(File fileToParse, Document doc) {
		Parameter parameterComponent = new Parameter();
		
		try (BufferedReader bffReader = new BufferedReader(new FileReader(fileToParse))) {
			String line = bffReader.readLine();
			boolean isParameter = false;
			while(line != null) {
				if (line.strip().equals("//@StartOfParameters")) {
					isParameter = true;
					line = bffReader.readLine();
				
				}

				if (line.strip().equals("//@EndOfParameters")) {
					isParameter= false;
				}

		

				if (isParameter == true) {
					parameterComponent = new Parameter();
					parameterComponent.setHeader(line);
					doc.getParameters().add(parameterComponent);
				}
				
				line = bffReader.readLine();
				
			}
			return doc.getParameters();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return doc.getParameters();
	}
	
	public ArrayList<Class> readClass(File fileToParse, Document doc){
		Class classComponent = new Class();	
		
		try (BufferedReader bffReader = new BufferedReader(new FileReader(fileToParse))) {
				String line = bffReader.readLine();
				boolean isParameter = false;
				while(line != null) {
					if (line.strip().equals("//@StartOfClass")) {
						isParameter = true;
						line = bffReader.readLine();
				
					}

					if (line.strip().equals("//@EndOfClass")) {
						isParameter= false;
					}

		

					if (isParameter == true) {
						classComponent = new Class();
						classComponent.setHeader(line);
						doc.getClasses().add(classComponent);
					}
				
					line = bffReader.readLine();
				
				}
				return doc.getClasses();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		return doc.getClasses();
	}

	public void parseFile(File file) {

		Document documentComponent = new Document();
		documentComponent.setDocumentName(file.getName());
		this.readClass(file, documentComponent);
		this.readParameter(file, documentComponent);
		this.readMethod(file, documentComponent);
		documentComponent.processDocument();
		this.documents.add(documentComponent);
	};
	

	
	public void parse() {
		filesToParse.forEach(file -> {
			System.out.println("Parsing file: " + file.getName());
			this.parseFile(file);
		});}
	
}
