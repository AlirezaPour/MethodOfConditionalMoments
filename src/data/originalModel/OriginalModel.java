package data.originalModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
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
	
	public OriginalModel(){
		// all the state variables are initialised using the set and get methods.
	}
	
	// returns the list of the state variables which 
	// enable the action type given.
	public ArrayList<StateVariable> getEnablingStateVariables(OriginalAction action){
		return null;
	}
	
	// this method implements our aggregation steps 
	// constructs an instance of the aggregatedModel from this model. 
	public AggregatedModel consTructAggregatedModel(){
		
		// find the state descriptor.
		StateDescriptor aggStateDescriptor;
		aggStateDescriptor = deriveAggregatedStateDescriptor();
		
		// derive initial aggregated state.
		State aggInitialState;
		aggInitialState = deriveAggregatedInitialState();
		
		// derive the actions related to the aggregated model. 
		ArrayList<OriginalAction> actionsSmallUnionSmallLarge = new ArrayList<>() ;
		actionsSmallUnionSmallLarge.addAll(actionsSmall);
		actionsSmallUnionSmallLarge.addAll(actionsSmallAndLarge);
		ArrayList<AggregatedAction> aggregatedActions = aggregateActions(actionsSmallUnionSmallLarge);

		// construct the aggregated model. 
		AggregatedModel aggModel = new AggregatedModel();
		aggModel.setAggStateDescriptor(aggStateDescriptor);
		aggModel.setAggInitialState(aggInitialState);
		aggModel.setAggActions(aggregatedActions);
		
		return aggModel;
		
	}
	
	private State deriveAggregatedInitialState(){
		
		Iterator<StateVariable> iter = initialState.keySet().iterator();
		
		State aggInitialState = new State();
		
		StateVariable variable ;
		Group group;
		Integer population;
		
		
		
		while(iter.hasNext()){
			variable = iter.next();
			group= variable.getGroup();
			if(smallGroups.contains(group)){
				population = initialState.get(variable);
				aggInitialState.put(variable, population);
			}
		}

		return aggInitialState;
	}
	
	private StateDescriptor deriveAggregatedStateDescriptor(){
		
		Iterator<StateVariable> iter = stateDescriptor.iterator();
		
		StateDescriptor aggDescriptor = new StateDescriptor();
		
		StateVariable variable;
		Group group;
		
		while(iter.hasNext()){
			variable = iter.next();
			group = variable.getGroup();
			if (smallGroups.contains(group)){
				aggDescriptor.add(variable);
			}
		}
		
		return aggDescriptor;
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
	

	
	
	
}
