package data.model;

import java.util.ArrayList;

public class Group {

	private String name;
	private ArrayList<LocalDerivative> groupLocalDerivatives;
	
	public String toString(){
		return name;
	}
	
	public int numLocalDerivative(){
		return groupLocalDerivatives.size();
	}
	
	// returns the maximum of the leghths of the names of the local derivatives.
	public int maxLocalDerivativeLength(){
		
		int max = 0 ; 
		
		for (LocalDerivative derivative : groupLocalDerivatives){
			if (derivative.length() > max){
				max = derivative.length();
			}
		}
		
		return max;
		
	}
}
