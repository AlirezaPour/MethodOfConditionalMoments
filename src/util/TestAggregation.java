package util;

import aggregation.Aggregation;
import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Explorer;
import data.originalModel.Display;
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
		
		

	}

}
