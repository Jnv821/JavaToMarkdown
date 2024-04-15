package processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import processor.ClassDiagramer.ClassDiagramer;

public class Writer {
	private File outputDirectory;
	private ArrayList<File> filesToWrite;
	private Parser parser;
	private ClassDiagramer classDiagrammer;
	
	public Writer(Parser parser, File outputDirectory) {
		this.parser = parser;
		this.classDiagrammer = new ClassDiagramer();
		this.outputDirectory = outputDirectory;
		this.filesToWrite = new ArrayList<File>();
		parser.getFilesToParse().forEach(v -> {
			this.filesToWrite
					.add(new File(this.outputDirectory.getAbsolutePath() + "\\" + v.getName().replace(".java", ".md")));
		});
	};

	public ArrayList<File> convertFilesToOutput(ArrayList<File> files) {
		ArrayList<File> converted = new ArrayList<File>();
		files.forEach(file -> {
			converted.add(new File(this.outputDirectory.getAbsolutePath() + "\\" + file.getName().replace(".java", ".md")));
		});
		
		return converted;
	}
	
	public ArrayList<File> getFilesToWrite() {
		return filesToWrite;
	}

	public void setFilesToWrite(ArrayList<File> filesToWrite) {
		this.filesToWrite = filesToWrite;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	
	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public ClassDiagramer getClassDiagrammer() {
		return classDiagrammer;
	}

	public void setClassDiagrammer(ClassDiagramer classDiagrammer) {
		this.classDiagrammer = classDiagrammer;
	}

	public void checkName() {
		this.filesToWrite.forEach(v -> {
			System.out.println(v.getAbsolutePath());
		});
	}

	private void createFiles(File file) {

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void writeTitle(BufferedWriter bffWriter, int documentIndex) throws IOException {
		Document document = this.parser.getDocuments().get(documentIndex);
		bffWriter.write("## Diagrama de clase");
		bffWriter.newLine();
		this.writeClassDiagram(bffWriter, document);
	}

	public void writeClass(BufferedWriter bffWriter, int documentIndex) throws IOException {
		Document document = this.parser.getDocuments().get(documentIndex);
		bffWriter.write("## Información general");
		bffWriter.newLine();
		document.getClasses().forEach(c -> System.out.println("A Class here: " + c.getHeader()));
		
		if(document.getClasses().size() == 0) return;
		
		if(!document.getClasses().get(documentIndex).getHeaderExtensions().isEmpty()) {			
			this.writeExtensions(bffWriter, document, documentIndex);
		}

		if(!document.getClasses().get(documentIndex).getHeaderImplements().isEmpty()) {			
			this.writeImplementations(bffWriter, document, documentIndex);
		}
		if(document.getClasses().get(documentIndex).hasModifiers()) {			
			this.writeClassModifiers(bffWriter, document, documentIndex);
		}
		
		bffWriter.newLine();
	}

	public void writeExtensions(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException {
			bffWriter.write("Hereda de: " + document.getClasses().get(documentIndex).getHeaderSignature().get(0));
			bffWriter.newLine();
	}

	public void writeImplementations(BufferedWriter bffWriter, Document document, int documentIndex)
			throws IOException {
			bffWriter.write("- Implementa: ");
			bffWriter.newLine();
			;
			for (int i = 0; i < document.getClasses().get(documentIndex).getHeaderImplements().size(); i++) {
				bffWriter.write("\t- " + document.getClasses().get(documentIndex).getHeaderImplements().get(i));
				bffWriter.newLine();
			};
		}

	
	public void writeClassModifiers(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException{
			bffWriter.write("Modificadores de comportamiento: ");
			bffWriter.newLine();
		
		if (document.getClasses().get(documentIndex).isFinal()) {
			bffWriter.write("\t- final");
			bffWriter.newLine();
			}
		if (document.getClasses().get(documentIndex).isAbstract()) {
			bffWriter.write("\t- abstract");
			bffWriter.newLine();
		}
		
	}

	public void writeParameters(BufferedWriter bffWriter, int documentIndex) throws IOException {
		Document document = this.parser.getDocuments().get(documentIndex);
		bffWriter.write("## Atributos");
		bffWriter.newLine();
		bffWriter.newLine();
		if(document.getParameters().size() == 0) return;
		for (int i = 0; i < document.getParameters().size(); i++) {
			bffWriter.write("### " + document.getParameters().get(i).getHeaderSignature()
					.get(document.getParameters().get(i).getHeaderSignature().size() - 1));
			bffWriter.newLine();
			if (document.getParameters().get(i).getHeaderSignature().contains("private")
					|| document.getParameters().get(i).getHeaderSignature().contains("public")
					|| document.getParameters().get(i).getHeaderSignature().contains("protected")) {
				bffWriter.write(
						"- Modificador de Acceso: " + document.getParameters().get(i).getHeaderSignature().get(0));
			} else {
				bffWriter.write("- Modificador de acceso:: Paquete");
			}
			bffWriter.newLine();
			writeParamModifiers(bffWriter, document, i);
			bffWriter.write("- Tipo de retorno: " + "`" + document.getParameters().get(i).getHeaderSignature()
					.get(document.getParameters().get(i).getHeaderSignature().size() - 2) + "`");
			bffWriter.newLine();
			writeDefaultValue(bffWriter, document, i);
			bffWriter.newLine();

		}
		bffWriter.newLine();
	}

	public void writeDefaultValue(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException {
		if (!document.getParameters().get(documentIndex).getDefaultValue().isEmpty()) {
			bffWriter.write("- Valor por defecto: " + document.getParameters().get(documentIndex).getDefaultValue());
			bffWriter.newLine();
		} else {
			bffWriter.write("- Valor por defecto: No inicializado");
			bffWriter.newLine();
		}
	}

	public void writeParamModifiers(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException {
		if (!document.getParameters().get(documentIndex).hasModifiers())
			return;
		bffWriter.write("- Modificadores de comportamiento:");
		bffWriter.newLine();

		if (document.getParameters().get(documentIndex).isFinal()) {
			bffWriter.write("\t- final");
			bffWriter.newLine();
		}
		if (document.getParameters().get(documentIndex).isStatic()) {
			bffWriter.write("\t- static");
			bffWriter.newLine();
		}
		if (document.getParameters().get(documentIndex).isTransient()) {
			bffWriter.write("\t- transient");
			bffWriter.newLine();
		}
		if (document.getParameters().get(documentIndex).isVolatile()) {
			bffWriter.write("\t- volatile");
			bffWriter.newLine();
		}
		if (document.getParameters().get(documentIndex).isHasGenerics()) {
			bffWriter.write("\t- Generic: " + "`" +
					document.getParameters().get(documentIndex).getHeaderSignature()
					.get(document.getParameters().get(documentIndex).getHeaderSignature().size() - 2) + "`");
			bffWriter.newLine();
		}
	}

	public void writeMethods(BufferedWriter bffWriter, int documentIndex) throws IOException {
		Document document = this.parser.getDocuments().get(documentIndex);
		bffWriter.write("## Métodos");
		bffWriter.newLine();
		if(document.getMethods().size() == 0) return;
		for (int i = 0; i < document.getMethods().size(); i++) {
			writeMethod(bffWriter, document, i);
		}
	}

	public void writeMethod(BufferedWriter bffWriter, Document document, int methodIndex) throws IOException {
		bffWriter.write("### " + document.getMethods().get(methodIndex).getHeaderSignature()
				.get(document.getMethods().get(methodIndex).getHeaderSignature().size() - 1));
		bffWriter.newLine();
		if (document.getMethods().get(methodIndex).getHeaderSignature().contains("public")
				|| document.getMethods().get(methodIndex).getHeaderSignature().contains("private")
				|| document.getMethods().get(methodIndex).getHeaderSignature().contains("protected")) {
			bffWriter.write(
					"- Modificador de acceso: " + document.getMethods().get(methodIndex).getHeaderSignature().get(0));
		} else {
			bffWriter.write("- Modificador de acceso: Paquete");
		}

		bffWriter.newLine();
		writeMethodModifiers(bffWriter, document, methodIndex);
		bffWriter.write("- Tipo de Retorno: " + "`" + document.getMethods().get(methodIndex).getHeaderSignature()
				.get(document.getMethods().get(methodIndex).getHeaderSignature().size() - 2) + "`");
		bffWriter.newLine();
		writeMethodExceptions(bffWriter, document, methodIndex);
		writeMethodBody(bffWriter,document,methodIndex);
		bffWriter.newLine();
	}

	public void writeMethodParameters(BufferedWriter bffWriter, Document document, int documentIndex)
			throws IOException {
		bffWriter.write("- Parametros: ");
		bffWriter.newLine();
		for (int i = 0; i < document.getMethods().get(documentIndex).getHeaderParameters().size(); i++) {
			writeMethodParameter(bffWriter, document, i);
		}
	}

	public void writeMethodParameter(BufferedWriter bffWriter, Document document, int documentIndex)
			throws IOException {
		bffWriter.write(
				"\t- Parametros" + document.getMethods().get(documentIndex).getHeaderParameters().get(documentIndex));
		bffWriter.newLine();
	}

	public void writeMethodModifiers(BufferedWriter bffWriter, Document document, int documentIndex)
			throws IOException {
		if (!document.getMethods().get(documentIndex).hasModifiers())
			return;

		bffWriter.write("- Modificadores de comportamiento:");
		bffWriter.newLine();

		if (document.getMethods().get(documentIndex).isAbstract()) {
			bffWriter.write("\t- abstract");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isDefault()) {
			bffWriter.write("\t- default");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isFinal()) {
			bffWriter.write("\t- final");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isNative()) {
			bffWriter.write("\t- native");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isStatic()) {
			bffWriter.write("\t- static");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isSynchronized()) {
			bffWriter.write("\t- synchronized");
			bffWriter.newLine();
		}

		if (document.getMethods().get(documentIndex).isHasGenerics()) {
			bffWriter.write("\t- Generic: " + "`" +document.getMethods().get(documentIndex).getHeaderSignature()
					.get(document.getMethods().get(documentIndex).getHeaderSignature().size() - 3) + "`");
			bffWriter.newLine();
		}
	}

	private void writeMethodExceptions(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException {
		if(!document.getMethods().get(documentIndex).getHeaderExceptions().isEmpty()) {
			bffWriter.write("- Excepciones:");
			bffWriter.newLine();
			for(int i = 0; i < document.getMethods().get(documentIndex).getHeaderExceptions().size(); i++) {
				bffWriter.write("\t- " + document.getMethods().get(documentIndex).getHeaderExceptions().get(i));
				bffWriter.newLine();
			}
		}
		bffWriter.newLine();
	};
	
	private void writeMethodBody(BufferedWriter bffWriter, Document document, int documentIndex) throws IOException {
		if(!document.getMethods().get(documentIndex).getBody().isEmpty()) {
			bffWriter.write("#### Implementación:");
			bffWriter.newLine();
			bffWriter.write("```java");
			bffWriter.newLine();
			for(int i = 0; i < document.getMethods().get(documentIndex).getBody().size(); i++) {
				if(i == 0) {
					bffWriter.write(document.getMethods().get(documentIndex).getHeader() + " " + document.getMethods().get(documentIndex).getBody().get(i));
				} else {
					bffWriter.write(document.getMethods().get(documentIndex).getBody().get(i));
				}
				bffWriter.newLine();
			}
			bffWriter.write("```");
			bffWriter.newLine();
		}
	}
	
	public void writeClassDiagram(BufferedWriter bffWriter, Document document) throws IOException {
		bffWriter.newLine();
		bffWriter.write("```mermaid");
		bffWriter.newLine();
		bffWriter.write("classDiagram");
		bffWriter.newLine();
		bffWriter.write("class " + document.getDocumentNameWithoutExtension() + "{");
		bffWriter.newLine();
		bffWriter.write(this.classDiagrammer.constructDiagramContent(document));
		bffWriter.write("}");
		bffWriter.newLine();
		bffWriter.write("```");
		bffWriter.newLine();
		bffWriter.newLine();
	}
	
	public void writeFiles() {
		this.filesToWrite.forEach(s ->  System.out.println("Supposed to write: " + s.getAbsolutePath()));
		for (int i = 0; i < filesToWrite.size(); i++) {
			System.out.println("Trying to create file " + filesToWrite.get(i).getAbsolutePath() + " in " + this.getOutputDirectory().getAbsolutePath());
			this.createFiles(filesToWrite.get(i));
			try (BufferedWriter bffWriter = new BufferedWriter(new FileWriter(filesToWrite.get(i).getAbsolutePath()))) {
				System.out.println("Writting to file" + filesToWrite.get(i).getName());
				this.writeTitle(bffWriter, i);
				this.writeClass(bffWriter, i);
				this.writeParameters(bffWriter, i);
				this.writeMethods(bffWriter, i);
				System.out.println("Finished Writting file: " + filesToWrite.get(i).getAbsolutePath());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void showText() {
		System.out.println("TEST OK");
		System.out.println(Arrays.toString(this.filesToWrite.toArray()));
		this.filesToWrite.forEach(s ->  System.out.println("Supposed to write: " + s.getAbsolutePath()));
	}
	
}

