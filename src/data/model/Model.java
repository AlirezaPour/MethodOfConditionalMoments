package data.model;

import java.util.ArrayList;


public class Model {

	protected StateDescriptor stateDescriptor;
	protected State initialState;
	protected ArrayList<Group> groups;
	protected ArrayList<Action> actions;
	
	public StateDescriptor getStateDescriptor() {
		return stateDescriptor;
	}
	public void setStateDescriptor(StateDescriptor stateDescriptor) {
		this.stateDescriptor = stateDescriptor;
	}
	public State getInitialState() {
		return initialState;
	}
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}
	public ArrayList<Group> getGroups() {
		return groups;
	}
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}
	public ArrayList<Action> getActions() {
		return actions;
	}
	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
	
	
}
