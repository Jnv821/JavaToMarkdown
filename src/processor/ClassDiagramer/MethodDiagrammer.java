package processor.ClassDiagramer;

import java.util.stream.Collectors;

import processor.Method;

public class MethodDiagrammer {

	
	public String createClassDiagramStatement(Method m) {
		String diagramMethod;
		
		diagramMethod = this.classDiagramerGenerateAccessModifier(m);

		diagramMethod += this.classDiagramerGenerateMethodName(m);

		diagramMethod += this.classDiagramerGenerateParameters(m);

		diagramMethod += " " + this.classDiagramerGetReturnType(m);

		diagramMethod = this.classDiagramerCleanUpForGenerics(diagramMethod);

		return diagramMethod;
	}

	private String classDiagramerGenerateAccessModifier(Method m) {
		switch (m.getHeaderSignature().get(0)) {
			case "public":
				return "+";
			case "private":
				return"-";
			case "protected":
				return "#";
			default:
				return"~";
		}
	}
	
	private String classDiagramerGenerateMethodName(Method m) {
		return m.getHeaderSignature().get(m.getHeaderSignature().size()-1);
	}
	
	private String classDiagramerGenerateParameters(Method m) {
		if(m.getHeaderExceptions().isEmpty()) {
			return "(" + m.getHeaderParameters().stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
		} else {
			return "(" + m.getHeaderParameters().stream().map(Object::toString).collect(Collectors.joining(", "));
		}
	}

	private String classDiagramerCleanUpForGenerics(String s) {
		return s.replaceAll("<", "~").replaceAll(">", "~");
	}
	
	private String classDiagramerGetReturnType(Method m) {
		
		String[] data = m.getNonFormattedHeader().split("\\s(?![^<>]*>)");
		return data[data.length-2];
		
	}
	
	
	
	public void testClassDiagram(String ...strings ) {
		for (int i = 0; i < strings.length; i++) {
			// The error is actually here. You're supposed to create a new Method object bruh 
			Method testMethod = new Method();
			testMethod.setHeader(strings[i]);
			testMethod.processMethod();
			System.out.println(this.createClassDiagramStatement(testMethod));
		}
	}
}
