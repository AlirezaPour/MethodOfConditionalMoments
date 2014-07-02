package matlab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import util.ClientServerAggregatedModel;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Explorer;

public class MarginalDistribution {
	
	
	
	AggregatedModel model;
	AggregatedStateSpace stateSpace;
	
	public MarginalDistribution(AggregatedModel model, AggregatedStateSpace sp){
		this.model = model;
		this.stateSpace = sp;
	}
	
	
	public String constructStatesMaps(){
		String output = "";
		
		ArrayList<AggregatedState> states = stateSpace.getExplored();
		
		for (AggregatedState state : states){
			
		}
		
		return output; 
	}
	
	public String constructMapForOneState(AggregatedState state){
		String output = "st";
		
		int id = state.getStateId();
		output += Integer.toString(id);
		
		return output;
	}
	
	public String setParamters (){
		
		String output = "";
		
		HashMap<String, Integer> constants = model.getConstants();
		
		Iterator<String> names = constants.keySet().iterator();
		
		String name; 
		int value;
		
		while(names.hasNext()){
			name = names.next();
			value = constants.get(name).intValue();
			output += name + " = " +  Integer.toString(value) + " ;\n";
		}
		
		return output;
		
	}

	
	
	public static void main(String args[]){
		
		// testing set parameter;
		
		AggregatedModel model  = ClientServerAggregatedModel.getAggregatedClientServerModel();
		Explorer explorer = new Explorer(model);
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();
		MarginalDistribution md = new MarginalDistribution(model, sp);
		String output = md.setParamters();
		System.out.printf(output);
		
		
		
	}
	
	
}
