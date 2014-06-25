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
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		LocalDerivative fderivative = (LocalDerivative) obj;
		if (	(this.name).equals(fderivative.getName())		){
			return true;
		}else{
			return false;
		}
	} 
}
