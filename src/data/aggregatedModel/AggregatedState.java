package data.aggregatedModel;

import java.util.HashMap;

import data.model.StateDescriptor;
import data.model.StateVariable;

public class AggregatedState extends HashMap<StateVariable, Integer>{
	
	public AggregatedState(StateDescriptor desciptor){

		for(StateVariable variable : desciptor){
			put(variable, 0);
		}
		
	}
	
	public String toString(){
		
	}
	
	
	
	
}