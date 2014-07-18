package aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.general.Group;
import data.general.JumpVector;
import data.general.LocalDerivative;
import data.general.StateDescriptor;
import data.general.StateVariable;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;
import data.originalModel.OriginalState;

// this class is responsible for implementing the process of aggregation. 
// the input is an original model and the output is an aggregated model. 

public class Aggregation {

	private OriginalModel originalModel;
	private AggregatedModel aggregatedModel;
	
	public Aggregation(OriginalModel model){
		this.originalModel = model;
	}

	public AggregatedModel runAggregationAlgorithm(){
		
		AggregatedModel aggModel = new AggregatedModel();		
		
		HashMap<String, Integer> constants = constructConstants(originalModel);
		aggModel.setConstants(constants);
		
		ArrayList<Group> groups = constructGroups(originalModel);
		aggModel.setGroups(groups);
		
		ArrayList<LocalDerivative> localDerivatives = constructNewLocalDerivatives(groups);
		aggModel.setLocalDerivatives(localDerivatives);
		
		StateDescriptor descriptor = constructStateDescriptor(originalModel);
		aggModel.setAggStateDescriptor(descriptor);
		
		AggregatedState state = deriveAggregatedInitialState(descriptor);
		aggModel.setAggInitialState(state);
		
		ArrayList<OriginalAction> actions = originalModel.getActions();
		ArrayList<AggregatedAction> aggActions = aggregateActions(descriptor,actions);
		aggModel.setAggActions(aggActions);
		
		return aggModel;
		
	}
	
	
	
	public AggregatedState deriveAggregatedInitialState(StateDescriptor stateDescriptor){
		
		AggregatedState aggInitialState = new AggregatedState(stateDescriptor);
		
		Iterator<StateVariable> iter = stateDescriptor.iterator();
		
		StateVariable variable ;
		Integer population;
		
		while(iter.hasNext()){
			
			variable = iter.next();
			population = originalModel.getInitialState().get(variable);
			
			aggInitialState.put(variable, population);
			
		}

		return aggInitialState;
	}
	
	
	public StateDescriptor constructStateDescriptor(OriginalModel model){
		
		StateDescriptor descriptor = new StateDescriptor();
		
		ArrayList<Group> groups = constructGroups(originalModel);
		
		for (Group group : groups){
			descriptor.addAll(constructStateDescriptor(originalModel,group));
		}
		
		return descriptor;
		
	}
	
	public StateDescriptor constructStateDescriptor(OriginalModel model, Group group){
		StateDescriptor descriptor = new StateDescriptor();
		
		ArrayList<LocalDerivative> derivatives = group.getGroupLocalDerivatives();
		
		StateVariable var; 
		for (LocalDerivative ld : derivatives){
			var = new StateVariable(group, ld);
			descriptor.add(var);
		}
		
		return descriptor;
	}
	
	public ArrayList<Group> constructGroups(OriginalModel originalModel){
		
		ArrayList<Group> groups = new ArrayList<Group>();
	
		Group newGroup ;
		ArrayList<LocalDerivative> localDerivatives;
		String name ;
		
		for(Group group : originalModel.getSmallGroups()){
			
			localDerivatives = constructNewLocalDerivativesForGroup(group);
			name = group.getName();
			newGroup = new Group(name, localDerivatives);
			groups.add(newGroup);
		}
		
		return groups;
		
	}
	
	public ArrayList<LocalDerivative> constructNewLocalDerivatives(ArrayList<Group> groups){
		ArrayList<LocalDerivative> newDerivatives = new ArrayList<LocalDerivative>();
		
		for (Group group : groups){
			newDerivatives.addAll(constructNewLocalDerivativesForGroup(group));
		}
		
		return newDerivatives;
	}
	
	public ArrayList<LocalDerivative> constructNewLocalDerivativesForGroup(Group group){
		
		ArrayList<LocalDerivative> newDerivatives = new ArrayList<LocalDerivative>();
		ArrayList<LocalDerivative> oldDerivatives = group.getGroupLocalDerivatives();
	
		LocalDerivative newDerivative;
		String name ;
		
		for (LocalDerivative oldDerivative: oldDerivatives){
			name = oldDerivative.getName();
			newDerivative = new LocalDerivative(name);
			newDerivatives.add(newDerivative);
		}
		
		return newDerivatives;
		
	}
	
	public AggregatedModel consTructAggregatedModel(){
		
		ArrayList<Group> groups = constructGroups(originalModel);
		StateDescriptor descriptor = constructStateDescriptor(originalModel);
		
		// derive initial aggregated state.
		AggregatedState aggInitialState = deriveAggregatedInitialState(descriptor);;

		
		// derive the actions related to the aggregated model. 
		ArrayList<OriginalAction> actionsSmallUnionSmallLarge = new ArrayList<OriginalAction>() ;
		actionsSmallUnionSmallLarge.addAll(originalModel.getActionsSmall());
		actionsSmallUnionSmallLarge.addAll(originalModel.getActionsSmallAndLarge());
		ArrayList<AggregatedAction> aggregatedActions = aggregateActions(descriptor,actionsSmallUnionSmallLarge);
		
		// construct the aggregated model. 
		//AggregatedModel aggModel = new AggregatedModel(stateDescriptorSmallGroups,aggInitialState,aggregatedActions,smallGroups);
		
		//return aggModel;
		
		return null;
		
	}
	
	 
	
	
	public ArrayList<AggregatedAction> aggregateActions(StateDescriptor descriptor, ArrayList<OriginalAction> actions){
		
		ArrayList<AggregatedAction> aggregatedActions = new ArrayList<>();
		
		Iterator<OriginalAction> iter = actions.iterator();

		OriginalAction action;
		AggregatedAction aggregatedAction; 
		
		while(iter.hasNext()){
			action = iter.next();
			aggregatedAction = aggregateAction(descriptor,action);
			aggregatedActions.add(aggregatedAction);
		}

		return aggregatedActions;

	}
	
	public AggregatedAction aggregateAction(StateDescriptor descriptor, OriginalAction action){
		
		AggregatedAction aggAction = new AggregatedAction();
		
		aggAction.setName(action.getName());
		
		JumpVector aggJumpVector = new JumpVector();
		JumpVector aggJumpVectorMinus = new JumpVector();
		JumpVector aggJumpVectorPlus = new JumpVector();
		
		
		Iterator<StateVariable> iter = descriptor.iterator();
		
		StateVariable variable;
		Integer impact;
		Integer impactPlus;
		Integer impactMinus;
		
		while(iter.hasNext()){
			
			variable = iter.next();
			
			impact = action.getImpactOn(variable);
			impactMinus = action.getImpactMinusOn(variable);
			impactPlus = action.getImpactPlusOn(variable);
				
			aggJumpVector.put(variable, impact);
			aggJumpVectorMinus.put(variable, impactMinus);
			aggJumpVectorPlus.put(variable, impactPlus);
			
		}
		
		aggAction.setJumpVector(aggJumpVector);
		aggAction.setJumpVectorMinus(aggJumpVectorMinus);
		aggAction.setJumpVectorPlus(aggJumpVectorPlus);
		
		// for this action, the aggregated action was constructed.
		// update the local derivatives that the action is enabled at.
		ArrayList<LocalDerivative> originalDerivatives = originalModel.getEnablingLocalDerivative(action);
		 
		LocalDerivative aggLocalDerivative;
		Double rate;
		String paramter;
		
		for (LocalDerivative originalDerivative : originalDerivatives){
		
			rate = originalDerivative.getActionRates().get(action);
			paramter = originalDerivative.getParameterNames().get(action);
			
			String name = originalDerivative.getName();
			aggLocalDerivative = aggregatedModel.findLocalDerivativeByName(name);
			
			aggLocalDerivative.getActionRates().put(aggAction, rate);
			aggLocalDerivative.getParameterNames().put(aggAction, paramter);
			
		}
		
		return aggAction;
	}
	
	public HashMap<String, Integer> constructConstants(OriginalModel originalModel){
		HashMap<String, Integer> constants = originalModel.getConstants();
		return constants;
	}
	
	
}
