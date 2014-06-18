package data.aggregatedModel;

import java.util.ArrayList;

import data.model.StateDescriptor;
import data.model.State;
import data.model.Group; 

public class AggregatedModel extends data.model.Model{

	private StateDescriptor stateDescriptor; 
	private State initialState;
	
	private ArrayList<AggregatedAction> actions; 
	private ArrayList<AggregatedAction> actionsSmall; 
	private ArrayList<AggregatedAction> actionsSmallLarge; 
	
	private ArrayList<Group> groups; 

	public AggregatedModel(){
		
	}

	public ArrayList<AggregatedAction> getActionsSmall() {
		return actionsSmall;
	}

	public void setActionsSmall(ArrayList<AggregatedAction> actionsSmall) {
		this.actionsSmall = actionsSmall;
	}

	public ArrayList<AggregatedAction> getActionsSmallLarge() {
		return actionsSmallLarge;
	}

	public void setActionsSmallLarge(ArrayList<AggregatedAction> actionsSmallLarge) {
		this.actionsSmallLarge = actionsSmallLarge;
	}
	
	
	
	
}
