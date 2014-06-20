package data.aggregatedModel;

import java.util.ArrayList;

import data.model.StateDescriptor;

public class AggregatedStateSpace {
	
	ArrayList<AggregatedState> states; 
	ArrayList<Transition> transitions;
	
	public AggregatedStateSpace(){
		states = new ArrayList<AggregatedState>();
		transitions = new ArrayList<Transition>();		
	}

	public ArrayList<AggregatedState> getStates() {
		return states;
	}

	public void setStates(ArrayList<AggregatedState> states) {
		this.states = states;
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}
	
	

	
}
