package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.Display;
import data.aggregatedModel.Explorer;
import data.model.Group;
import data.model.LocalDerivative;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class TestExplorer {

	
	/*public static void main(String[] args) {

		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		ArrayList<AggregatedState> states = explorer.generateStateSpace();
		
		
		String output = "";
		for( AggregatedState state : states){
			
			output += model.getDisplay().showNumericalRepresentation(state);
			output += " : ";
			
			for (AggregatedState incomingState : state.getIncomingStates()){
				
				output += model.getDisplay().showNumericalRepresentation(incomingState);
				output += " , ";
				
			}
			
			output += "\n\n";
			
		}
		
		System.out.printf(output);
		
		System.out.printf("\n\n");
		
		//AggregatedState state = getArbitraryState(model);
		
		
		// test isEnabledAt 
		//System.out.printf(model.getDisplay().showState(state));
		//ArrayList<AggregatedAction> actions = explorer.actionsEnabledAt(state);
		//System.out.printf("\n\n\n");
		//System.out.printf(model.getDisplay().showActions(actions));
		
		// test nextState
		//AggregatedAction action = getArbitraryAction(model, "fix");
		
		//AggregatedState nextState = explorer.nextState(state, action);
		//System.out.printf(model.getDisplay().showState(nextState));

		// test nextStates
		//ArrayList<AggregatedState> nextStates = explorer.nextStates(getArbitraryState(model));
		//System.out.printf(model.getDisplay().showStates(nextStates));
		
		// test generateStates
		ArrayList<AggregatedState> allStates = explorer.generateStates();
		System.out.printf(model.getDisplay().showStates(allStates));
		
	}
	
	public static AggregatedState getArbitraryState(AggregatedModel model){
	
		AggregatedState state = new AggregatedState();
		
		StateVariable Si = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_idle"));
		state.put(Si, 0);
		
		StateVariable Sl = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_log"));
		state.put(Sl, 1);
		
		StateVariable Sb = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_brk"));
		state.put(Sb, 1);
		
		return state;
		
	}
	
	public static AggregatedAction getArbitraryAction(AggregatedModel model, String actionName){
		AggregatedAction action;
		action = model.getActionByName(actionName);
		return action;
	}
	
	*/
	
	public static void main (String args[]){
		boolean output = checkStateSpaceGeneration2();
		System.out.printf(Boolean.toString(output));
	}
	
	public static boolean checkStateSpaceGeneration(){
		
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		
		boolean output = true;
		
		ArrayList<AggregatedState> states = explorer.generateStateSpace();
		
		for (AggregatedState state : states){
			
			// we get the current state.
			ArrayList<AggregatedState> nextStates = state.getReachableStates();
			
			for (AggregatedState nextState : nextStates){
				
				ArrayList<AggregatedState> incomingStates = nextState.getIncomingStates(); 
				
				if (	!	(	incomingStates.contains(state)	)	){
					return false; 
				}
				
			}
			
		}
		
		return true; 
	}

	public static boolean checkStateSpaceGeneration2(){
		
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		
		Explorer explorer = new Explorer(model);
		
		boolean output = true;
		
		ArrayList<AggregatedState> states = explorer.generateStateSpace();
		
		for (AggregatedState state : states){
			
			// we get the current state.
			ArrayList<AggregatedState> incomingStates = state.getIncomingStates();
			
			for (AggregatedState incomingState : incomingStates){
				
				ArrayList<AggregatedState> reachableStates = incomingState.getReachableStates(); 
				
				if (	!	(	reachableStates.contains(state)	)	){
					return false; 
				}
				
			}
			
		}
		
		
		return true;

		
	}
	
	
}
