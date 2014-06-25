package data.model;

import java.util.ArrayList;

public class Group {

	private String name;
	private ArrayList<LocalDerivative> localDerivatives;
	
	
	public Group(String name,ArrayList<LocalDerivative> derivative){
		this.name = name; 
		this.localDerivatives = derivative;
	}
	
	
	public ArrayList<LocalDerivative> getGroupLocalDerivatives() {
		return localDerivatives;
	}

	public void setGroupLocalDerivatives(
			ArrayList<LocalDerivative> groupLocalDerivatives) {
		this.localDerivatives = groupLocalDerivatives;
	}

	
	public String toString(){
		return name;
	}
	
	public int numLocalDerivative(){
		return localDerivatives.size();
	}
	
	// returns the maximum of the leghths of the names of the local derivatives.
	public int maxLocalDerivativeLength(){
		
		int max = 0 ; 
		
		for (LocalDerivative derivative : localDerivatives){
			if (derivative.length() > max){
				max = derivative.length();
			}
		}
		
		return max;
		
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		Group fgroup = (Group) obj;
		if (       (this.name).equals(fgroup.getName())          ){
			return true;
		}else{
			return false;
		}
	}

}
