package data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;

public class LocalDerivative {

	String name ;
	
	// this represents the action types that the local derivative enables.
	HashMap<AggregatedAction, Double> actionRates;
	
	HashMap<AggregatedAction, String > parameterNames;
	
	public HashMap<AggregatedAction, String> getParameterNames(){
		return this.parameterNames;
	}
	
	public HashMap<AggregatedAction, Double> getActionRates() {
		return actionRates;
	}

	public LocalDerivative(String name){
		this.name = name;
		actionRates = new HashMap<AggregatedAction, Double>();
		parameterNames  = new HashMap<AggregatedAction, String>();
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
	
	public ArrayList<AggregatedAction> getActions() {
		ArrayList<AggregatedAction> enabledActions = new ArrayList<AggregatedAction>();
		Iterator<AggregatedAction> iter = actionRates.keySet().iterator();
		AggregatedAction action;
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
	
	public void setActionRates(HashMap<AggregatedAction, Double> actionRates){
		this.actionRates = actionRates;
	}
	
	public double getRateOf(AggregatedAction action){
		Double constant = actionRates.get(action);
		double rate = constant.doubleValue();
		return rate;
	}
	
	public String getSymbolicRateOf(AggregatedAction action){
		//Double constant = actionRates.get(action);
		String parameterName = parameterNames.get(action);
		return parameterName;
	}
}
