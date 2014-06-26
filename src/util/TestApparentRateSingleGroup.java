package util;

import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.model.Group;
import data.model.LocalDerivative;
import data.model.StateVariable;

public class TestApparentRateSingleGroup {

	
	public static void main(String[] args) {
	
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		
		AggregatedState state = getArbitraryState(model);
		AggregatedAction action = getArbitraryAction(model, "fix");
		Group group = getArbitraryGroupByName(model, "Servers");
		
		double rate = state.getRateOf(action, model.getAggStateDescriptor(), model.getGroups());
		System.out.printf("%f",rate);


	}
	
	
	public static AggregatedState getArbitraryState(AggregatedModel model){
		
		AggregatedState state = new AggregatedState();
		
		StateVariable Si = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_idle"));
		state.put(Si, 10);
		
		StateVariable Sl = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_log"));
		state.put(Sl, 1);
		
		StateVariable Sb = model.getAggStateDescriptor().getCorrespondingStateVariable(new Group("Servers",null), new LocalDerivative("Server_brk"));
		state.put(Sb, 0);
		
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
