package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.Explorer;
import data.general.Transition;
import data.general.Group;
import data.general.LocalDerivative;
import data.general.StateVariable;

public class TestShowTransitions {

	public static void main(String args[]){
		
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		
		Explorer explorer = new Explorer(model);
		
		AggregatedState state = getArbitraryState(model);
		AggregatedAction action = getArbitraryAction(model, "log");
		
		ArrayList<Transition> transitions = explorer.getTransitions(state);
		String output = model.getDisplay().showTransitions(transitions);
		
		System.out.printf(output);
		
	}
	
	
	public static AggregatedState getArbitraryState(AggregatedModel model){
		
		AggregatedState state = new AggregatedState();
		
		StateVariable Si = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_idle"));
		state.put(Si, 1);
		
		StateVariable Sl = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_log"));
		state.put(Sl, 1);
		
		StateVariable Sb = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_brk"));
		state.put(Sb, 9);
		
		return state;
		
	}
	
	public static AggregatedAction getArbitraryAction(AggregatedModel model, String actionName){
		AggregatedAction action;
		action = model.getActionByName(actionName);
		return action;
	}
	
	public static Group getArbitraryGroupByName(AggregatedModel model, String fname){
		
		for (Group group : model.getGroups()){
			String name = group.getName(); 
			if (name.equals(fname)) return group;
		}
		return null;
	}
	
}
