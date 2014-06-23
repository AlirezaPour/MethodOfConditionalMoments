package data.aggregatedModel;

import java.util.HashMap;

import data.model.NumericalVector;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class AggregatedState extends NumericalVector{
	
	public AggregatedState(StateDescriptor desciptor){

		for(StateVariable variable : desciptor){
			put(variable, 0);
		}
		
	}
	
	
	public AggregatedState() {
		super();
	}
	
	
	
	/*public String toString(){
		
	}*/
	
	
}
