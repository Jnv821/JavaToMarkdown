package processor;
import java.util.ArrayList;

public class Document {

	private String documentName;
	private ArrayList<Method> methods;
	private ArrayList<Parameter> parameters;
	private ArrayList<Class> classes;
	
	public Document() {
		this.methods = new ArrayList<Method>();
		this.parameters =  new ArrayList<Parameter>();
		this.classes = new ArrayList<Class>();
	}
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public ArrayList<Method> getMethods() {
		return methods;
	}
	public void setMethods(ArrayList<Method> methods) {
		this.methods = methods;
	}
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	public ArrayList<Class> getClasses() {
		return classes;
	}
	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}
	
	public void processDocument() {
	this.classes.forEach(value -> {
			value.processClassHeader();
		});
		
		this.parameters.forEach(value -> {
			value.processParameter();
		});
		
		this.methods.forEach(value -> {
			value.processMethod();
		});
	}
	public void displayDocument() {
		System.out.println("=".repeat(30) + this.documentName + "=".repeat(30));
		System.out.println();
		System.out.println("=".repeat(30) + " Class Information " + "=".repeat(30));
		System.out.println();
		if(this.classes.size() > 0) { this.displayClassParsed(); } else {System.out.println("No classes");}
		System.out.println("=".repeat(30) + " Param Information " + "=".repeat(30));
		System.out.println();
		if(this.parameters.size() > 0) { this.displayParametersParsed(); } else {System.out.println("No Params");}
		System.out.println("=".repeat(30) + " Method Information " + "=".repeat(30));
		System.out.println();
		if(this.methods.size() > 0) { this.displayMethodsParsed(); } else {System.out.println("No Methods");}
	}
	
	public void displayClassParsed() {
		classes.forEach(v -> {
			System.out.println("=".repeat(50));
			System.out.println("Header: " + v.getHeader() + "\n");
			
			v.getHeaderSignature().forEach(sig -> {
				System.out.println("Signature Fragment: " + sig);
			});
			
			v.getHeaderExtensions().forEach(ext -> {
				System.out.println("Signature Extension: " + ext);
			});
			v.getHeaderImplements().forEach(imp -> {
				System.out.println("Signature Implement: " + imp);
			});
		});
	}
	
	public void displayParametersParsed() {
		parameters.forEach(v -> {
			System.out.println("=".repeat(50));
			System.out.println("Header: " + v.getHeader() + "\tDefault: " + v.getDefaultValue()+"\n");
			v.getHeaderSignature().forEach(n -> {
				System.out.println("Header Signature Fragment: " + n);
			});
		});
	}
	public void displayMethodsParsed() {
		methods.forEach(v -> {
			System.out.println("=".repeat(50));
			System.out.println("Header: " + v.getHeader() + "\n");
			v.getHeaderSignature().forEach(n -> {
				System.out.println("Header Signature Fragment: " + n);
			});
			System.out.println("Body: ");
			v.getBody().forEach(line -> {
				System.out.println(line);
			});
			System.out.println("Exceptions");
			v.getHeaderExceptions().forEach(e -> {
				System.out.println("Exception: " + e);
			});
		});
	}
	
	public void displayMethodFragments() {
		methods.forEach(v -> {
			System.out.println("Method:");
			v.getHeaderSignature().forEach(k -> {
				System.out.println("Fragment: " + k);
			});
		});
	}
	
	public String getDocumentNameWithoutExtension() {
		return this.getDocumentName().replaceFirst("[.][^.]+$", "");
	}
}
