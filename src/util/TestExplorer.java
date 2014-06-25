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

	public static void main(String[] args) {

		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		
		//AggregatedState state = getArbitraryState(model);
		
		
		// test isEnabledAt 
		//System.out.printf(model.getDisplay().showState(state));
		//ArrayList<AggregatedAction> actions = explorer.actionsEnabledAt(state);
		//System.out.printf("\n\n\n");
		//System.out.printf(model.getDisplay().showActions(actions));
		
		// test nextState
		AggregatedAction action = getArbitraryAction(model, "fix");
		
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
	

	
	
}
