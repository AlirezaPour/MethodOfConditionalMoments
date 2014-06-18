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

public class OriginalModel extends data.model.Model{
	
	private StateDescriptor stateDescriptor;
	private StateDescriptor stateDescriptorSmallGroups;
	private StateDescriptor stateDescriptorLargeGroups; 
	
	private State initialState;
	
	private ArrayList<OriginalAction> actions;
	private ArrayList<OriginalAction> actionsSmall;
	private ArrayList<OriginalAction> actionsSmallAndLarge;
	private ArrayList<OriginalAction> actionsLarge;
	
	private ArrayList<Group> largeGroups; 
	private ArrayList<Group> smallGroups; 
	
	public OriginalModel(){
		// a dummy constructor now.
		 stateDescriptor = new StateDescriptor();
		 stateDescriptorSmallGroups   = new StateDescriptor();
		 stateDescriptorLargeGroups = new StateDescriptor(); 
		
		 initialState = new State();
		
		 actions = new ArrayList<OriginalAction>();
		 actionsSmall = new ArrayList<OriginalAction>();
		 actionsSmallAndLarge = new ArrayList<OriginalAction>();
		 actionsLarge = new ArrayList<OriginalAction>();
		
		 largeGroups = new ArrayList<Group>(); 
		 smallGroups = new ArrayList<Group>(); 
	}
	
	// returns the list of the state variables which 
	// enable the action type given.
	public ArrayList<StateVariable> getEnablingStateVariables(OriginalAction action){
		return null;
	}
	
	// this method implements our aggregation steps 
	// constructs an instance of the aggregatedModel from this model. 
	public AggregatedModel consTructAggregatedModel(){
		
		Iterator<StateVariable> iter = stateDescriptor.iterator();
		StateVariable variable ;
		Group group;
		Integer population;
		
		// Variables to be found.
		StateDescriptor aggStateDescriptor = new StateDescriptor();
		State aggInitialState = new State();
		
		// derive the state descriptor
		while(iter.hasNext()){
			variable = iter.next();
			group = variable.getGroup();
			if (smallGroups.contains(group)){
				aggStateDescriptor.add(variable);
			}
		}
		
		// derive the initial aggregated state
		iter = initialState.keySet().iterator();
		while(iter.hasNext()){
			variable = iter.next();
			group= variable.getGroup();
			if(smallGroups.contains(group)){
				population = initialState.get(variable);
				aggInitialState.put(variable, population);
			}
		}

	
		
		// derive the actions related to the aggregated model. 
		ArrayList<OriginalAction> actionsSmallUnionSmallLarge = new ArrayList<>() ;
		actionsSmallUnionSmallLarge.addAll(actionsSmall);
		actionsSmallUnionSmallLarge.addAll(actionsSmallAndLarge);
		ArrayList<AggregatedAction> aggregatedVersions = aggregateActions(actionsSmallUnionSmallLarge);

		
		// construct the aggregated model. 
		data.aggregatedModel.Model aggModel = new data.aggregatedModel.Model();
		aggModel.setStateDescriptor(aggStateDescriptor);
		aggModel.setInitialState(aggInitialState);
		
		return null;
		
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
	

}
