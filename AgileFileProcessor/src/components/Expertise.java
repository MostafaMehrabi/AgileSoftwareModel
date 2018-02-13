package components;

public class Expertise {
	private String backEnd, frontEnd, design;
	public Expertise(String backEnd, String frontEnd, String design) {
		this.backEnd = backEnd;
		this.frontEnd = frontEnd;
		this.design = design;
	}
	
	public String getBackEnd() {return backEnd;}
	
	public String getFrontEnd() {return frontEnd;}
	
	public String getDesign() {return design;}
}
