package data.originalModel;

import data.model.JumpVector;
import data.model.StateVariable;

// an action in the original model
public class OriginalAction{
	
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
		
	
}