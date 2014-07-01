package data.aggregatedModel;

import java.util.ArrayList;
import java.util.HashMap;

import data.model.StateDescriptor;

public class AggregatedStateSpace {
	
	private ArrayList<AggregatedState> explored; 
	public HashMap<AggregatedState, ArrayList<Transition>> getTransitionBank() {
		return transitionBank;
	}

	public void setTransitionBank(
			HashMap<AggregatedState, ArrayList<Transition>> transitionBank) {
		this.transitionBank = transitionBank;
	}


	private HashMap<AggregatedState, ArrayList<Transition>> transitionBank;
	
	public AggregatedStateSpace(){
		explored = new ArrayList<AggregatedState>();
		transitionBank = new HashMap<AggregatedState, ArrayList<Transition>>();
	}

	public ArrayList<AggregatedState> getExplored() {
		return explored;
	}

	public void setExplored(ArrayList<AggregatedState> states) {
		this.explored = states;
	}

	public Transition unifromiseTransition(Transition transition){
		
		// analyse the transition and see if the target has already been explored.
		
		return transition;
	}
	
	
	public boolean isExplored(AggregatedState state){
		if (explored.contains(state)) return true;
		if (!(explored.contains(state))) return false;
		return true;
	}
	
	public void addToExplored(AggregatedState st){
		explored.add(st);
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		transitionBank.put(st, transitions);
	}

	public void addToTransitionBank(AggregatedState state, Transition tr) {
		transitionBank.get(state).add(tr);
	}
	
}
