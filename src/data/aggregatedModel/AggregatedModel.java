package data.aggregatedModel;

import java.util.ArrayList;

import data.model.StateDescriptor;
import data.model.Group; 
import data.model.StateVariable;

public class AggregatedModel{

	private StateDescriptor stateDescriptor; 
	private AggregatedState initialState;
	
	private ArrayList<AggregatedAction> actions; 
	private ArrayList<Group> groups; 	
	
	private Display display;
	
	
	public Display getDisplay (){
		return this.display;
	}
	
	public void setDisplay(Display display){
		this.display = display;
	}
	
	public AggregatedModel(StateDescriptor stateDescriptor, 
							AggregatedState initialState,
							ArrayList<AggregatedAction> actions,
							ArrayList<Group> groups)	 {
		this.stateDescriptor = stateDescriptor;
		this.initialState = initialState;
		this.actions = actions;
		this.groups = groups;
		
		// initialising the model displayer.
		display = new Display(this);
	}
	
	public AggregatedModel(){
		
	}

	
	/*
	 * 
	 * 
	 * 
	 * set and get methods
	 * 
	 * 
	 * 
	 * 
	 */
	
	public StateDescriptor getAggStateDescriptor() {
		return stateDescriptor;
	}

	public void setAggStateDescriptor(StateDescriptor aggStateDescriptor) {
		this.stateDescriptor = aggStateDescriptor;
	}

	public ArrayList<AggregatedAction> getAggActions() {
		return actions;
	}

	public void setAggActions(ArrayList<AggregatedAction> aggActions) {
		this.actions = aggActions;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public AggregatedState getAggInitialState() {
		return initialState;
	}

	public void setAggInitialState(AggregatedState aggInitialState) {

		initialState = aggInitialState;
	}
	
	public AggregatedAction getActionByName(String actionName){
		for(AggregatedAction action : actions){
			if ((action.getName()).equals(actionName)			){
				return action;
			}
		}
		return null;
	}
	
}
