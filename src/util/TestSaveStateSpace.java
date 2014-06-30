package util;

import java.util.ArrayList;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.AggregatedState;
import data.aggregatedModel.Explorer;

public class TestSaveStateSpace {

	
	public static void main(String[] args) {

		
		AggregatedModel model = ClientServerAggregatedModel.getAggregatedClientServerModel();
		
		Explorer explorer = new Explorer(model);
		
		ArrayList<AggregatedState> states = explorer.generateStateSpace(); 

		String output = model.getDisplay().storeStateSpace(states);
		
		System.out.printf(output);
	}

}
