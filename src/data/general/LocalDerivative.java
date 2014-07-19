package data.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class LocalDerivative {

	String name ;
	
	// this represents the action types that the local derivative enables.
	HashMap<Action, Double> actionRates;
	
	HashMap<Action, String > parameterNames;
	
	public void setParameterNames(HashMap<Action, String> parameterNames) {
		this.parameterNames = parameterNames;
	}

	
	
	public HashMap<Action, String> getParameterNames(){
		return this.parameterNames;
	}
	
	public HashMap<Action, Double> getActionRates() {
		return actionRates;
	}

	public LocalDerivative(String name){
		this.name = name;
		actionRates = new HashMap<Action, Double>();
		parameterNames  = new HashMap<Action, String>();
	}
	
	public String toString(){
		return name;		
	}
	
	public int length(){
		return name.length();
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
	
	public ArrayList<Action> getActions() {
		ArrayList<Action> enabledActions = new ArrayList<Action>();
		Iterator<Action> iter = actionRates.keySet().iterator();
		Action action;
		while (iter.hasNext()){
			action = iter.next();
			enabledActions.add(action);
		}
		return enabledActions;
	}

	public String getName(){
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	} 
	
	public void setActionRates(HashMap<Action, Double> actionRates){
		this.actionRates = actionRates;
	}
	
	public double getRateOf(Action action){
		Double constant = actionRates.get(action);
		double rate = constant.doubleValue();
		return rate;
	}
	
	public String getSymbolicRateOf(Action action){
		//Double constant = actionRates.get(action);
		String parameterName = parameterNames.get(action);
		return parameterName;
	}
	
}
