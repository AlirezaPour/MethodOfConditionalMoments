package data.aggregatedModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.model.Group;
import data.model.JumpVector;
import data.model.Rate;
import data.model.StateDescriptor;
import data.model.StateVariable;

public class AggregatedAction {

	private String name;
	private JumpVector jumpVector;
	private JumpVector jumpVectorMinus;
	private JumpVector jumpVectorPlus ;
	private Rate rate;
	
	public boolean isEnabledAt(AggregatedState state){
		boolean isEnabled = true;
		
		StateVariable variable;
		Integer currentPopulation; 
		Integer requiredPopulation; 
		Iterator<StateVariable> iter = jumpVectorMinus.keySet().iterator();
		
		while(	iter.hasNext() & isEnabled==true	){
			
			variable = iter.next();
			currentPopulation = state.get(variable);
			requiredPopulation = jumpVectorMinus.get(variable);
			
			if(currentPopulation < requiredPopulation){
				isEnabled = false;
			}
			
		}
		
		return isEnabled;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JumpVector getJumpVector() {
		return jumpVector;
	}
	public void setJumpVector(JumpVector jumpVector) {
		this.jumpVector = jumpVector;
	}
	public JumpVector getJumpVectorMinus() {
		return jumpVectorMinus;
	}
	public void setJumpVectorMinus(JumpVector jumpVectorMinus) {
		this.jumpVectorMinus = jumpVectorMinus;
	}
	public JumpVector getJumpVectorPlus() {
		return jumpVectorPlus;
	}
	public void setJumpVectorPlus(JumpVector jumpVectorPlus) {
		this.jumpVectorPlus = jumpVectorPlus;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public ArrayList<Group> getEnablingGroups(ArrayList<Group> allGroups){
			
		ArrayList<Group> enablingGroups = new ArrayList<Group>();
		
		for(Group group : allGroups){
			if (group.getActions().contains(this)){
				enablingGroups.add(group);
			}
		}
		
		return enablingGroups;
	}
	
	
	public String getSymbolicRateOf(StateDescriptor descriptor , ArrayList<Group> allGroups){

		ArrayList<Group> enablingGroups = getEnablingGroups(allGroups);
		
		String rateExpression = "";

		Group group;
		
		int numberOfCooperatingGroups = enablingGroups.size();

		if (numberOfCooperatingGroups == 1 ){
		// the action is individual
			group = enablingGroups.get(0);
			rateExpression = group.getSymbolicRateOf(descriptor, this);
			return rateExpression;
			
		}
		if (numberOfCooperatingGroups > 1 ){
		// the action is shared			
			rateExpression = "min( ";
					
			Iterator<Group> iter = enablingGroups.iterator();	
			group = iter.next();
			rateExpression += group.getSymbolicRateOf(descriptor, this);
			
			while(iter.hasNext()){
				rateExpression += " , ";
				group = iter.next();
				rateExpression += group.getSymbolicRateOf(descriptor, this);		
			}
			
			rateExpression += " )";
		}
		

		
		
	
		return rateExpression;
	
	}
	
}
