package data.originalModel;


import java.util.ArrayList;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedState;
import data.general.Transition;
import data.general.Group;
import data.general.State;
import data.general.StateDescriptor;
import data.general.StateVariable;


public class OriginalState extends State {

	private String stateIdentifier;
	private int stateId;
	
	private ArrayList<OriginalState> incomingStates ; 
	private ArrayList<OriginalState> reachableStates;
	
	private ArrayList<Transition> inwardTransitions ;
	
	public OriginalState(StateDescriptor desciptor){

		for(StateVariable variable : desciptor){
			put(variable, 0);
		}
		
		incomingStates = new ArrayList<OriginalState>();
		reachableStates = new ArrayList<OriginalState>();
		
		inwardTransitions = new ArrayList<Transition>();
		
	}
	
	public OriginalState() {
		incomingStates = new ArrayList<OriginalState>();
		reachableStates = new ArrayList<OriginalState>();
		inwardTransitions = new ArrayList<Transition>();
		
	}
	
	public ArrayList<Transition> getInwardTransitions(){
		return this.inwardTransitions;
	}
	
	public void setInwardTransitions(ArrayList<Transition> transitions){
		this.inwardTransitions = transitions;
	}
	
	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public ArrayList<OriginalState> getIncomingStates() {
		return incomingStates;
	}

	public void setIncomingStates(ArrayList<OriginalState> incomingStates) {
		this.incomingStates = incomingStates;
	}

	public ArrayList<OriginalState> getReachableStates() {
		return reachableStates;
	}

	public void setReachableStates(ArrayList<OriginalState> reachableStates) {
		this.reachableStates = reachableStates;
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

}
