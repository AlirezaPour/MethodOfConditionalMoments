package data.aggregatedModel;

import java.util.Iterator;

import data.model.JumpVector;
import data.model.Rate;
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
	
	
	
}
