package data.general;

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

	public double getRateOf(State state, StateDescriptor descriptor, AggregatedAction action){
		
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
	
	public String getSymbolicRateOf(StateDescriptor descriptor, Action action){
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
	
	public String getSymbolicRateOfActionForMatlab(StateDescriptor descriptor, Action action){
		String output = "";
		
		if (	!(getActions().contains(action))		){
			return "Not Enabled";
		}
		
		LocalDerivative enablingDerivative = getEnablingLocalDerivative(action);
		String localRate = enablingDerivative.getSymbolicRateOf(action);
		
		//StateVariable variable = descriptor.getCorrespondingStateVariable(this, enablingDerivative);
		//String varName = variable.toString();
		
		String varName = this.getName() + "_" + enablingDerivative.getName() ;
		
		output = localRate + " * " + "state('" + varName + "')" ;
		
		return output; 
	}
	
	public String getSymbolicRateOfActionForMatlabConditionalForm(StateDescriptor descriptor, Action action){
		String output = "";
		
		if (	!(getActions().contains(action))		){
			return "Not Enabled";
		}
		
		LocalDerivative enablingDerivative = getEnablingLocalDerivative(action);
		String localRate = enablingDerivative.getSymbolicRateOf(action);
		
		//StateVariable variable = descriptor.getCorrespondingStateVariable(this, enablingDerivative);
		//String varName = variable.toString();
		
		String varName = this.getName() + "_" + enablingDerivative.getName() ;
		
		output = localRate + " * " + "moments('" + varName + "')" ;
		
		return output; 
	}
	
	public LocalDerivative getEnablingLocalDerivative (Action action){
		
		for (LocalDerivative derivative : localDerivatives){
			if (derivative.getActions().contains(action)) return derivative;
		}
		
		return null;
	}
	
	public ArrayList<Action> getActions(){
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		ArrayList<Action> actionsByDerivative ;
		
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
