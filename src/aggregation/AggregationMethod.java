package aggregation;

import java.util.ArrayList;
import java.util.Iterator;

import data.aggregatedModel.AggregatedModel;
import data.aggregatedModel.StateDescriptorAggregatedModel;
import data.model.Group;
import dataOriginalModel.Model;
import dataOriginalModel.StateDescriptor;
import dataOriginalModel.StateDescriptorModel;
import dataOriginalModel.StateVariable;

// this class contains the algorithms required for performing the aggregation step. 

public class AggregationMethod {
	
	public AggregatedModel generateAggregatedModel (Model model){
		
		// constructing a new instance of the aggregated model 
		AggregatedModel aggregatedModel = new AggregatedModel();
		
		// setting the aggregated state descriptor
		StateDescriptorAggregatedModel aggregatedStateDescriptor ; 
		aggregatedStateDescriptor = getAggregatedStateDescriptor(model,aggregatedModel);
		aggregatedModel.setStateDescriptorAggregatedModel(aggregatedStateDescriptor);
		
		// setting the initial state
		
		
		return null;
	}

	
	private State
	
	private StateDescriptorAggregatedModel getAggregatedStateDescriptor (Model model, AggregatedModel aggregatedModel){
		ArrayList<StateVariable> stateDescriptorAggregatedModel = new ArrayList<>(); 
		
		ArrayList<Group> smallGroups = model.getSmallGroups();
		
		StateDescriptorModel originalStateDescriptor = model.getStateDescriptorModel();
		Iterator<StateVariable> stateVariableIterator = originalStateDescriptor.iterator();
		
		StateVariable stateVariable;
		
		while(stateVariableIterator.hasNext()){
			stateVariable = stateVariableIterator.next(); 
			if (smallGroups.contains(stateVariable.getGroup())){
				stateDescriptorAggregatedModel.add(stateVariable);
			}
		}
		return stateDescriptorAggregatedModel;
		
		
	}
	
}
