package data.aggregatedModel;

import java.util.ArrayList;
import java.util.Iterator;
import data.model.Group;
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

	public double getRateOf(AggregatedAction action, StateDescriptor descriptor , ArrayList<Group> allGroups){
		double rate = Double.MAX_VALUE;
		
		ArrayList<Group> enablingGroups = action.getEnablingGroups(allGroups);
		
		double rateAtGroup;
		for (Group group : enablingGroups){
			rateAtGroup = group.getRateOf(this, descriptor, action);
			rate = minimum (rate,rateAtGroup);			
		}
	
		return rate;
	}
	
	
	
	public double minimum(double a , double b ){
		if (a <= b) return a ;
		if (b < a) return b ;
		return 0;
	}
	
	public boolean doesEnable(AggregatedAction action){
		
		boolean isEnabled = true;
		
		StateVariable variable;
		Integer currentPopulation; 
		Integer requiredPopulation; 
		Iterator<StateVariable> iter = action.getJumpVectorMinus().keySet().iterator();
		
		while(	iter.hasNext() & isEnabled==true	){
			
			variable = iter.next();
			currentPopulation = this.get(variable);
			requiredPopulation = action.getJumpVectorMinus().get(variable);
			
			if(currentPopulation < requiredPopulation){
				isEnabled = false;
			}
			
		}
		
		return isEnabled;

	}

	@Override
	public boolean equals (Object o) {
		
		// casting the object into an aggregated state.
		AggregatedState fstate = (AggregatedState) o;
		
	//	if (	!	(	this.stateIdentifier.equals(fstate.stateIdentifier)		)		){
	//		return false;
	//	}
		
		StateVariable variable;
		Integer populationHere;
		Integer populationInFState;
		
		Iterator<StateVariable> variables = this.keySet().iterator();

		while(variables.hasNext()){
		
			variable = variables.next();
			
			populationHere = this.get(variable);
			populationInFState = fstate.get(variable);
			
			
			
			if (  ! (populationHere.intValue() == populationInFState.intValue()	)	)
				return false;
			
		}
		
		return true;
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
