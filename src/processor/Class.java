package processor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class Class {
	private String header;
	private ArrayList<String> headerSignature;
	private ArrayList<String> headerImplements;
	private ArrayList<String> headerExtensions;
	private boolean hasImplementations;
	private boolean hasExtensions;
	private boolean hasGenerics;
	private boolean isFinal;
	private boolean isAbstract;
	
	public Class() {
		this.headerSignature = new ArrayList<String>();
		this.headerImplements = new ArrayList<String>();
		this.headerExtensions = new ArrayList<String>();
	}
	
	
	public boolean isHasImplementations() {
		return hasImplementations;
	}


	public void setHasImplementations(boolean hasImplementations) {
		this.hasImplementations = hasImplementations;
	}


	public boolean isHasExtensions() {
		return hasExtensions;
	}


	public void setHasExtensions(boolean hasExtensions) {
		this.hasExtensions = hasExtensions;
	}


	public boolean isHasGenerics() {
		return hasGenerics;
	}


	public void setHasGenerics(boolean hasGenerics) {
		this.hasGenerics = hasGenerics;
	}


	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ArrayList<String> getHeaderSignature() {
		return headerSignature;
	}
	public void setHeaderSignature(ArrayList<String> headerSignature) {
		this.headerSignature = headerSignature;
	}
	public ArrayList<String> getHeaderImplements() {
		return headerImplements;
	}
	public void setHeaderImplements(ArrayList<String> headerImplements) {
		this.headerImplements = headerImplements;
	}
	public ArrayList<String> getHeaderExtensions() {
		return headerExtensions;
	}
	public void setHeaderExtensions(ArrayList<String> headerExtensions) {
		this.headerExtensions = headerExtensions;
	}	
	
	
	public boolean isFinal() {
		return isFinal;
	}


	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}


	public boolean isAbstract() {
		return isAbstract;
	}


	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}


	private String[] splitHeaderByImplements() {
		String [] splittedByImplements = this.header.split("implements");
		if(splittedByImplements.length > 1) {
			this.setHasImplementations(true);
			splittedByImplements[1] = splittedByImplements[1].substring(0, splittedByImplements[1].length()-1).strip();
			String[] implementations = this.splitImplementations(splittedByImplements[1]);
			this.addFragmentsToHeaderImplements(implementations);
		} else {
			this.setHasImplementations(false);
		}
		return splittedByImplements;
	}
	
	private String [] splitImplementations(String string) {
		return string.split(",");
	}
	
	private void addFragmentsToHeaderImplements(String[] strings){
		for(int i = 0; i < strings.length; i++) {
			this.headerImplements.add(strings[i].strip());
		}
	}
	
	private String[] splitHeaderByExtensions(String string) {
		String [] splittedByExtends = string.split("extends");
		if(splittedByExtends.length > 1 ) {
			this.setHasExtensions(true);
			this.getHeaderExtensions().add(splittedByExtends[1].strip());
 		} else {
 			this.setHasExtensions(false);
 		}
		return splittedByExtends;
	}
	

	private String formatGenericOutOfSignature(String string) {

		ArrayList<String> tmpArray = new ArrayList<String>(Arrays.asList(string.split("\\s(?![^<>]*>)")));
		
		Predicate<String> doesntHaveGenericNotation = s -> (!s.startsWith("<") || !s.endsWith(">"));
		
		tmpArray.removeIf(doesntHaveGenericNotation);
		
		if (!tmpArray.isEmpty())
			return tmpArray.get(0);

		return "";
	}
	

	private String formatSignatureWithoutGeneric(String string) {
		String[] data = string.split("<.*>");
		String headerWithoutGeneric = "";

		for (int i = 0; i < data.length; i++) {
	
			headerWithoutGeneric += data[i].stripLeading();
		}

		return headerWithoutGeneric;
	}
	
	private String[] generateArrayOfHeaderFragments(String string, String genericContent) {
		String[] processedHeaderWithoutGeneric = string.split("\s");

		// We create a new ArrayList in order to insert things in the order we need.
		ArrayList<String> processingHeader = new ArrayList<String>();

		// We add everything from the non-generic signature into the array.
		for (int i = 0; i < processedHeaderWithoutGeneric.length; i++) {
			processingHeader.add(processedHeaderWithoutGeneric[i]);
		}
		
		// We check if we have a generic via the genericContent local variable.
		if (!genericContent.equals("")) {
			// If it's the case then we add it at index -2 of size since a generic will be
			// present before the return type and method name. 
			//It will always be -2 since you need to declare those two everytime you declare a method. 
			//It still works if the method doesn't return  anything since void  still needs to be used.
			processingHeader.add(processingHeader.size(), genericContent);
		}
		
		return processingHeader.toArray(new String[processingHeader.size()]);
	}

	private void addHeaderFragmentsToHeader(String[] information) {
		for(int i = 0; i < information.length; i++) {
			this.headerSignature.add(information[i].strip());
		}
	}
	
	public void processClassHeader() {
		
		String Data = this.splitHeaderByExtensions(this.splitHeaderByImplements()[0])[0];
		
		String genericContent = formatGenericOutOfSignature(Data);
		String headeStringWithoutGeneric = formatSignatureWithoutGeneric(Data);
		this.addHeaderFragmentsToHeader(this.generateArrayOfHeaderFragments(headeStringWithoutGeneric, genericContent));
		this.formatHeader();
		this.setupModifiers();
	}
	
	private void formatHeader() {
		this.header = header.substring(0, header.length()-1).strip();
	}
	
	private void hasFinalModifier() {
		if (this.headerSignature.contains("final")) {
			this.isFinal = true;
		} else {
			this.isFinal = false;
		}
	}
	
	private void hasAbstractModifier() {
		if (this.headerSignature.contains("abstract")) {
			this.isAbstract = true;
		} else {
			this.isAbstract = false;
		}
	}
	
	public void setupModifiers() {
		this.hasFinalModifier();
		this.hasAbstractModifier();
		this.hasGenerics();
	}
	
	public boolean hasModifiers() {
		if(this.hasGenerics || this.isAbstract || this.isFinal) return true;
		return false;
	}
	private void hasGenerics() {
		this.headerSignature.forEach(string -> {
			if (string.matches("<.*>")) {
				this.hasGenerics = true;
			}
		});
	}
	
}
