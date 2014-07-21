package matlab.conditionalExpectation;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.general.StateDescriptor;
import data.general.StateVariable;
import data.general.Transition;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;

public class ConditionalExpectation {

	private OriginalModel origModel;
	private ArrayList<ODEVariable> odeVariables ;
	
	
	public Display display;
	
	public ConditionalExpectation(OriginalModel origModel,AggregatedStateSpace ssp, StateDescriptor stDesLargeGroups){
		this.origModel = origModel;
		odeVariables = constructODEVariables(ssp,stDesLargeGroups);
		display = new Display(this);
	}
	
	public int getIndexOf(ODEVariable odeVar){
		
		if (odeVar instanceof ODEVariableProbability){
			int index = (	(ODEVariableProbability) odeVar).getIndex();
			return index;
		}
		
		if(odeVar instanceof ODEVariableConditionalExpectation){
			int index = (	(ODEVariableConditionalExpectation) odeVar).getIndex();
			return index;
		}
		
		return -1;
	}
	
	public ArrayList<ODEVariable> constructODEVariables(AggregatedStateSpace ssp, StateDescriptor stDesLargeGroups){
		
		ArrayList<ODEVariable> odeVariables = new ArrayList<ODEVariable>();
		
		ArrayList<? extends ODEVariable> currentVariables;
		
		// ode variables related to the marginal distribution over the small groups.
		currentVariables = constructODEVariablesProbabilities(ssp);
		odeVariables.addAll(currentVariables);
		
		// the zero point for the indexer is exactly the size of the aggregated State space.
		int zeroPlace = odeVariables.size();
		VariableIndexer indexer = new VariableIndexer(zeroPlace);
		
		// ode variables related to the large groups
		currentVariables = constructODEVariablesForLargeGroups(ssp, stDesLargeGroups, indexer);
		odeVariables.addAll(currentVariables);
		
		return odeVariables;
	}
	
	
	public ArrayList<ODEVariableConditionalExpectation> constructODEVariablesForLargeGroups(AggregatedStateSpace ssp, StateDescriptor stDesLargeGroups, VariableIndexer indexer){
		
		ArrayList<ODEVariableConditionalExpectation> odeVariables = new ArrayList<ODEVariableConditionalExpectation>();
		
		ArrayList<AggregatedState> allAggregatedStates = ssp.getExplored();
		
		ArrayList<ODEVariableConditionalExpectation> currentOdeVariables;
		
		for (AggregatedState state : allAggregatedStates){
			
			currentOdeVariables = construOdeVariableConditionalExpectations(state, stDesLargeGroups, indexer);
			
			odeVariables.addAll(currentOdeVariables);
			
		}
			
		
		return odeVariables;
		
	}
	
	// given an aggregated state, constructs the conditional expectation variables related to the variables of the large groups.
	public ArrayList<ODEVariableConditionalExpectation> construOdeVariableConditionalExpectations(AggregatedState state,StateDescriptor stDesLargeGroups, VariableIndexer indexer){
		
		ArrayList<ODEVariableConditionalExpectation> odeVariables = new ArrayList<ODEVariableConditionalExpectation>();
		
		ODEVariableConditionalExpectation odeVar ;
		
		String varName;
		int index ;
		
		for (StateVariable variable : stDesLargeGroups){
			
			odeVar = new ODEVariableConditionalExpectation();
			
			indexer.increaseIndex();
			index = indexer.getIndex();
			odeVar.setIndex(index);
			
			varName = "st" + state.getStateId() +  "_" + variable.toString();
			odeVar.setName(varName);
			
			odeVar.setState(state);
			
			odeVar.setVariable(variable);
			
			odeVariables.add(odeVar);
		}
		
		return odeVariables;
		
	}	
	
	// constructs the ode variables for the variables 
	// related to the marginal distribution over the small groups.
	public ArrayList<ODEVariableProbability> constructODEVariablesProbabilities(AggregatedStateSpace ssp){
		
		ArrayList<ODEVariableProbability> odeVariables = new ArrayList<ODEVariableProbability>();
		
		ODEVariableProbability  odeVar ;
		int index ;
		
		String name ;
		
		for (AggregatedState state : ssp.getExplored()){
			
			odeVar = new ODEVariableProbability();
			
			index = ssp.getExplored().indexOf(state);
			index += 1;	// the indexes in the matlab file start from one and not zero. 
			odeVar.setIndex(index);
			
			name = "st" + Integer.toString(state.getStateId());
			odeVar.setName(name);
			
			odeVar.setState(state);
			
			odeVariables.add(odeVar);
			
		}
		
		return odeVariables;
		
	}
	
	
	public static void main(String[] args) {
		
		

	}

	public ArrayList<ODEVariable> getOdeVariables() {
		return odeVariables;
	}

	public void setOdeVariables(ArrayList<ODEVariable> odeVariables) {
		this.odeVariables = odeVariables;
	}
	
	
	// from all transitions into this state, filter the
	// transitions enabled by an small and large action type.
	public ArrayList<Transition> filterInwardTransitionsSmallLarge(AggregatedState state){
		
			ArrayList<Transition> allTransitions = state.getInwardTransitions();
			ArrayList<Transition> inwTrans = new ArrayList<Transition>();
			
			for (Transition trans : allTransitions){
				
				AggregatedAction aggAction = (AggregatedAction) trans.getAction();
				
				OriginalAction origAction = aggAction.getOriginalAction();
				
				if (origModel.getActionsSmallAndLarge().contains(origAction)){
					inwTrans.add(trans);
				}
				
			}
			
			return inwTrans;
			
	} 

}
