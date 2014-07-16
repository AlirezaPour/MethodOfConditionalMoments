package data.originalModel;

import java.util.ArrayList;
import java.util.Iterator;

import data.general.Action;
import data.general.Group;
import data.general.JumpVector;
import data.general.StateDescriptor;
import data.general.StateVariable;

// an action in the original model
public class OriginalAction extends Action{
	
	private String name;
	private JumpVector jumpVector;
	private JumpVector jumpVectorMinus;
	private JumpVector jumpVectorPlus ;
	
	
	public Integer getImpactOn(StateVariable variable){
		return jumpVector.get(variable);
	}
	
	public Integer getImpactMinusOn(StateVariable variable){
		return jumpVectorMinus.get(variable);
	}
	
	public Integer getImpactPlusOn(StateVariable variable){
		return jumpVectorPlus.get(variable);
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
	
	public String getSymbolicRateOfActionForMatlab(StateDescriptor descriptor , ArrayList<Group> allGroups){

		ArrayList<Group> enablingGroups = getEnablingGroups(allGroups);
		
		String rateExpression = "";

		Group group;
		
		int numberOfCooperatingGroups = enablingGroups.size();

		if (numberOfCooperatingGroups == 1 ){
		// the action is individual
			group = enablingGroups.get(0);
			rateExpression = group.getSymbolicRateOfActionForMatlab(descriptor, this);
			return rateExpression;
			
		}
		if (numberOfCooperatingGroups > 1 ){
		// the action is shared			
			rateExpression = "min( ";
					
			Iterator<Group> iter = enablingGroups.iterator();	
			group = iter.next();
			rateExpression += group.getSymbolicRateOfActionForMatlab(descriptor, this);
			
			while(iter.hasNext()){
				rateExpression += " , ";
				group = iter.next();
				rateExpression += group.getSymbolicRateOfActionForMatlab(descriptor, this);		
			}
			
			rateExpression += " )";
		}
		

		
		
	
		return rateExpression;
	
	}
	
}