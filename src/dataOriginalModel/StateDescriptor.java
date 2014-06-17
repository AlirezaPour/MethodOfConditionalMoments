package dataOriginalModel;

import java.util.ArrayList;

public class StateDescriptor {

	
	private ArrayList<StateVariable> stateDescriptor ; 
	private ArrayList<StateVariable> stateDescriptorSmallGroups; 
	private ArrayList<StateVariable> stateDescriptorLargeGroups;
	
	public ArrayList<StateVariable> getStateDescriptor() {
		return stateDescriptor;
	}
	public void setStateDescriptor(ArrayList<StateVariable> stateDescriptor) {
		this.stateDescriptor = stateDescriptor;
	}
	
	public ArrayList<StateVariable> getStateDescriptorSmallGroups() {
		return stateDescriptorSmallGroups;
	}
	public void setStateDescriptorSmallGroups(
			ArrayList<StateVariable> stateDescriptorSmallGroups) {
		this.stateDescriptorSmallGroups = stateDescriptorSmallGroups;
	}
	
	public ArrayList<StateVariable> getStateDescriptorLargeGroups() {
		return stateDescriptorLargeGroups;
	}
	public void setStateDescriptorLargeGroups(
			ArrayList<StateVariable> stateDescriptorLargeGroups) {
		this.stateDescriptorLargeGroups = stateDescriptorLargeGroups;
	}
	
	
	
	
	
}
