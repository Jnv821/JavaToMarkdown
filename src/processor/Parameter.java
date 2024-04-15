package processor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class Parameter {
	private String header;
	private String nonFormattedHeader;
	private String defaultValue;
	private ArrayList<String> headerSignature;
	private boolean isTransient = false;
	private boolean isVolatile = false;
	private boolean isFinal = false;
	private boolean isStatic = false;
	private boolean hasGenerics = false;
	
	public Parameter() {
		this.header = "";
		this.defaultValue = "";
		this.headerSignature = new ArrayList<String>();
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public ArrayList<String> getHeaderSignature() {
		return headerSignature;
	}

	public void setHeaderSignature(ArrayList<String> headerSignature) {
		this.headerSignature = headerSignature;
	}
	
	public boolean isTransient() {
		return isTransient;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public boolean isVolatile() {
		return isVolatile;
	}

	public void setVolatile(boolean isVolatile) {
		this.isVolatile = isVolatile;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isHasGenerics() {
		return hasGenerics;
	}

	public void setHasGenerics(boolean hasGenerics) {
		this.hasGenerics = hasGenerics;
	}
	

	public String getNonFormattedHeader() {
		return nonFormattedHeader;
	}

	public void setNonFormattedHeader(String nonFormattedHeader) {
		this.nonFormattedHeader = nonFormattedHeader;
	}

	private String[] splitHeader(String string) {
		String [] splittedHeader = string.split("=");
		if(splittedHeader.length > 1) {
			this.defaultValue = splittedHeader[splittedHeader.length-1];
		}
		return splittedHeader;
	}
	
	private String formatGenericOutOfSignature(String string) {
		
		this.setNonFormattedHeader(string);

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
		for(int i = 0; i < data.length; i++) {
			headerWithoutGeneric += data[i].strip();
		}
		return headerWithoutGeneric;
	}
	
	private String[] generateArrayOfHeaderFragments(String string, String genericContent) {
		String [] proccessHeaderWithoutGeneric = string.split("\s");
		
		ArrayList<String> proccessingHeader = new ArrayList<String>();
		
		for(int i = 0; i < proccessHeaderWithoutGeneric.length; i++) {
			proccessingHeader.add(proccessHeaderWithoutGeneric[i]);
		}
		
		if(!genericContent.equals("")) {
			proccessingHeader.add(proccessHeaderWithoutGeneric.length-1, genericContent);
		}
		
		return proccessingHeader.toArray(new String[proccessingHeader.size()]);
	}
	
	public String[] splitParameterSignature(String string) {
		
		String [] splittedHeaderParameter = this.splitHeader(string);
		String genericContent = formatGenericOutOfSignature(splittedHeaderParameter[0]);
		String headerStringWithoutGeneric = formatSignatureWithoutGeneric(splittedHeaderParameter[0]);
		String [] result = generateArrayOfHeaderFragments(headerStringWithoutGeneric,genericContent);
		return result;
	}
	
	private void addSignatureInfoToHeaderSignature(String[] information) {
		for(int i = 0; i < information.length; i++) {
			this.headerSignature.add(information[i].strip());
		}
	};
	
	public void processParameter() {
		header = this.header.replace(";", "");
		header = this.header.strip();
		String[] SignatureInfo = this.splitParameterSignature(header);
		this.addSignatureInfoToHeaderSignature(SignatureInfo);
		this.setupModifiers();
		
	}
	
	private void hasStaticModifier() {
		if (this.headerSignature.contains("static")) {
			this.isStatic = true;
		} else {
			this.isStatic = false;
		}
		;
	}


	private void hasFinalModifier() {
		if (this.headerSignature.contains("final")) {
			this.isFinal = true;
		} else {
			this.isFinal = false;
		}
	}
	 

	private void hasVolatileModifier() {
		if (this.headerSignature.contains("volatile")) {
			this.isVolatile = true;
		} else {
			this.isVolatile = false;
		}
	}
	
	private void hasTransientModifier() {
		if (this.headerSignature.contains("transient")) {
			this.isTransient = true;
		} else {
			this.isTransient = false;
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
		if(this.hasGenerics
				|| this.isFinal
				|| this.isStatic
				|| this.isTransient
				|| this.isVolatile) return true;
		return false;
	}
	
	private void setupModifiers() {
		this.hasGenerics(); 
		this.hasFinalModifier();
		this.hasStaticModifier();
		this.hasVolatileModifier();
		this.hasTransientModifier();
	}
}
