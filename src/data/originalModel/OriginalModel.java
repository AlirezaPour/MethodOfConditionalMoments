package data.originalModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.model.Action;
import data.model.Group;
import data.model.JumpVector;
import data.model.State;
import data.model.StateDescriptor;
import data.model.StateVariable;

// this class captures the notion of a 
// LSRB model where the groups are 
// partitions based on their size into
// large and small groups.

public class OriginalModel{
	
	// instance variables set by the parser
	
	private StateDescriptor stateDescriptor;
	private State initialState;
	private ArrayList<OriginalAction> actions;
	private ArrayList<Group> largeGroups; 
	private ArrayList<Group> smallGroups;
	
	// instance variables derived by considering the initial instance variables.
	
	private StateDescriptor stateDescriptorSmallGroups;
	private StateDescriptor stateDescriptorLargeGroups; 
	
	private ArrayList<OriginalAction> actionsSmall;
	private ArrayList<OriginalAction> actionsSmallAndLarge;
	private ArrayList<OriginalAction> actionsLarge;
	
	public OriginalModel (	StateDescriptor stateDescriptor, 
						  	State initialState,
						  	ArrayList<OriginalAction> actions, 
						  	ArrayList<Group> largeGroups,
						  	ArrayList<Group> smallGroups)   {
		
		this.stateDescriptor = stateDescriptor;
		this.initialState = initialState;
		this.actions = actions;
		this.largeGroups = largeGroups;
		this.smallGroups = smallGroups;
		
		setStateDescriptorSmallGroups();
		setStateDescriptorLargeGroups();
		setActionsInCategories();
		
	}

	// returns the list of the state variables which 
	// enable the action type given.
	public ArrayList<StateVariable> getEnablingStateVariables(OriginalAction action){
		return null;
	}
	
	// this method implements our aggregation steps 
	// constructs an instance of the aggregatedModel from this model. 
	public AggregatedModel consTructAggregatedModel(){
		
		
		// derive initial aggregated state.
		AggregatedState aggInitialState;
		aggInitialState = deriveAggregatedInitialState(stateDescriptorSmallGroups);
		
		// derive the actions related to the aggregated model. 
		ArrayList<OriginalAction> actionsSmallUnionSmallLarge = new ArrayList<>() ;
		actionsSmallUnionSmallLarge.addAll(actionsSmall);
		actionsSmallUnionSmallLarge.addAll(actionsSmallAndLarge);
		ArrayList<AggregatedAction> aggregatedActions = aggregateActions(actionsSmallUnionSmallLarge);

		// construct the aggregated model. 
		AggregatedModel aggModel = new AggregatedModel(stateDescriptorSmallGroups,aggInitialState,aggregatedActions,smallGroups);
		
		return aggModel;
		
	}
	
	private AggregatedState deriveAggregatedInitialState(StateDescriptor stateDescriptor){
		
		Iterator<StateVariable> iter = stateDescriptor.iterator();
		
		AggregatedState aggInitialState = new AggregatedState(stateDescriptor);
		
		StateVariable variable ;
		Integer population;
		
		while(iter.hasNext()){
			
			variable = iter.next();
			population = initialState.get(variable);
			
			aggInitialState.put(variable, population);
			
		}

		return aggInitialState;
	}
		 
	public ArrayList<AggregatedAction> aggregateActions(ArrayList<OriginalAction> actions){
		
		ArrayList<AggregatedAction> aggregatedActions = new ArrayList<>();
		
		Iterator<OriginalAction> iter = actions.iterator();

		OriginalAction action;
		AggregatedAction aggregatedAction; 
		
		while(iter.hasNext()){
			action = iter.next();
			aggregatedAction = aggregateAction(action);
			aggregatedActions.add(aggregatedAction);
		}

		return aggregatedActions;

	}
	
	public AggregatedAction aggregateAction(OriginalAction action){
		
		AggregatedAction aggAction = new AggregatedAction();
		aggAction.setName(action.getName());
		
		JumpVector aggJumpVector = new JumpVector();
		JumpVector aggJumpVectorMinus = new JumpVector();
		JumpVector aggJumpVectorPlus = new JumpVector();
		
		
		Iterator<StateVariable> iter = action.getJumpVector().keySet().iterator();
		
		StateVariable variable;
		Group group;
		Integer impact;
		Integer impactPlus;
		Integer impactMinus;
		
		while(iter.hasNext()){
			variable = iter.next();
			group = variable.getGroup();
			if (smallGroups.contains(group)){
				impact = action.getImpactOn(variable);
				impactMinus = action.getImpactMinusOn(variable);
				impactPlus = action.getImpactPlusOn(variable);
				
				aggJumpVector.put(variable, impact);
				aggJumpVectorMinus.put(variable, impactMinus);
				aggJumpVectorPlus.put(variable, impactPlus);
			}
		}
		
		aggAction.setJumpVector(aggJumpVector);
		aggAction.setJumpVectorMinus(aggJumpVectorMinus);
		aggAction.setJumpVectorPlus(aggJumpVectorPlus);
		
		return aggAction;
	}

		
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

	public ArrayList<OriginalAction> getActions() {
		return actions;
	}

	public void setActions(ArrayList<OriginalAction> actions) {
		this.actions = actions;
	}

	public ArrayList<Group> getLargeGroups() {
		return largeGroups;
	}

	public void setLargeGroups(ArrayList<Group> largeGroups) {
		this.largeGroups = largeGroups;
	}

	public ArrayList<Group> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(ArrayList<Group> smallGroups) {
		this.smallGroups = smallGroups;
	}
	

	private void setStateDescriptorSmallGroups(){
		StateDescriptor stateDescriptorSmallGroups = new StateDescriptor();
		
		Group group;
		
		for(StateVariable variable : stateDescriptor){
			group = variable.getGroup();
			if(smallGroups.contains(group)){
				stateDescriptorSmallGroups.add(variable);
			}
		}
		this.stateDescriptorSmallGroups = stateDescriptorSmallGroups;
	}
	
	private void setStateDescriptorLargeGroups(){
		
		StateDescriptor stateDescriptorLargeGroups = new StateDescriptor();
		
		Group group;
		
		for(StateVariable variable : stateDescriptor){
			group = variable.getGroup();
			if(largeGroups.contains(group)){
				stateDescriptorLargeGroups.add(variable);
			}
		}
		this.stateDescriptorLargeGroups = stateDescriptorLargeGroups;
		
	}
	
	// use the actions state variable and populate the state variables actionSmall, actionsSmallLarge and actionsLarge
	private void setActionsInCategories(){
		for (OriginalAction action : actions){
			actionCategory category = getActionCategory(action);
			switch (category) {
			case LARGE:
				actionsLarge.add(action);
				break;

			case SMALL:
				actionsSmall.add(action);
				break;
			case SMALLLARGE:
				actionsSmallAndLarge.add(action);
				break;
			default:
				break;
			}
		}
	}
	
	// for one action, this method returns the category where this action belongs.
	private actionCategory getActionCategory(OriginalAction action){
		
		JumpVector jumpVector = action.getJumpVectorMinus();
		
		boolean involvedSmallGroups = false ;
		boolean involvedLargeGroups = false ;
		
		StateVariable variable;
		Integer impactMinus ;
		Group group;
		
		Iterator<StateVariable> iter = jumpVector.keySet().iterator();
		
		while( iter.hasNext() & (involvedSmallGroups == false || involvedLargeGroups==false)  ){
			variable = iter.next();
			group = variable.getGroup();
			impactMinus = jumpVector.get(variable);
			if ( impactMinus ==1 & smallGroups.contains(group) ) involvedSmallGroups = true ;
			if ( impactMinus ==1 & largeGroups.contains(group) ) involvedLargeGroups = true ;		
		}
		
		if (involvedSmallGroups==true & involvedLargeGroups==true) return actionCategory.SMALLLARGE;
		if (involvedSmallGroups==false & involvedLargeGroups ==true) return actionCategory.LARGE;
		if (involvedSmallGroups == true & involvedLargeGroups== false) return actionCategory.SMALL;
		
		return null;
	}
		
	
	
}
