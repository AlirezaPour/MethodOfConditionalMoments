package util;


import java.util.ArrayList;

import matlab.conditionalExpectation.ConditionalExpectation;
import aggregation.Aggregation;
import data.aggregatedModel.AggregatedAction;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Explorer;
import data.general.StateDescriptor;
import data.general.Transition;
import data.originalModel.Display;
import data.originalModel.OriginalAction;
import data.originalModel.OriginalModel;

public class TestAggregation {

	
	public static void main(String[] args) {
	
		// construct a client server system
		OriginalModel origModel = ClientServerModel.getClientServerModel();
		
		Display originalDisplay = origModel.getDisplay();
		String output = originalDisplay.showModel();
		System.out.printf(output);
		
		
		// construct the aggregated version of the client server system
		System.out.printf("\n\n\nRunning the aggregation \n\n");
		Aggregation aggregationAlgorithm = new Aggregation(origModel);
		AggregatedModel aggModel = aggregationAlgorithm.runAggregationAlgorithm();
		System.out.printf("\n\n\nAggregation Done! \n\n");
				
		// show the aggregated model
		data.aggregatedModel.Display aggDisplay = aggModel.getDisplay();
		String output2 = aggDisplay.showModel();
		System.out.printf(output2);
		
		Explorer explorer = new Explorer(aggModel);
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();
		String output3 = aggDisplay.storeStateSpace(sp);
		System.out.printf(output3);
		
		System.out.printf("\n\n\nRunning the conditional expectation generator\n\n\n");
		
		StateDescriptor stDesLargeGroups = origModel.getStateDescriptorLargeGroups();
		ConditionalExpectation condExptGenerator = new ConditionalExpectation(origModel,sp, stDesLargeGroups);
	//	ArrayList<ODEVariable> odeVariables = condExptGenerator.getOdeVariables();
		String output4 = condExptGenerator.display.showODEVariables();
		System.out.printf(output4);
		
		String output5 = "filtered inward transitions\n\n";
		for (AggregatedState st : sp.getExplored()){
			
			output5 += "st" + st.getStateId() + "\n";
			
			ArrayList<Transition> transitions = condExptGenerator.filterInwardTransitionsSmallLarge(st);
			
			
			for (Transition tran : transitions){
				AggregatedAction aggAction = (AggregatedAction)tran.getAction();
				OriginalAction origAction = aggAction.getOriginalAction();
				output5 += origAction.getName();
				output5 += ("\n");
			}
			
			output5 += ("\n\n");
			
		}
		
		System.out.printf(output5);
	}

}
