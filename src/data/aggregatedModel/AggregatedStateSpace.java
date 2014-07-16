package data.aggregatedModel;

import java.util.ArrayList;
import java.util.HashMap;

import data.general.Transition;
import data.general.StateDescriptor;

public class AggregatedStateSpace {
	
	private ArrayList<AggregatedState> explored; 
	
	private HashMap<AggregatedState, ArrayList<Transition>> outGoingTransitionBank;
	//private HashMap<AggregatedState, ArrayList<Transition>> incomingTransitionBank;
	

	public HashMap<AggregatedState, ArrayList<Transition>> getOutgoingTransitionBank() {
		return outGoingTransitionBank;
	}

	public void setOutgoingTransitionBank(
			HashMap<AggregatedState, ArrayList<Transition>> transitionBank) {
		this.outGoingTransitionBank = transitionBank;
	}


	

	
	
	public AggregatedStateSpace(){
		explored = new ArrayList<AggregatedState>();
		outGoingTransitionBank = new HashMap<AggregatedState, ArrayList<Transition>>();
		
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
		
		ArrayList<Transition> outgoingTransitions = new ArrayList<Transition>();
		outGoingTransitionBank.put(st, outgoingTransitions);
		
		ArrayList<Transition> incomingTransitions = new ArrayList<Transition>();
		outGoingTransitionBank.put(st, incomingTransitions);
		
	}

	public void addToOutgoingTransitionBank(AggregatedState state, Transition tr) {
		outGoingTransitionBank.get(state).add(tr);
	}
	
	
	
}
