package data.aggregatedModel;

import data.model.JumpVector;

public class AggregatedAction {

	private String name;
	private JumpVector jumpVector;
	private JumpVector jumpVectorMinus;
	private JumpVector jumpVectorPlus ;
	
	
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
