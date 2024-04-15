package processor.ClassDiagramer;

import processor.Document;

public class ClassDiagramer {
	private MethodDiagrammer methodDiagrammer;
	private ParameterDiagrammer paramDiagrammer;
	public ClassDiagramer() {
		this.methodDiagrammer = new MethodDiagrammer();
		this.paramDiagrammer = new ParameterDiagrammer();
	}
	
	public String constructDiagramContent(Document doc) {
		return this.constructParametersIntoDiagram(doc) + this.constructMethodsIntoDiagram(doc) ;
		
	}
	
	public String constructParametersIntoDiagram(Document doc) {
		String params = "";
		
		for (int i = 0; i < doc.getParameters().size(); i++) {
			params += "\t" + paramDiagrammer.createParameterStatement(doc.getParameters().get(i)) + "\n";
		}
		
		return params;
	}
	
	public String constructMethodsIntoDiagram(Document doc) {
		String methods = "";
		
		for (int i = 0; i < doc.getMethods().size(); i++) {
			methods += "\t" + doc.getDocumentNameWithoutExtension() + " : " + methodDiagrammer.createClassDiagramStatement(doc.getMethods().get(i)) + "\n";	
			}
	
		return methods;
	}	
}
