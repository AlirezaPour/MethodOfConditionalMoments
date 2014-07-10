package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.AggregatedStateSpace;
import data.aggregatedModel.Explorer;

public class TestSaveStateSpace {

	
	public static void main(String[] args) {

		
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		
		Explorer explorer = new Explorer(model);
		
		AggregatedStateSpace sp = explorer.generateStateSpaceCompleteVersion();
		
		String output = model.getDisplay().storeStateSpace(sp);
		
		System.out.printf(output);
	}

}
