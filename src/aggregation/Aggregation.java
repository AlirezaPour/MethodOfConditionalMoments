package aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.general.Action;
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
	private AggregatedModel aggModel;
	
	public Aggregation(OriginalModel model){
		this.originalModel = model;
	}

	public AggregatedModel runAggregationAlgorithm(){
			
		aggModel = new AggregatedModel();		
		
		HashMap<String, Double> constants = constructConstants(originalModel);
		aggModel.setConstants(constants);
		
		ArrayList<Group> groups = constructGroups(originalModel);
		aggModel.setGroups(groups);
		
		ArrayList<LocalDerivative> localDerivatives = constructNewLocalDerivatives(groups);
		aggModel.setLocalDerivatives(localDerivatives);
		
		StateDescriptor descriptor = constructStateDescriptor(originalModel,groups);
		aggModel.setAggStateDescriptor(descriptor);
		
		AggregatedState state = deriveAggregatedInitialState(descriptor);
		aggModel.setAggInitialState(state);
		
		
		ArrayList<OriginalAction> actionsSmallUnionSmallLarge = new ArrayList<OriginalAction>() ;
		actionsSmallUnionSmallLarge.addAll(originalModel.getActionsSmall());
		actionsSmallUnionSmallLarge.addAll(originalModel.getActionsSmallAndLarge());		
		ArrayList<AggregatedAction> aggActions = aggregateActions(descriptor,actionsSmallUnionSmallLarge);
		aggModel.setAggActions(aggActions);
		
		// updating the display of the aggregated model so that it has access to the new groups, actions, etc.
		aggModel.getDisplay().updateDisplayPrimitives();
		
		return aggModel;
		
	}
	
	
	
	public AggregatedState deriveAggregatedInitialState(StateDescriptor stateDescriptor){
		
		AggregatedState aggInitialState = new AggregatedState(stateDescriptor);
		
		Iterator<StateVariable> iter = stateDescriptor.iterator();
		
		StateVariable variable ;
		StateVariable originalVariable;
		LocalDerivative ld;
		Group group;
		Integer population;
		
		while(iter.hasNext()){
			
			variable = iter.next();
			ld = variable.getLocalDerivative();
			group = variable.getGroup();
			originalVariable = originalModel.getStateDescriptor().getCorrespondingStateVariable(group, ld);
			
			OriginalState originalInitialState = originalModel.getInitialState();
			population = originalInitialState.get(originalVariable);
			
			aggInitialState.put(variable, population);
			
		}

		return aggInitialState;
	}
	
	
	public StateDescriptor constructStateDescriptor(OriginalModel model, ArrayList<Group> groups){
		
		StateDescriptor descriptor = new StateDescriptor();

		
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
	
	public ArrayList<LocalDerivative> constructNewLocalDerivativesForGroup(Group group){
		
		ArrayList<LocalDerivative> newDerivatives = new ArrayList<LocalDerivative>();
		ArrayList<LocalDerivative> oldDerivatives = group.getGroupLocalDerivatives();
	
		LocalDerivative newDerivative;
		String name ;
		
		for (LocalDerivative oldDerivative: oldDerivatives){
			name = oldDerivative.getName();
			newDerivative = new LocalDerivative(name);
			
			HashMap<Action, Double> actionRates = new HashMap<Action, Double>();
			HashMap<Action, String> actionParamters = new HashMap<Action, String>();
			
			newDerivative.setActionRates(actionRates);
			newDerivative.setParameterNames(actionParamters);
			
			newDerivatives.add(newDerivative);
		}
		
		return newDerivatives;
		
	}
	
	public ArrayList<LocalDerivative> constructNewLocalDerivatives(ArrayList<Group> groups){
		ArrayList<LocalDerivative> newDerivatives = new ArrayList<LocalDerivative>();
		
		for (Group group : groups){
			newDerivatives.addAll(group.getGroupLocalDerivatives());
		}
		
		return newDerivatives;
	}
	
	public ArrayList<AggregatedAction> aggregateActions(StateDescriptor descriptor, ArrayList<OriginalAction> actions){
		
		ArrayList<AggregatedAction> aggregatedActions = new ArrayList<AggregatedAction>();
		
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
		
		// linking the original action and its aggregated form together.
		aggAction.setOriginalAction(action);
		action.setAggregatedVersion(aggAction);
		
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
			
			// given the variable in the aggregated state descriptor, whats the variable in the 
			// original state descriptor.
			StateVariable originalVariable = unformiseVariable(variable);
			
			impact = action.getImpactOn(originalVariable);
			impactMinus = action.getImpactMinusOn(originalVariable);
			impactPlus = action.getImpactPlusOn(originalVariable);
				
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
		
		// find the intersection of all local derivatives with the ones in the aggregated model.
		originalDerivatives = uniformiseLocalDerivatives(originalDerivatives,aggModel.getLocalDerivatives());
		
		
		
		LocalDerivative aggLocalDerivative;
		Double rate;
		String paramter;
		
		for (LocalDerivative originalDerivative : originalDerivatives){
		
			rate = originalDerivative.getActionRates().get(action);
			paramter = originalDerivative.getParameterNames().get(action);
			
			String name = originalDerivative.getName();
			aggLocalDerivative = aggModel.findLocalDerivativeByName(name);
			
			aggLocalDerivative.getActionRates().put(aggAction, rate);
			aggLocalDerivative.getParameterNames().put(aggAction, paramter);
			
		}
		
		return aggAction;
	}
	
	// given a variable in the state descriptor of the aggregated model, 
	// what is the equivalent state in the desciptor of the original model. 
	public StateVariable unformiseVariable (StateVariable var){
		LocalDerivative ld = var.getLocalDerivative();
		Group group = var.getGroup();
		
		StateVariable originalVar = originalModel.getStateDescriptor().getCorrespondingStateVariable(group, ld);
		return originalVar;
	}
	
	public ArrayList<LocalDerivative> uniformiseLocalDerivatives(ArrayList<LocalDerivative> originalDerivatives, ArrayList<LocalDerivative> aggDerivatives){
		
		ArrayList<LocalDerivative> newOriginalDerivatives = new ArrayList<LocalDerivative>();
	
		for(LocalDerivative origDer : originalDerivatives){
			
			if(		aggDerivatives.contains(origDer)	){
				newOriginalDerivatives.add(origDer);
			}				
		}
		
		return newOriginalDerivatives;
		
	}
	
	public HashMap<String, Double> constructConstants(OriginalModel originalModel){
		HashMap<String, Double> constants = originalModel.getConstants();
		return constants;
	}
	
	
}
