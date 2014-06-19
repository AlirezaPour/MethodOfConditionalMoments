package data.aggregatedModel;

import java.util.ArrayList;

import data.model.StateDescriptor;
import data.model.State;
import data.model.Group; 

public class AggregatedModel{

	private StateDescriptor aggStateDescriptor; 
	
	private State AggInitialState;
	
	private ArrayList<AggregatedAction> aggActions; 
	private ArrayList<AggregatedAction> aggActionsSmall; 
	private ArrayList<AggregatedAction> aggActionsSmallLarge; 
	
	private ArrayList<Group> groups; 

	public AggregatedModel(){
		
	}

	public StateDescriptor getAggStateDescriptor() {
		return aggStateDescriptor;
	}

	public void setAggStateDescriptor(StateDescriptor aggStateDescriptor) {
		this.aggStateDescriptor = aggStateDescriptor;
	}

	
	public ArrayList<AggregatedAction> getAggActions() {
		return aggActions;
	}

	public void setAggActions(ArrayList<AggregatedAction> aggActions) {
		this.aggActions = aggActions;
	}

	public ArrayList<AggregatedAction> getAggActionsSmall() {
		return aggActionsSmall;
	}

	public void setAggActionsSmall(ArrayList<AggregatedAction> aggActionsSmall) {
		this.aggActionsSmall = aggActionsSmall;
	}

	public ArrayList<AggregatedAction> getAggActionsSmallLarge() {
		return aggActionsSmallLarge;
	}

	public void setAggActionsSmallLarge(
			ArrayList<AggregatedAction> aggActionsSmallLarge) {
		this.aggActionsSmallLarge = aggActionsSmallLarge;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public State getAggInitialState() {
		return AggInitialState;
	}

	public void setAggInitialState(State aggInitialState) {
		AggInitialState = aggInitialState;
	}
	
	
	
}
