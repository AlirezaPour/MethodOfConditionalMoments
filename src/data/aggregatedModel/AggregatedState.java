package data.aggregatedModel;

import java.util.HashMap;

import data.model.NumericalVector;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class AggregatedState extends NumericalVector{
	
	private String stateIdentifier;
	
	public AggregatedState(StateDescriptor desciptor){

		for(StateVariable variable : desciptor){
			put(variable, 0);
		}
		
	}
	
	
	public String getStateIdentifier() {
		return stateIdentifier;
	}


	public void setStateIdentifier(String stateIdentifier) {
		this.stateIdentifier = stateIdentifier;
	}


	public AggregatedState() {
		super();
	}
	
	
	
	/*public String toString(){
		
	}*/
	
	
}
