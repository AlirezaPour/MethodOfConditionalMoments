package data.model;

public class LocalDerivative {

	String name ;
	
	public LocalDerivative(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;		
	}
	
	public int length(){
		return name.length();
	}
}
