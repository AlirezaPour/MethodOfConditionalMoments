package data.aggregatedModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.general.Group;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;

public class AggregatedModel{

	private ArrayList<LocalDerivative> localDerivatives; 
	
	private StateDescriptor stateDescriptor; 
	private AggregatedState initialState;
	
	private ArrayList<AggregatedAction> actions; 
	private ArrayList<Group> groups; 	
	
	private Display display;
	
	private HashMap<String, Double> constants;
	
	
	public Display getDisplay (){
		return this.display;
	}
	
	public void setDisplay(Display display){
		this.display = display;
	}
	
	public AggregatedModel(StateDescriptor stateDescriptor, 
							AggregatedState initialState,
							ArrayList<AggregatedAction> actions,
							ArrayList<Group> groups,
							HashMap<String, Double> constants)	 {
		this.stateDescriptor = stateDescriptor;
		this.initialState = initialState;
		this.actions = actions;
		this.groups = groups;
		this.constants = constants;
		
		// initialising the model displayer.
		display = new Display(this);
	}
	
	public AggregatedModel(){
		display = new Display(this);
	}
	
	public HashMap<String, Double> getConstants() {
		return constants;
	}

	public void setConstants(HashMap<String, Double> constants) {
		this.constants = constants;
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

	public ArrayList<LocalDerivative> getLocalDerivatives() {
		return localDerivatives;
	}

	public void setLocalDerivatives(ArrayList<LocalDerivative> localDerivatives) {
		this.localDerivatives = localDerivatives;
	}

	public LocalDerivative findLocalDerivativeByName(String fname){
		Iterator<LocalDerivative> iter = localDerivatives.iterator();
		
		LocalDerivative derivative;
		
		while(iter.hasNext()){
			derivative = iter.next();
			
			if (derivative.getName().equals(fname)){
				return derivative;
			}
		}
		
		return null;
	}
	
}
