package data.model;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedState;

public class Group {

	private String name;
	private ArrayList<LocalDerivative> localDerivatives;
	
	public Group(String name,ArrayList<LocalDerivative> derivative){
		this.name = name; 
		this.localDerivatives = derivative;
	}

	public double getRateOf(AggregatedState state, StateDescriptor descriptor, AggregatedAction action){
		
		double rate ;
		
		if (	!(getActions().contains(action))		){
			return 0;
		}
		
		LocalDerivative enablingDerivative = getEnablingLocalDerivative(action);
		double localRate = enablingDerivative.getRateOf(action);
		
		StateVariable variable = descriptor.getCorrespondingStateVariable(this, enablingDerivative);
		
		int population = state.get(variable).intValue();
		
		rate = population * localRate;
		
		return rate;
	}
	
	public String getSymbolicRateOf(StateDescriptor descriptor, AggregatedAction action){
		String output = "";
		
		if (	!(getActions().contains(action))		){
			return "Not Enabled";
		}
		
		LocalDerivative enablingDerivative = getEnablingLocalDerivative(action);
		String localRate = enablingDerivative.getSymbolicRateOf(action);
		
		StateVariable variable = descriptor.getCorrespondingStateVariable(this, enablingDerivative);
		String varName = variable.toString();
		
		output = localRate + " x " + varName ;
		
		return output; 
	}
	
	public LocalDerivative getEnablingLocalDerivative (AggregatedAction action){
		
		for (LocalDerivative derivative : localDerivatives){
			if (derivative.getActions().contains(action)) return derivative;
		}
		
		return null;
	}
	
	public ArrayList<AggregatedAction> getActions(){
		ArrayList<AggregatedAction> actions = new ArrayList<AggregatedAction>();
		ArrayList<AggregatedAction> actionsByDerivative ;
		for (LocalDerivative derivative : localDerivatives){
			actionsByDerivative = derivative.getActions();
			actions.addAll(actionsByDerivative);
		}
		return actions;
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
