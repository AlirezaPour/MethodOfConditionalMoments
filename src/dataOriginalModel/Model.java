package dataOriginalModel;

import java.util.ArrayList;

import dataAggregatedModel.AggregatedModel;

// this class captures the notion of a 
// LSRB model where the groups are 
// partitions based on their size into
// large and small groups.

public class Model {
	
	private StateDescriptor stateDescriptor;
	private State initialState; 
	 
	
	
	// returns the vector containing the state variables.
	// state variables are used as the keys in many keyMaps.
	public StateDescriptor getStateDescriptor(){
		return this.stateDescriptor; 		
	}
	
	// return the initial state of the model
	public State getInitialState(){
		return this.initialState; 
	}
	
	// this method implements our aggregation steps 
	// constructs an instance of the aggregatedModel 
	public AggregatedModel getAggregatedModel(){
		return null;
	}
	
	public ArrayList<Action> getActionsSmallModel(){
		return null;
	}
	
	public ArrayList<Action> getActionsSmallLargeModel(){
		return null; 
	}
	
	public ArrayList<Action> getActionsLargeModel(){
		return null; 
	}
	

}
