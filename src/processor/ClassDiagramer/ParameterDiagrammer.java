package processor.ClassDiagramer;

import processor.Parameter;

public class ParameterDiagrammer {

	public String createParameterStatement(Parameter p) {
		String diagramParameter;
		
		diagramParameter = this.classDiagramerGenerateAccessModifier(p);
		diagramParameter += this.generateParameterName(p);
		diagramParameter += " " + this.classDiagrammerGetType(p);
		diagramParameter = this.classDiagramerCleanUpForGenerics(diagramParameter);
		
		return diagramParameter;
	}
	
	private String classDiagramerGenerateAccessModifier(Parameter p) {
		switch (p.getHeaderSignature().get(0)) {
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
	
	
	private String generateParameterName(Parameter p) {
		return p.getHeaderSignature().get(p.getHeaderSignature().size()-1);
	}
	
	
	public void testParameterDiagrammer(String...strings) {
		for (int i = 0; i < strings.length; i++) {
			Parameter tp = new Parameter();
			tp.setHeader(strings[i]);
			tp.processParameter();
			System.out.println(this.createParameterStatement(tp));
	}
		};
	
	private String classDiagrammerGetType(Parameter p) {
		String[] data = p.getNonFormattedHeader().split("\\s(?![^<>]*>)");
		return data[data.length-2];
	};
		
	private String classDiagramerCleanUpForGenerics(String s) {
		return s.replaceAll("<", "~").replaceAll(">", "~");
	};
}
