package processor;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.function.Predicate;


public class Method {
	private String header;
	private String nonFormattedHeader;
	private ArrayList<String> body;
	private ArrayList<String> headerSignature;
	private ArrayList<String> headerParameters;
	private ArrayList<String> headerExceptions;
	private boolean isAbstract = false;
	private boolean isStatic = false;
	private boolean isNative = false;
	private boolean isSynchronized = false;
	private boolean hasGenerics = false;
	private boolean isFinal = false;
	private boolean isDefault = false;
	// THIS IS REALLY STUPID AND FUCKED UP.
	// NEVER PROGRAM LIKE THIS.
	private boolean ranOnce = false;

	public Method() {
		this.header = "";
		this.body = new ArrayList<String>();
		this.headerSignature = new ArrayList<String>();
		this.headerParameters = new ArrayList<String>();
		this.headerExceptions = new ArrayList<String>();
	}

	public boolean isRanOnce() {
		return ranOnce;
	}

	public void setRanOnce(boolean ranOnce) {
		this.ranOnce = ranOnce;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

	public boolean isHasGenerics() {
		return hasGenerics;
	}

	public void setHasGenerics(boolean hasGenerics) {
		this.hasGenerics = hasGenerics;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public ArrayList<String> getHeaderSignature() {
		return headerSignature;
	}

	public void setHeaderSignature(ArrayList<String> headerSignature) {
		this.headerSignature = headerSignature;
	}

	public ArrayList<String> getHeaderParameters() {
		return headerParameters;
	}

	public void setHeaderParameters(ArrayList<String> headerParameters) {
		this.headerParameters = headerParameters;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ArrayList<String> getBody() {
		return body;
	}

	public void setBody(ArrayList<String> body) {
		this.body = body;
	}

	public ArrayList<String> getHeaderExceptions() {
		return headerExceptions;
	}

	public void setHeaderExceptions(ArrayList<String> headerExceptions) {
		this.headerExceptions = headerExceptions;
	}
	

	public String getNonFormattedHeader() {
		return nonFormattedHeader;
	}

	public void setNonFormattedHeader(String nonFormattedHeader) {
		this.nonFormattedHeader = nonFormattedHeader;
	}

	@Override
	public String toString() {
		return String.format("Method header:%s%nMethod body: %s%n", header, body);
	}

	private String splitHeaderSignatureByThrows() {
		String[] methodSignature = this.header.split("throws");

		if (methodSignature.length > 1 && this.ranOnce == false) {
			String[] exceptionsThrown = methodSignature[1].split(",");
			for (int i = 0; i < exceptionsThrown.length; i++) {
				this.headerExceptions.add(exceptionsThrown[i].strip());
			}
		}
		return methodSignature[0];
		
	}

	private String[] splitHeaderSignatureAndParameters(String string) {
		String[] methodSignatureAndParams = this.header.split("\\(");
		methodSignatureAndParams[1] = "(" + methodSignatureAndParams[1];
		return methodSignatureAndParams;
	}

	private String formatGenericOutOfSignature(String string) {
		// Generic info will always be on position 1
		// It's splitting by "<" and ">".
		// If there's no generic the length of data will always be 1. Otherwise it will
		// be 2 or more.
		// The regex gets us any character that's between < and > at position 1.
		this.setNonFormattedHeader(string);
		
		// Much better way of handling this, at least is cleaner.
		ArrayList<String> tmpArray = new ArrayList<String>(Arrays.asList(string.split("\\s(?![^<>]*>)")));

		Predicate<String> doesntHaveGenericNotation = s -> (!s.startsWith("<") || !s.endsWith(">"));

		tmpArray.removeIf(doesntHaveGenericNotation);

		if (!tmpArray.isEmpty()) {
			return tmpArray.get(0);
		}

		return "";
	}

	private String formatSignatureWithoutGeneric(String string) {
		// We now split again by generic so we get the A and B instead of the things in
		// "<" & ">"
		String[] data = string.split("<.*>");
		// We create an empty string so we can build the method signature as if it there
		// was
		// no generic.
		String headerWithoutGeneric = "";

		for (int i = 0; i < data.length; i++) {
			// Strip leading is super important here! This will make sure anything is always
			// lead by 1 space. The trailing space of the part that goes before.
			// It will prevent this situation (Spaces are replaced with * for visualization)
			// A*****B*C*D --> A*B*C*D
			// Where A, B C, D, will only be allowed to have * after them.
			headerWithoutGeneric += data[i];
		}

		return headerWithoutGeneric;
	}

	private String[] generateArrayOfHeaderFragments(String string, String genericContent) {
		String[] processedHeaderWithoutGeneric = string.split("\s");

		// We create a new ArrayList in order to insert things in the order we need.
		ArrayList<String> processingHeader = new ArrayList<String>();

		// We add everything from the non-generic signature into thearray.
		for (int i = 0; i < processedHeaderWithoutGeneric.length; i++) {
			processingHeader.add(processedHeaderWithoutGeneric[i]);
		}

		// We check if we have a generic via the genericContent local variable.
		if (!genericContent.equals("")) {
			// If it's the case then we add it at index -2 of size since a generic will be
			// present before the return type and method name.
			// It will always be -2 since you need to declare those two everytime you
			// declare a method.
			// It still works if the method doesn't return anything since void still needs
			// to be used.
			processingHeader.add(processingHeader.size() - 2, genericContent);
		}

		return processingHeader.toArray(new String[processingHeader.size()]);
	}

	private String[] splitMethodSignature(String[] strings) {
		// Out of the param we get the signature that is Strings[0] as the strings value
		// passed will
		String headerSignatureInformation = strings[0];
		// We create a variable that will store the value of our generic.
		String genericContent = formatGenericOutOfSignature(headerSignatureInformation);
		String headerStringWithoutGeneric = formatSignatureWithoutGeneric(headerSignatureInformation);
		String[] result = generateArrayOfHeaderFragments(headerStringWithoutGeneric, genericContent);
		return result;
	}

	private void addSignatureInformationToHeaderSignature(String[] information) {
		for (int i = 0; i < information.length; i++) {
			this.headerSignature.add(information[i].strip());
		}
	}

	private String[] splitMethodParameters(String[] strings) {
		if(this.headerExceptions.isEmpty()) {
			strings[1] = strings[1].substring(1, strings[1].length() -1);			
		} else {
			strings[1] = strings[1].substring(1, strings[1].length());	
		}
		return strings[1].split(",");
	}

	private void addParametersToHeaderParameters(String[] parameters) {
		for (int i = 0; i < parameters.length; i++) {
			this.headerParameters.add(parameters[i].strip());
		}
	}

	public void processMethod() {

		// Make sure there's no trailing or leading whitespaces.
		header = this.header.strip();
		String[] signatureInfo = this
				.splitMethodSignature(this.splitHeaderSignatureAndParameters(this.splitHeaderSignatureByThrows()));
		
		this.setRanOnce(true);
		String[] parameters = this
				.splitMethodParameters(this.splitHeaderSignatureAndParameters(this.splitHeaderSignatureByThrows()));


		this.addSignatureInformationToHeaderSignature(signatureInfo);
		this.addParametersToHeaderParameters(parameters);
		this.setupModifiers();
		// String[] a = this.header.split("\\(.*\\)"); Gets modifiers and method name
		// System.out.println(Arrays.toString(a));

		/*
		 * headerSignature.forEach(v -> { System.out.println("SigInfo: " + v); });
		 * 
		 * headerParameters.forEach(v -> { System.out.println("Param: " + v); });
		 */
	}

	private void hasStaticModifier() {
		if (this.headerSignature.contains("static")) {
			this.isStatic = true;
		} else {
			this.isStatic = false;
		}
		;
	}

	private void hasAbstractModifier() {
		if (this.headerSignature.contains("abstract")) {
			this.isAbstract = true;
		} else {
			this.isAbstract = false;
		}
	}

	private void hasNativeModifier() {
		if (this.headerSignature.contains("native")) {
			this.isNative = true;
		} else {
			this.isNative = false;
		}
	}

	private void hasSynchronizedModifier() {
		if (this.headerSignature.contains("synchronized")) {
			this.isSynchronized = true;
		} else {
			this.isSynchronized = false;
		}
	}

	private void hasFinalModifier() {
		if (this.headerSignature.contains("final")) {
			this.isFinal = true;
		} else {
			this.isFinal = false;
		}
	}

	private void hasGenerics() {
		this.headerSignature.forEach(string -> {
			if (string.matches("<.*>")) {
				this.hasGenerics = true;
			}
		});
	}

	public boolean hasModifiers() {
		if (this.hasGenerics || this.isAbstract || this.isDefault || this.isFinal || this.isNative || this.isStatic
				|| this.isSynchronized)
			return true;
		return false;
	}

	// Maybe it would be better to use strategy pattern here?
	// ClassDiagramer -> DiagramMethod & DiagramParameter? This would be better on
	// the good version of this lol
	
	

	
	private void setupModifiers() {
		this.hasGenerics();
		this.hasAbstractModifier();
		this.hasFinalModifier();
		this.hasNativeModifier();
		this.hasStaticModifier();
		this.hasSynchronizedModifier();
	}
}
